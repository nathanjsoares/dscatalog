package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

	public static Product CreateProduct(){
		Product product = new Product(1L,"Smartphone", "Good phone", 170.0, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/25-big.jpg", Instant.now());
		product.getCategories().add(CreateCategory());
		return product;
	}

	public static ProductDTO CreatProductDTO(){
		Product product = CreateProduct();
		return new ProductDTO(product,product.getCategories());
	}

	public static Category CreateCategory(){
		return new Category(1L,"Eletronics");
	}
}
