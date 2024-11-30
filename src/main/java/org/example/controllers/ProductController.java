package org.example.controllers;

import org.example.databases.Category;
import org.example.databases.Product;
import org.example.services.CategoryService;
import org.example.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/inventory")
    public String showAddProductForm(Model model) {
        List<Product> products = productService.getAllProducts();
        // Выводим список продуктов в консоль
        System.out.println("Список продуктов: " + products);
        List<Category> categories = categoryService.getCategoriesForCurrentUser();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "inventory";
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductByIdForCurrentUser(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK); // Возвращаем объект и статус OK
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Возвращаем только статус NOT_FOUND
        }
    }
    // создание товара
    @PostMapping("/inventory")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try {
            productService.saveProduct(product); // Основная логика
            return ResponseEntity.ok("Продукт успешно добавлен");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    //редиктирование товара
    @PostMapping("/inventory/{id}/edit")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        try {
            productService.updateProduct(id, updatedProduct);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
