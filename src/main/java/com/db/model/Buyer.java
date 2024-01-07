package com.db.model;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.db.entity.BidEntity;

import lombok.Data;

@Data
public class Buyer {
	
	private Long buyerId;
	
	@NotBlank
    @Size(max = 30)
	private String buyerName;
	
	@NotBlank
    @Size(max = 30)
	@Email
	private String email;
	
	@NotBlank
	private String password;
	
//	private 
	
	private List<Products> viewProductList;
	
	private List<BidEntity> viewBidList;

}