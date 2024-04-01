package com.inventory.app.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.app.entity.Products;
import com.inventory.app.repository.ProductRepository;

// This is the service class that helps perform CRUD operations on the Products Model (Entity)
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Method to get all products from table
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    // Retrieving a product using its id
    public Optional<Products> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Handles creation of a new product
    public Products saveProduct(Products product) {
        return productRepository.save(product);
    }

    // Handles updating os a given product
    public Products updateProduct(Long id, Products newProductData) {
        Optional<Products> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Products existingProduct = optionalProduct.get();
            existingProduct.setName(newProductData.getName());
            existingProduct.setDescription(newProductData.getDescription());
            existingProduct.setCurrentStock(newProductData.getCurrentStock());
            existingProduct.setMinimumStockLevel(newProductData.getMinimumStockLevel());
            return productRepository.save(existingProduct);
        } else {
            // Handle the case when the product with the given id is not found
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    // Handles removal of specific stock quantity of a product
    public Products removeStock(Long id, int currentStockValue) {
        Optional<Products> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Products existingProduct = optionalProduct.get();
            int currentStock = existingProduct.getCurrentStock();
            int updatedStock = currentStock - currentStockValue;
            if ((updatedStock >= 0) && (currentStockValue > -1) ) { // Ensuring current stock doesn't go negative
                existingProduct.setCurrentStock(updatedStock);
                return productRepository.save(existingProduct);
            } else {
                throw new RuntimeException("Insufficient stock");
            }
        } else {
            // Handle the case when the product with the given id is not found
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    // Handles deletion of a product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private static final Logger logger = Logger.getLogger(ProductService.class.getName());
    // This is a trigger to log an alert when stock level goes below minimum level
    public void checkStockLevelsAndAlert() {
        List<Products> products = productRepository.findAll();
        for (Products product : products) {
            if (product.getCurrentStock() < product.getMinimumStockLevel()) {
                // Log message instead of printing on the screen
                logger.warning("Alert: Stock level of product '" + product.getName() + "' is below minimum level!");
            }
        }
    }

}
