package com.db.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

import lombok.Data;

@Data
@Embeddable
public class BidId  	{
	
	//@ManyToOne
	@JoinColumn(name = "BIDDER", nullable = false)
	private Long buyerId;
	
	
	@Column(name = "PRODUCT_ID", nullable = false)
	private Long productId;
}
