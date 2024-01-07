package com.db.exception;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


@Data
public class AuctionExceptionResponse {
	

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	  private LocalDateTime timestamp;
	  private String message;
	  private String details;
	  private HttpStatus status;
	  private List<String> errors;
	  
	  public AuctionExceptionResponse(LocalDateTime timestamp, String message, String details, HttpStatus status, List<String> errorDetails ) {
			super();
			this.timestamp = timestamp;
			this.message = message;
			this.details = details;
			this.status = status;
			this.errors = errorDetails;
		}
	  
	  public AuctionExceptionResponse(LocalDateTime timestamp, String message,String details, HttpStatus status, String error) {
	    super();
	    this.timestamp = timestamp;
	    this.message = message;
	    this.details = details;
	    this.status = status;
	    errors = Arrays.asList(error);
	  }
	  
}
