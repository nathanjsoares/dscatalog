package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long noExistingId;
    private long dependeceId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        noExistingId = 2L;
        dependeceId = 3L;
        product = Factory.CreateProduct();
        page = new PageImpl<>(List.of(product));
        category = Factory.CreateCategory();

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.ofNullable(product));
        Mockito.when(repository.findById(noExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.getOne(existingId)).thenReturn(product);
        Mockito.when(repository.getOne(noExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(noExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependeceId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(noExistingId);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(noExistingId)).thenReturn(false);
        Mockito.when(repository.existsById(dependeceId)).thenReturn(true);
    }

    @Test
    public void updateShouldThrollResourceNotFoundExceptionWhenIdNoExists(){
        ProductDTO productDTO = Factory.CreatProductDTO();
        Assertions.assertThrows(ResourceNotFoundException.class ,() -> {
            ProductDTO result = service.update(noExistingId,productDTO);
        });
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists(){
        ProductDTO productDTO = Factory.CreatProductDTO();
        ProductDTO result = service.update(existingId,productDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists(){
        ProductDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrollResourceNotFoundExceptionWhenIdNoExists(){
        Assertions.assertThrows(ResourceNotFoundException.class ,() -> {
            ProductDTO result = service.findById(noExistingId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    public void deleteShouldThrowsDatabaseExceptionWhenIdNotExist(){
        Assertions.assertThrows(DatabaseException.class ,() -> {
            service.delete(dependeceId);
        });
    }

    @Test
    public void deleteShouldThrowsResourceNotFoundExceptionWhenIdNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class ,() -> {
            service.delete(noExistingId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenExistId(){
        Assertions.assertDoesNotThrow( () -> {
            service.delete(existingId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }
}
