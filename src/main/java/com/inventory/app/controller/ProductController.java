package com.inventory.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.app.entity.Products;
// import com.inventory.app.error.ErrorClass;
import com.inventory.app.service.ProductService;


// in this controller endpoints are defined to ensure functionality of CRUD operations on product from the front end
@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    // Get route to retrieve all products 
    @GetMapping("/allProducts")
    public List<Products> getAllProducts() {
        return productService.getAllProducts();
    }

    // get route to retrieve a single product using its id
    @GetMapping("/product/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable Long id) {
        Optional<Products> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // put route for updating a specific product in the table, product is identified by id
    @PutMapping("/product/{id}")
    public ResponseEntity<Products> updateProduct(@PathVariable Long id, @RequestBody Products newProductData) {
        try {
            Products updatedProduct = productService.updateProduct(id, newProductData);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            // Handle the case when the product with the given id is not found
            return ResponseEntity.notFound().build();
        }
    }

    // put method to allow for removal of specific tock quantity of a product, both product id and 
    // quantity to be removed a passed in the route
    @PutMapping("/removeStock/{id}/{currentStockValue}")
    public ResponseEntity<Products> removeStock(@PathVariable Long id, @PathVariable int currentStockValue ) {
        // System.out.println("Running");
        try {
            Products updatedProduct = productService.removeStock(id, currentStockValue);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // post method to create a new product
    @PostMapping("/product")
    public ResponseEntity<Products> createProduct(@RequestBody Products product) {
        Products savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // delete route to delete a specific product by id passed in the route
    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
