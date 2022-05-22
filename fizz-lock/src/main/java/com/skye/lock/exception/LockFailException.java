package com.skye.lock.exception;

public class LockFailException extends RuntimeException {

	private static final long serialVersionUID = 3466572399967063163L;

	public LockFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public LockFailException(Throwable cause) {
		super(cause);
	}

	public LockFailException(String message) {
		super(message);
	}

	public LockFailException() {
		super();
	}

}
