package com.inventory.App;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inventory.app.entity.Products;
import com.inventory.app.repository.ProductRepository;
import com.inventory.app.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // This is the first test case, here we check if app is able to correctly find a product using its id
    @Test
    public void testGetProductById() {
        // Arrange
        long id = 1L;
        Products product = new Products();
        product.setId(id);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // Act
        Optional<Products> result = productService.getProductById(id);

        // Assert
        assertEquals(Optional.of(product), result);
    }

    // Next here the test checks if the service is able to save a new product, comparing the test result and
    // desired output to ensure its working correctly
    @Test
    public void testSaveProduct() {
        // Arrange
        Products product = new Products();
        when(productRepository.save(product)).thenReturn(product);

        // Act
        Products result = productService.saveProduct(product);

        // Assert
        assertEquals(product, result);
    }

    
    // Test whether deletion of product is working correctly
    @Test
    public void testDeleteProduct() {
        // Arrange
        long id = 1L;

        // Act
        productService.deleteProduct(id);

        // Assert
        verify(productRepository, times(1)).deleteById(id);
    }

    // Test whether products are being updated as required and ensure that product exists before updating
    @Test
    public void testUpdateProduct_WhenProductExists_ShouldUpdateAndReturnUpdatedProduct() {
        // Arrange
        Long id = 1L;
        Products existingProduct = new Products();
        existingProduct.setId(id);
        existingProduct.setName("Existing Product");
        existingProduct.setDescription("Existing Description");
        existingProduct.setCurrentStock(10);
        existingProduct.setMinimumStockLevel(5);

        Products newProductData = new Products();
        newProductData.setName("Updated Product");
        newProductData.setDescription("Updated Description");
        newProductData.setCurrentStock(20);
        newProductData.setMinimumStockLevel(10);

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // Act
        Products result = productService.updateProduct(id, newProductData);

        // Assert
        assertEquals(newProductData.getName(), result.getName());
        assertEquals(newProductData.getDescription(), result.getDescription());
        assertEquals(newProductData.getCurrentStock(), result.getCurrentStock());
        assertEquals(newProductData.getMinimumStockLevel(), result.getMinimumStockLevel());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(existingProduct);
    }

    // Test whether update is rejected if product does not exist in the database table
    @Test
    public void testUpdateProduct_WhenProductDoesNotExist_ShouldThrowRuntimeException() {
        // Arrange
        Long id = 1L;
        Products newProductData = new Products();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> productService.updateProduct(id, newProductData));
        verify(productRepository, times(1)).findById(id);
        verifyNoMoreInteractions(productRepository);
    }

    // Test if stock quantity can be removed if there is sufficient stock
    @Test
    public void testRemoveStock_WhenStockIsSufficient_ShouldUpdateAndReturnUpdatedProduct() {
        // Arrange
        Long id = 1L;
        int currentStockValue = 3;
        Products existingProduct = new Products();
        existingProduct.setId(id);
        existingProduct.setCurrentStock(10);

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // Act
        Products result = productService.removeStock(id, currentStockValue);

        // Assert
        assertEquals(7, result.getCurrentStock());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(existingProduct);
    }

    // Test if stock removal process fails if there is insufficient existing stock
    @Test
    public void testRemoveStock_WhenStockIsInsufficient_ShouldThrowRuntimeException() {
        // Arrange
        Long id = 1L;
        int currentStockValue = 15;
        Products existingProduct = new Products();
        existingProduct.setId(id);
        existingProduct.setCurrentStock(10);

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> productService.removeStock(id, currentStockValue));
        verify(productRepository, times(1)).findById(id);
        verifyNoMoreInteractions(productRepository);
    }

    // Test to ensure that stock to be removed is not a negative number
    @Test
    public void testRemoveStock_WhenCurrentStockValueIsNegative_ShouldThrowRuntimeException() {
        // Arrange
        Long id = 1L;
        int currentStockValue = -1;
        Products existingProduct = new Products();
        existingProduct.setId(id);
        existingProduct.setCurrentStock(10);

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> productService.removeStock(id, currentStockValue));
        verify(productRepository, times(1)).findById(id);
        verifyNoMoreInteractions(productRepository);
    }

    // Test to ensure that you are prohibited from removing stock from a product that does not exist
    @Test
    public void testRemoveStock_WhenProductNotFound_ShouldThrowRuntimeException() {
        // Arrange
        Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> productService.removeStock(id, 5));
        verify(productRepository, times(1)).findById(id);
        verifyNoMoreInteractions(productRepository);
    }
}
