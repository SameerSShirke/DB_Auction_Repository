package com.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "BIDS")
@Data
public class BidEntity {

//	@EmbeddedId
//	private BidId bidId;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BID_ID")
    private long bidId;

	//@ManyToOne	
    @Column(name = "BIDDER", nullable = false)
	private Long buyerId;
	
	@Column(name = "PRODUCT_ID", nullable = false)
	private Long productId;

	@Column(name = "BID_TIME", nullable = false)
	private String bidTime;

	@Column(name = "BID_PRICE", nullable = false)
	private Double bidPrice;

	@Column(name = "PRODUCT_STATUS", nullable = true)
	private String productStatus;

	@Column(name = "SOLD_PRICE", nullable = true)
	private Double soldPrice;

}
