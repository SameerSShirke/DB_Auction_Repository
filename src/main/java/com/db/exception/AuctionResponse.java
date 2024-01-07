package com.db.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class AuctionResponse {

	private String message;
	private HttpStatus status;

	public AuctionResponse(String message, HttpStatus status) {
		super();
		this.message = message;
		this.status = status;
	}

}
