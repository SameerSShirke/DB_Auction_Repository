package com.db.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Bid {
	
//	@NotBlank
	private Long bidId; 
	
	@NotNull
	private Double bidPrice;
	
	@NotNull
	private Long buyerId;
	
	@NotNull
	private Long productId;

}
