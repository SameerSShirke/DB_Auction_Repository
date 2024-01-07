package com.db.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.db.entity.ProductsEntity;
import com.db.exception.AuctionResponse;
import com.db.model.Products;
import com.db.repository.ProductRepository;

import javassist.NotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	private static Logger log = LogManager.getLogger("LOGGER_WITH_JSON_LAYOUT");
	HttpServletRequest request;
	String ResponseMsg = "";
	AuctionResponse responseJSON;

	public AuctionResponse registerProduct(Products product) throws Exception {

		ProductsEntity productsEntity = new ProductsEntity();
		productsEntity.setSellerId(product.getSellerId());
		productsEntity.setProductName(product.getProductName());
		productsEntity.setProductDetails(product.getProductDetails());
		productsEntity.setProductStatus("AVAILABLE");
		productsEntity.setAuctionPrice(product.getAuctionPrice());

		productRepository.save(productsEntity);

		responseJSON = new AuctionResponse("Product Added Successfully", HttpStatus.OK);

		return responseJSON;
	}

	public List<ProductsEntity> getProductList() throws Exception{
		List<ProductsEntity> productList = productRepository.findAll();
		return productList;
	}

	public AuctionResponse updateProduct(@Valid ProductsEntity productsEntity) throws Exception {

		try {
			Optional<ProductsEntity> productsDetails = Optional.ofNullable(
					productRepository.findById(productsEntity.getProductId()).orElseThrow(() -> new NotFoundException(
							"Product with id " + productsEntity.getProductId() + " not found")));

			if (productsDetails.isPresent()) {
				productRepository.save(productsEntity);
				ResponseMsg = "Product Updated Successfully";
			}

		} catch (Exception e) {
			log.info("Exception Occured: ProductService -> updateProduct() : " + e.getMessage());
			ResponseMsg = "Exception Occured: ProductService -> updateProduct() : " + e.getMessage();
			return new AuctionResponse(ResponseMsg,HttpStatus.BAD_REQUEST);
		}

		responseJSON = new AuctionResponse(ResponseMsg, HttpStatus.OK);

		return responseJSON;
	}

	public AuctionResponse deleteProduct(Long productId) throws Exception{
		try {
			Optional<ProductsEntity> productsDetails = Optional.ofNullable(productRepository.findById(productId)
					.orElseThrow(() -> new NotFoundException("Product with id " + productId + " is not found")));

			if (!productsDetails.isPresent()) {
				productRepository.deleteAll();
				ResponseMsg = "Product Updated Successfully";
			}
		} catch (Exception e) {
			log.info("Exception Occured: ProductService -> deleteProduct() : " + e.getMessage());
			ResponseMsg = "Exception Occured: ProductService -> deleteProduct() : " + e.getMessage();
			return new AuctionResponse(ResponseMsg, HttpStatus.BAD_REQUEST);
		}
		responseJSON = new AuctionResponse(ResponseMsg,HttpStatus.OK);
		return responseJSON;
	}

}
