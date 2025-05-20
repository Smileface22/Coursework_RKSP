package org.example.services;

import org.example.databases.*;
import org.example.repositories.OrderRepository;
import org.example.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void createOrder(List<Long> productIds, List<Integer> quantities, float totalAmount) {
        User currentUser = userService.getCurrentUser();

        // Создание нового заказа
        Order order = new Order();
        order.setUser(currentUser);
        // Форматируем текущую дату
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = LocalDate.now().format(formatter);
        order.setOrderDate(formattedDate);
        order.setStatus("Новый");

        List<OrderItem> orderItems = new ArrayList<>();

        // Добавляем товары в заказ
        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            Integer quantity = quantities.get(i);

            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Товар не найден"));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            float itemPrice = product.getPurchasePrice() * quantity;
            orderItem.setPrice(itemPrice);
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // Сохранение заказа
        orderRepository.save(order);
    }

    // получение всех заказов у текущего юзера
    public List<Order> getAllOrders() {
        User currentUser = userService.getCurrentUser();
        return orderRepository.findByUser(currentUser);
    }

    public void updateOrderStatus(Long orderId, String status) {
        // Находим заказ по ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ с ID " + orderId + " не найден"));

        // Устанавливаем новый статус
        try {
            status = status.replace("\"", "");
            String previousStatus = order.getStatus(); // Сохраняем старый статус заказа
            order.setStatus(status);
            orderRepository.save(order); // Сохраняем изменения

            // Если статус заказа "Выполнен" и был не "Выполнен" ранее
            if ("Выполнен".equals(status) && !"Выполнен".equals(previousStatus)) {
                // Для каждого товара в заказе обновляем stockQuantity
                for (OrderItem item : order.getOrderItems()) {
                    Product product = item.getProduct(); // Получаем продукт из заказа
                    product.setStockQuantity(product.getStockQuantity() + item.getQuantity()); // Увеличиваем количество
                    productRepository.save(product); // Сохраняем изменения в продукте
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Некорректный статус заказа: " + status);
        }
    }

    // Метод для поиска заказа по ID
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ с ID " + id + " не найден"));
    }

    // Метод для подсчета заказов за сегодня
    public Long countOrdersToday(User currentUser) {
        String todayStr = LocalDate.now().format(DATE_FORMATTER);
        return orderRepository.countByUserAndOrderDate(currentUser, todayStr);
    }

    // Метод для подсчета заказов за месяц
    public long countOrdersThisMonth(User currentUser) {
        String firstOfMonthStr = LocalDate.now().withDayOfMonth(1).format(DATE_FORMATTER);  // Преобразуем первое число месяца в строковый формат
        String todayStr = LocalDate.now().format(DATE_FORMATTER);  // Текущая дата в строковом формате
        return orderRepository.countByUserAndDateRange(currentUser, firstOfMonthStr, todayStr);  // Подсчитываем заказы за месяц
    }


}

