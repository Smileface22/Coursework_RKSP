package org.example.controllers;

import org.example.repositories.OrderRepository;
import org.example.repositories.ProductRepository;
import org.example.services.OrderService;
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

    // Метод для получения информации о текущих запасах
    @GetMapping("/api/metrics/current-stock")
    public ResponseEntity<Map<String, Object>> getCurrentStock() {
        int totalStock = productRepository.sumStockQuantity(); // Метод для подсчета общего количества
        int lowStockCount = productRepository.countLowStockProducts(10); // Метод для подсчета товаров с низким уровнем
        Map<String, Object> response = new HashMap<>();
        response.put("totalStock", totalStock);
        response.put("lowStockCount", lowStockCount);
        return ResponseEntity.ok(response);
    }
    // Метод для получения статуса заказов
    @GetMapping("/api/metrics/order-status")
    public ResponseEntity<Map<String, Integer>> getOrderStatus() {
        int completedCount = orderRepository.countByStatus("Выполнен");
        int processingCount = orderRepository.countByStatus("В процессе");
        Map<String, Integer> response = new HashMap<>();
        response.put("completed", completedCount);
        response.put("processing", processingCount);
        return ResponseEntity.ok(response);
    }
    // Метод для получения статистики по заказам за сегодня и за месяц
    @GetMapping("/metrics/order-count")
    public ResponseEntity<Map<String, Long>> getOrderCountMetrics() {
        Map<String, Long> orderMetrics = new HashMap<>();
        orderMetrics.put("ordersToday", orderService.countOrdersToday());
        orderMetrics.put("ordersThisMonth", orderService.countOrdersThisMonth());
        return ResponseEntity.ok(orderMetrics);
    }
}
