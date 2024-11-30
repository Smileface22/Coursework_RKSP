package org.example.repositories;

import org.example.databases.Product;
import org.example.databases.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUser(User user);// Метод для поиска продуктов по пользователю
    Product findByIdAndUser(Long id, User user);
    List<Product> findByUserAndCategoryId(User user, Long categoryId);

    @Query("SELECT SUM(p.stockQuantity) FROM Product p")
    int sumStockQuantity(); // Суммируем количество всех товаров
    @Query("SELECT COUNT(p) FROM Product p WHERE p.stockQuantity < :threshold")
    int countLowStockProducts(int threshold); // Подсчитываем товары с низким уровнем запаса
}
