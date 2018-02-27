package com.github.msx80.runtime;

public class OmicronException extends RuntimeException {

	private static final long serialVersionUID = 1638760593528045324L;

	public OmicronException() {
	}

	public OmicronException(String message) {
		super(message);
	}

	public OmicronException(Throwable cause) {
		super(cause);
	}

	public OmicronException(String message, Throwable cause) {
		super(message, cause);
	}

	public OmicronException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
