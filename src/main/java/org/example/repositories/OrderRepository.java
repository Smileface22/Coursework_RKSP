package org.example.repositories;

import org.example.databases.Order;
import org.example.databases.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    int countByUserAndStatus(User user, String status); // Подсчитываем количество заказов с конкретным статусом
    // Метод для подсчета заказов по дате
    Long countByUserAndOrderDate(User user, String orderDate);
    // Метод для подсчета заказов между двумя датами
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user = :user AND o.orderDate BETWEEN :startDate AND :endDate")
    long countByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}
