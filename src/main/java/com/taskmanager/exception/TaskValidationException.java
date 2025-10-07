package com.taskmanager.exception;

@SuppressWarnings("serial")
public class TaskValidationException extends Exception {
	
	public TaskValidationException(String message) {
		super(message);
	}
	
}