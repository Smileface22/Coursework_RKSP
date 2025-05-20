package org.example.services;

import jakarta.transaction.Transactional;
import org.example.databases.Category;
import org.example.databases.Product;
import org.example.databases.User;
import org.example.repositories.CategoryRepository;
import org.example.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;

    // Метод для добавления новой категории
    public boolean addCategory(Category category) {
        User currentUser = userService.getCurrentUser();
        category.setUser(currentUser); // Привязываем категорию к текущему пользователю
        if (categoryRepository.findByName(category.getName()) != null) {
            return false; // Если категория с таким именем уже существует, возвращаем false
        }

        categoryRepository.save(category); // Сохраняем категорию в базе данных
        return true;
    }

    //получение категории по id
    public Category getCategoryById(Long id) {
        // Используем Optional, чтобы безопасно обрабатывать отсутствие результата
        Category categoryOptional = categoryRepository.findById(id).orElse(null);
        return categoryOptional; // Возвращаем категорию или null, если не найдена
    }

    //список категорий текущего юзера
    public List<Category> getCategoriesForCurrentUser() {
        User currentUser = userService.getCurrentUser(); // Получаем текущего пользователя
        return categoryRepository.findByUser(currentUser); // Возвращаем категории, принадлежащие текущему пользователю
    }

    //обновление категории
    public boolean updateCategory(Long id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory != null) {
            existingCategory.setName(updatedCategory.getName());
            existingCategory.setDescription(updatedCategory.getDescription());
            categoryRepository.save(existingCategory);
            return true;
        }
        return false;
    }

    //удаление категории
    public void deleteCategoryById(Long categoryId) {
        // Получаем текущего пользователя
        User currentUser = userService.getCurrentUser();

        // Получаем все продукты текущего пользователя, которые принадлежат категории с заданным ID
        List<Product> products = productRepository.findByUserAndCategoryId(currentUser, categoryId);

        // Обновляем связь с категорией (устанавливаем null)
        for (Product product : products) {
            product.setCategory(null);
        }
        productRepository.saveAll(products); // Сохраняем обновленные продукты

        // Удаляем категорию
        categoryRepository.deleteById(categoryId);
    }

}