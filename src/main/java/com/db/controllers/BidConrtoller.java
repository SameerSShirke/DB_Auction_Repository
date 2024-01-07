package com.db.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.entity.ProductsEntity;
import com.db.exception.AuctionResponse;
import com.db.model.Bid;
import com.db.model.EndAuction;
import com.db.service.BidService;
import com.db.service.ProductService;

@RestController
@RequestMapping("/db/bidController")
public class BidConrtoller {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BidService bidService;
	
	AuctionResponse auctionResponse;

	@GetMapping("/productsList")
	public ResponseEntity<List<ProductsEntity>> getAllProductList() throws Exception {

		List<ProductsEntity> productList =  productService.getProductList();
		return ResponseEntity.ok(productList);
	} 


	@PostMapping("/registerBid")
	public AuctionResponse registerBid(@Valid @RequestBody Bid bid) throws Exception {

		auctionResponse = bidService.registerBid(bid);
		return auctionResponse;
	}
	
	@PutMapping("/updateBid")
	public AuctionResponse updateBid(@Valid @RequestBody Bid bid) throws Exception {

		auctionResponse = bidService.updateBid(bid);
		return auctionResponse;
	}
	
	@DeleteMapping("/deleteBid")
	public AuctionResponse deleteBid(@Valid @RequestBody Bid bid) throws Exception {

		 auctionResponse = bidService.deleteBid(bid);
		return auctionResponse;
	}
	
	@PostMapping("/endAuction")
	public AuctionResponse endAuction(@RequestBody EndAuction endAuction) throws Exception {

		auctionResponse = bidService.endAuction(endAuction);
		return auctionResponse;
	}
}
