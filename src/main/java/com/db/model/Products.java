package com.db.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Products {
	

	@NotBlank
	@Size(max = 20)
	private String productName;
	
	@Size(max = 100)
	private String productDetails;
	
	@NotNull
	private Double auctionPrice;
	
//	@NotNull
//	@NotBlank
//	private String productStatus;
	
	@NotNull
	private Long sellerId;
	

}

