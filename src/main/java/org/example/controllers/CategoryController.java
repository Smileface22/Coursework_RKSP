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

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public String category(Model model) {
        model.addAttribute("categories", categoryService.getCategoriesForCurrentUser());
        return "category";
    }

    // Добавить новую категорию
    @PostMapping("/category")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        boolean success = categoryService.addCategory(category);
        if (success) {
            return ResponseEntity.ok("Категория успешно добавлена");
        } else {
            return ResponseEntity.status(400).body("Категория с таким именем уже существует");
        }
    }

    // Получить данные категории по её ID
    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.status(404).build(); // Возвращаем 404, если категория не найдена
        }
    }

    //редактирование категории
    @PostMapping("/category/{id}/edit")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody Category updatedCategory) {
        boolean success = categoryService.updateCategory(id, updatedCategory);
        if (success) {
            return ResponseEntity.ok("Категория успешно обновлена");
        } else {
            return ResponseEntity.status(400).body("Ошибка при обновлении категории");
        }
    }

    //удаление категории
    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok().build(); // Возвращаем успешный ответ без перенаправления
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Возвращаем ошибку
        }
    }
}
