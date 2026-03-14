package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository repository;

    private long productId;
    private long producNotExistId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception{
        productId = 1L;
        producNotExistId = 1000L;
        countTotalProducts = 25;
    }

    @Test
    public void saveShouldPersistWithAutoicrementWhenIdIsNull(){
        Product product = Factory.CreateProduct();
        product.setId(null);
        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnNotNullWhenIdExist(){
        Product product = Factory.CreateProduct();
        product.setId(null);
        product = repository.save(product);

        Assertions.assertNotNull(repository.findById(product.getId()));
    }

    @Test
    public void findByIdShouldReturnNullWhenIdNotExist(){
        Optional<Product> result = repository.findById(producNotExistId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExist(){

        repository.deleteById(productId);

        Optional<Product> result = repository.findById(productId);

        Assertions.assertFalse(result.isPresent());
    }
}
