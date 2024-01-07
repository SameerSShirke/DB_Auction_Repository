package com.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "PRODUCTS")
@Data
public class ProductsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_ID", nullable = false)
	private Long productId;

	@NotBlank
	@Size(max = 20)
	@Column(name = "PRODUCT_NAME", nullable = false)
	private String productName;

	@Column(name = "PRODUCT_DETAILS", nullable = true)
	private String productDetails;

	@NotNull
	@Column(name = "AUCTION_PRICE", nullable = false)
	private Double auctionPrice;

	@Column(name = "PRODUCT_STATUS", nullable = false)
	private String productStatus;

	@NotNull
	@Column(name = "SELLER_ID", nullable = false)
	private Long sellerId;
	
	@Column(name = "SOLD_PRICE", nullable = true)
	private Double soldPrice;
}