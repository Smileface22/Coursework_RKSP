package org.example.repositories;

import org.example.databases.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Дополнительные запросы можно добавить здесь
}
