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
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    // Получение списка заказов (и, при необходимости, товаров)
    @GetMapping
    public ResponseEntity<Map<String, Object>> getOrdersWithProducts() {
        Map<String, Object> response = new HashMap<>();
        List<Order> orders = orderService.getAllOrders();
        response.put("orders", orders);
        response.put("products", productService.getAllProducts());
        System.out.println("Orders from backend: " + orders);
        return ResponseEntity.ok(response);
    }

    // Получение информации о конкретном заказе по ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable Long id) {
        Order order = orderService.findById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

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

    // Создание нового заказа
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestParam("productIds") List<Long> productIds,
                                         @RequestParam("quantities") List<Integer> quantities,
                                         @RequestParam("totalAmount") float totalAmount) {
        try {
            orderService.createOrder(productIds, quantities, totalAmount);
            return ResponseEntity.ok("Заказ успешно создан");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при создании заказа: " + e.getMessage());
        }
    }

    // Обновление статуса заказа
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id,
                                               @RequestBody String status) {
        try {
            orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok("Статус заказа обновлен");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
        }
    }
}
