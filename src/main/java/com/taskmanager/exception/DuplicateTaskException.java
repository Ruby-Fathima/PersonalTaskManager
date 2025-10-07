package com.taskmanager.exception;

@SuppressWarnings("serial")
public class DuplicateTaskException extends Exception {

	public DuplicateTaskException(String message) {
		super(message);
	}
}
