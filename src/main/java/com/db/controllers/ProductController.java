package com.db.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.entity.ProductsEntity;
import com.db.exception.AuctionResponse;
import com.db.model.Products;
import com.db.service.ProductService;


@RestController
@RequestMapping("/db/auctionProducts")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	AuctionResponse auctionResponse;

	// Register Product
	@PostMapping("/products")
	public AuctionResponse addAuctionProduct(@Valid @RequestBody Products products) throws Exception {

		auctionResponse = productService.registerProduct(products);
		return auctionResponse;
	}

	// GetProduct Details
	@GetMapping("/productsList")
	public ResponseEntity<List<ProductsEntity>> getAllProductList() throws Exception {

		List<ProductsEntity> productList =  productService.getProductList();
		return ResponseEntity.ok(productList);
	}
	
	// Update Product
	@PutMapping("/updateProduct")
	public AuctionResponse updateAuctionProduct(@Valid @RequestBody ProductsEntity productsEntity) throws Exception {

		 auctionResponse = productService.updateProduct(productsEntity);
		return auctionResponse;
	}
	
	// Delete Product
	@DeleteMapping("/{productId}")
	public AuctionResponse deleteAuctionProduct(@PathVariable Long productId) throws Exception {

		 auctionResponse = productService.deleteProduct(productId);
		return auctionResponse;
	}

	
}
