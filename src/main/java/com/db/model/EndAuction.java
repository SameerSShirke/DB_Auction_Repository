package com.db.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EndAuction {
	
	@NotNull
	private Long sellerId;
	
	@NotNull
	private Long productId;

}
