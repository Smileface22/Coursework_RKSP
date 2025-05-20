package org.example.controllers;

import org.example.databases.Category;
import org.example.services.CategoryService;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Получить список категорий (для текущего пользователя)
    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategoriesForCurrentUser();
        return ResponseEntity.ok(categories);
    }

    // Добавить новую категорию
    @PostMapping
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        boolean success = categoryService.addCategory(category);
        if (success) {
            return ResponseEntity.ok("Категория успешно добавлена");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Категория с таким именем уже существует");
        }
    }

    // Получить данные категории по её ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Редактировать категорию по ID
    @PostMapping("/{id}/edit")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody Category updatedCategory) {
        boolean success = categoryService.updateCategory(id, updatedCategory);
        if (success) {
            return ResponseEntity.ok("Категория успешно обновлена");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при обновлении категории");
        }
    }

    // Удалить категорию по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
