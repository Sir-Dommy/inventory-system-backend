package com.inventory.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.validator.constraints.Range;

@Entity
public class Products {
    
    // This will be the primary identifier in the table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // making the name colum unique so that each product name is unique
    @Column(unique = true)
    
    private String name;
    private String description;
    
    @Range(min = 0, message = "Current stock must be greater than or equal to -1")
    private int currentStock;
    
    @Range(min = 0, message = "Minimum stock level must be greater than or equal to -1")
    private int minimumStockLevel;


    // This is the default constructor
    public Products() {
        
    }
    
    // This constructor contains all the values that can be reference from the service class
    public Products(String name, String description, int currentStock, int minimumStockLevel) {
        this.name = name;
        this.description = description;
        this.currentStock = currentStock;
        this.minimumStockLevel = minimumStockLevel;
    }

    // Below are all the required getters and setters methods
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getCurrentStock() {
        return currentStock;
    }
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }
    public int getMinimumStockLevel() {
        return minimumStockLevel;
    }
    public void setMinimumStockLevel(int minimumStockLevel) {
        this.minimumStockLevel = minimumStockLevel;
    }
}
