package com.pgr301.exam;

public class BackEndException extends RuntimeException {
	public BackEndException(String msg, Throwable cause) {
		super(msg, cause);
		BankAccountController.postError(msg, cause);
	}
	public BackEndException(String msg) {
		super(msg);
		BankAccountController.postError(msg);
	}
	public BackEndException() {
		super();
		BankAccountController.postError(this);
	}
}
