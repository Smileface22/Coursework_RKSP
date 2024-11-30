package org.example.databases;


import jakarta.persistence.*;

@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private String description;
    // Связь с таблицей пользователей
    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Пользователь, создавший категорию

    public Category(){};

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }
}
