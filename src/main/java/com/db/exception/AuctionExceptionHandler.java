

package com.db.exception;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AuctionExceptionHandler extends ResponseEntityExceptionHandler {

		private static Logger loggerWithJsonLayout = LogManager.getLogger("LOGGER_WITH_JSON_LAYOUT");
	

      	@ExceptionHandler(Exception.class)
		public final ResponseEntity<AuctionExceptionResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());
	
			List<String> errorDetails = new ArrayList<String>();
			errorDetails.add(ex.getMessage());
	
			AuctionExceptionResponse responseJSON = new AuctionExceptionResponse(LocalDateTime.now(), ex.getMessage(),
					request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, errorDetails);
			return new ResponseEntity<>(responseJSON, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		@ExceptionHandler(RecordNotFoundException.class)
		public final ResponseEntity<AuctionExceptionResponse> handleRecordNotFoundException(RecordNotFoundException ex,
				HttpServletRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());
	
			List<String> errorDetails = new ArrayList<String>();
			errorDetails.add(ex.getMessage());
			String customMessage = "Record Not Found In Database, Please Check Request";
	
			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), customMessage,
					request.getRequestURI(), HttpStatus.NOT_FOUND, errorDetails);
			return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
		}
	 
		
		@Override
		protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
				HttpHeaders headers, HttpStatus status, WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());
			StringBuilder message = new StringBuilder();
			List<String> errors = new ArrayList<String>();
			for (FieldError error : ex.getBindingResult().getFieldErrors()) {
				errors.add("'"+error.getField() + "' " +error.getDefaultMessage());
				message.append( "'"+ error.getField() + "' " + error.getDefaultMessage());
			}
			for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
				errors.add("'"+error.getObjectName() + "' " + error.getDefaultMessage());
				message.append(", ");
				message.append(error.getDefaultMessage());
			}
				

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), message.toString(),
					request.getContextPath(), HttpStatus.BAD_REQUEST, errors);
			return handleExceptionInternal(ex, exceptionResponse, headers, exceptionResponse.getStatus(), request);
		}
		
		
		@ExceptionHandler(MethodArgumentTypeMismatchException.class)
		protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
				WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());

			List<String> errorDetails = new ArrayList<String>();
			errorDetails.add(ex.getMessage());

			AuctionExceptionResponse err = new AuctionExceptionResponse(LocalDateTime.now(),
					"Input Type Mismatch, Please Check the Request", request.getContextPath(), HttpStatus.BAD_REQUEST,errorDetails);

			return new ResponseEntity<>(err, err.getStatus());
		}
	  
		@Override
		protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
				HttpHeaders headers, HttpStatus status, WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());
			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMethod());
			builder.append("Method is not supported for this request. Supported methods are ");
			ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					request.getContextPath(), HttpStatus.METHOD_NOT_ALLOWED, builder.toString());
			
			return new ResponseEntity<Object>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
		}
	
		@Override
		protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
				HttpHeaders headers, HttpStatus status, WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());

			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("Media is not supported for this request. Supported methods are ");
			ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
			
			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					request.getContextPath(), HttpStatus.NOT_ACCEPTABLE, builder.toString());
			
			return new ResponseEntity<Object>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
		}

		@Override
		protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
				HttpStatus status, WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());

			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("Missing Path Variable");
			ex.getVariableName();

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					request.getContextPath(), HttpStatus.BAD_REQUEST, builder.toString());

			return new ResponseEntity<Object>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
		}

		
		@Override
		protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
				HttpHeaders headers, HttpStatus status, WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());

			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("Media Type Not Supported, Supported media types are ");
			ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

			List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
			if (!CollectionUtils.isEmpty(mediaTypes)) {
				headers.setAccept(mediaTypes);
				if (request instanceof ServletWebRequest) {
					ServletWebRequest servletWebRequest = (ServletWebRequest) request;
					if (HttpMethod.PATCH.equals(servletWebRequest.getHttpMethod())) {
						headers.setAcceptPatch(mediaTypes);
					}
				}
			}

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					request.getContextPath(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.toString());

			return new ResponseEntity<Object>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
		}
		
		
		@Override
		protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
				HttpHeaders headers, HttpStatus status, WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());

			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("Conversion Not Supported");
			ex.getRootCause();

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					request.getContextPath(), HttpStatus.INTERNAL_SERVER_ERROR, builder.toString());

			return new ResponseEntity<Object>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());

		}
	 
		@ExceptionHandler(TimeoutException.class)
		public ResponseEntity<AuctionExceptionResponse> handleTimeoutException(TimeoutException ex,
				HttpServletRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());

			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("Could Not Connect To The Server In Specified Time");
			ex.getCause();

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					request.getRequestURI(), HttpStatus.GATEWAY_TIMEOUT, builder.toString());

			return new ResponseEntity<>(exceptionResponse, HttpStatus.GATEWAY_TIMEOUT);

		}
		
		@Override
		protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex,
				HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
			loggerWithJsonLayout.error(ex.getMessage());

			if (webRequest instanceof ServletWebRequest) {
				ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
				HttpServletResponse response = servletWebRequest.getResponse();
				if (response != null && response.isCommitted()) {
					if (logger.isWarnEnabled()) {
						logger.warn("Async request timed out");
					}
					return null;
				}
			}

			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("Async request timed out");

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					webRequest.getContextPath(), HttpStatus.SERVICE_UNAVAILABLE, builder.toString());

			return new ResponseEntity<Object>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
		}
	
		@ExceptionHandler(DataIntegrityViolationException.class)
		public ResponseEntity<AuctionExceptionResponse> handleDataIntegrityViolationException(
				DataIntegrityViolationException ex, WebRequest webRequest) {
			loggerWithJsonLayout.error(ex.getMessage());

			Throwable throwable = ex.getCause();
			String errorMessage = null;
			while (throwable != null) {
				if (throwable instanceof SQLIntegrityConstraintViolationException)
					errorMessage = throwable.getMessage();
				throwable = throwable.getCause();
			}

			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("CONFLICT OCCURED, Please Check The Data");

			List<String> errorDetails = new ArrayList<String>();
			errorDetails.add(ex.getMessage());
			errorDetails.add(errorMessage);
			errorDetails.add(builder.toString());

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					webRequest.getContextPath(), HttpStatus.CONFLICT, errorDetails);

			return new ResponseEntity<AuctionExceptionResponse>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());

		}

		@ExceptionHandler({ SQLException.class, DataAccessException.class })
		public ResponseEntity<AuctionExceptionResponse> handleDatabaseException(Exception ex, WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());

			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("SQL Exception Occured");

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					request.getContextPath(), HttpStatus.NOT_ACCEPTABLE, builder.toString());

			return new ResponseEntity<AuctionExceptionResponse>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
		}
	  
		@ExceptionHandler(IllegalArgumentException.class)
		public ResponseEntity<AuctionExceptionResponse> handleIllegalArgumentException(Exception ex, WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());
			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("Illegal Argument");

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					request.getContextPath(), HttpStatus.NOT_ACCEPTABLE, builder.toString());
			
			return new ResponseEntity<AuctionExceptionResponse>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
		}
		
		
		@ExceptionHandler(ConstraintViolationException.class)
		public ResponseEntity<AuctionExceptionResponse> handleconstraintViolationException(Exception ex,
				WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());
			// response.sendError(HttpStatus.BAD_REQUEST.value());
			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("Not A Valid Input");

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					request.getContextPath(), HttpStatus.BAD_REQUEST, builder.toString());

			return new ResponseEntity<AuctionExceptionResponse>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
		}

		@ExceptionHandler({NullPointerException.class, ArrayIndexOutOfBoundsException.class, IOException.class})
		public ResponseEntity<AuctionExceptionResponse> handleNullPointerException(Exception ex, WebRequest request) {
			loggerWithJsonLayout.error(ex.getMessage());
			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("Object/Data Is Empty, Can Not Perform Any Operation On It");

			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					request.getContextPath(), HttpStatus.UNPROCESSABLE_ENTITY, builder.toString());

			return new ResponseEntity<AuctionExceptionResponse>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
		}
	  
	
		@ExceptionHandler(UnauthorizedException.class)
		public ResponseEntity<AuctionExceptionResponse> unauthorizedException(UnauthorizedException ex,
				WebRequest webRequest) {
			loggerWithJsonLayout.error(ex.getMessage()); 
	
			StringBuilder builder = new StringBuilder();
			builder.append(ex.getMessage());
			builder.append("User Is NOT Authorized");
	
			AuctionExceptionResponse exceptionResponse = new AuctionExceptionResponse(LocalDateTime.now(), ex.getLocalizedMessage(),
					webRequest.getContextPath(), HttpStatus.UNAUTHORIZED, builder.toString());
			
			return new ResponseEntity<AuctionExceptionResponse>(exceptionResponse, new HttpHeaders(),
					exceptionResponse.getStatus());
		}
	    

	    
		
}
