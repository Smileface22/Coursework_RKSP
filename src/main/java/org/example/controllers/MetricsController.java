package org.example.controllers;

import org.example.databases.User;
import org.example.repositories.OrderRepository;
import org.example.repositories.ProductRepository;
import org.example.services.OrderService;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MetricsController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;

    // Метод для получения информации о текущих запасах
    @GetMapping("/api/metrics/current-stock")
    public ResponseEntity<Map<String, Object>> getCurrentStock() {
        User currentUser = userService.getCurrentUser();
        int totalStock = productRepository.sumStockQuantityByUser(currentUser);
        int lowStockCount = productRepository.countLowStockProductsByUser(currentUser, 10);
        Map<String, Object> response = new HashMap<>();
        response.put("totalStock", totalStock);
        response.put("lowStockCount", lowStockCount);
        return ResponseEntity.ok(response);
    }
    // Метод для получения статуса заказов
    @GetMapping("/api/metrics/order-status")
    public ResponseEntity<Map<String, Integer>> getOrderStatus() {
        User currentUser = userService.getCurrentUser();
        int completedCount = orderRepository.countByUserAndStatus(currentUser, "Выполнен");
        int processingCount = orderRepository.countByUserAndStatus(currentUser, "В процессе");
        Map<String, Integer> response = new HashMap<>();
        response.put("completed", completedCount);
        response.put("processing", processingCount);
        return ResponseEntity.ok(response);
    }
    // Метод для получения статистики по заказам за сегодня и за месяц
    @GetMapping("/api/metrics/order-count")
    public ResponseEntity<Map<String, Long>> getOrderCountMetrics() {
        User currentUser = userService.getCurrentUser();
        Map<String, Long> orderMetrics = new HashMap<>();
        orderMetrics.put("ordersToday", orderService.countOrdersToday(currentUser));
        orderMetrics.put("ordersThisMonth", orderService.countOrdersThisMonth(currentUser));
        return ResponseEntity.ok(orderMetrics);
    }
}
