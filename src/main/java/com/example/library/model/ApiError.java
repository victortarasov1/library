package com.example.library.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApiError {
	private String message;
	  private String debugMessage;
	  @JsonInclude(JsonInclude.Include.NON_NULL)
	  private List<String> errors;
	  public String getMessage() {
		  return message;
	  }
	  public void setMessage(String message) {
		  this.message = message;
	  }
	  public String getDebugMessage() {
		  return debugMessage;
	  }
	  
	  public List<String> getErrors() {
		return errors;
	  }
	  public void setErrors(List<String> errors) {
		 this.errors = errors;
	 }
	 public void setDebugMessage(String debugMessage) {
		  this.debugMessage = debugMessage;
	 }
	 public ApiError() {};
	 public ApiError(String message, String debugMessage) {
	  this.message = message;
	  this.debugMessage = debugMessage;
	 }
	 public ApiError(String message, String debugMessage, List <String> errors) {
		 this.message = message;
		 this.debugMessage = debugMessage;
		 this.errors = errors;
	}
}
