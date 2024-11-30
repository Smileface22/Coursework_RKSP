package org.example.repositories;

import org.example.databases.Order;
import org.example.databases.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    int countByStatus(String status); // Подсчитываем количество заказов с конкретным статусом
    // Метод для подсчета заказов по дате
    Long countByOrderDate(String orderDate);
    // Метод для подсчета заказов между двумя датами
    long countByOrderDateBetween(String startDate, String endDate);
}
