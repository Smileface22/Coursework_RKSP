package org.example.controllers;


import org.example.databases.Order;
import org.example.services.OrderService;
import org.example.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String showCreateOrderForm(Model model) {
        // Получаем все товары из базы данных
        model.addAttribute("products", productService.getAllProducts());
        List<Order> orders = orderService.getAllOrders(); // Получаем заказы из сервиса
        model.addAttribute("orders", orders);
        return "orders"; // Название шаблона, который будет отображен
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable Long id) {
        Order order = orderService.findById(id);  // Находим заказ по ID
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Строим ответ с нужными данными
        Map<String, Object> response = new HashMap<>();
        response.put("products", order.getOrderItems().stream()
                .map(item -> {
                    Map<String, Object> product = new HashMap<>();
                    product.put("productName", item.getProduct().getName());
                    product.put("quantity", item.getQuantity());
                    product.put("price", item.getPrice());
                    return product;
                })
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    // создание заказа
    @PostMapping("/orders/create")
    public ResponseEntity<?> createOrder(@RequestParam("productIds") List<Long> productIds,
                                         @RequestParam("quantities") List<Integer> quantities,
                                         @RequestParam("totalAmount") float totalAmount) {
        try {
            // Логируем входные данные
            System.out.println("Received productIds: " + productIds);
            System.out.println("Received quantities: " + quantities);
            System.out.println("Received totalAmount: " + totalAmount);
            // Взаимодействие с сервисом для создания заказа
            orderService.createOrder(productIds, quantities, totalAmount);
            // Ответ в случае успеха
            return ResponseEntity.ok("Заказ успешно создан");
        } catch (Exception e) {
            // Логирование ошибки и отправка ответа с ошибкой
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при создании заказа: " + e.getMessage());
        }
    }

    // Обновление статуса заказа
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        try {
            orderService.updateOrderStatus(id, status); // Передаем в сервис
            return ResponseEntity.ok("Статус заказа обновлен");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
        }
    }
}
