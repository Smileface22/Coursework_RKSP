package org.example.services;

import org.example.databases.Product;
import org.example.databases.User;
import org.example.repositories.CategoryRepository;
import org.example.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;

    public void saveProduct(Product product) {
        // Устанавливаем текущего пользователя в продукт
        User currentUser = userService.getCurrentUser();
        product.setUser(currentUser);
        // Сохраняем продукт в базе данных
        productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        User currentUser = userService.getCurrentUser();  // Получаем текущего пользователя
        return productRepository.findByUser(currentUser);  // Возвращаем продукты, принадлежащие текущему пользователю
    }
    public Product getProductByIdForCurrentUser(Long id) {
        User currentUser = userService.getCurrentUser();
        return productRepository.findByIdAndUser(id, currentUser);
    }

    public void updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));

        // Обновление данных
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPurchasePrice(updatedProduct.getPurchasePrice());
        existingProduct.setSellingPrice(updatedProduct.getSellingPrice());
        existingProduct.setCategory(updatedProduct.getCategory());

        productRepository.save(existingProduct);
    }
}
