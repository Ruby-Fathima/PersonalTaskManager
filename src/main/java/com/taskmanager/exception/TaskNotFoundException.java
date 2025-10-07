package com.taskmanager.exception;

@SuppressWarnings("serial")
public class TaskNotFoundException extends Exception {

	public TaskNotFoundException(String message) {
		super(message);
	}
	
}
