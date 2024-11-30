package org.example.databases;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Идентификатор заказа

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Пользователь, сделавший заказ

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>(); // Список товаров в заказе

    private float totalAmount; // Общая сумма заказа


    private String status; // Статус заказа

    private String orderDate; // Дата заказа

    // Конструктор по умолчанию
    public Order() {
    }

    // Конструктор с параметрами
    public Order(User user, float totalAmount, String status, String orderDate) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    // Удобный метод для добавления элемента заказа
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this); // Устанавливаем связь с заказом
    }

    // Удобный метод для удаления элемента заказа
    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null); // Разрываем связь
    }
}

