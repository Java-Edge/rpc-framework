package com.study.javaedge.rpc.common.protocol;

public enum Status {
	SUCCESS(200, "SUCCESS"), ERROR(500, "ERROR"), NOT_FOUND(404, "NOT FOUND");

	private int code;

	private String message;

	private Status(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
