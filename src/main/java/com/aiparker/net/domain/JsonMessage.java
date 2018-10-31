package com.aiparker.net.domain;

import java.util.Arrays;

public class JsonMessage {

	private byte[] byte_header = new byte[2];

	private byte[] byte_length = new byte[2];
	
	private String message = null;
	
	private byte[] byte_end = new byte[1];

	public byte[] getByte_end() {
		return byte_end;
	}

	public void setByte_end(byte[] byte_end) {
		this.byte_end = byte_end;
	}

	public byte[] getByte_header() {
		return byte_header;
	}

	public void setByte_header(byte[] byte_header) {
		this.byte_header = byte_header;
	}

	public byte[] getByte_length() {
		return byte_length;
	}

	public void setByte_length(byte[] byte_length) {
		this.byte_length = byte_length;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "JsonMessage [byte_header=" + Arrays.toString(byte_header) + ", byte_length="
				+ Arrays.toString(byte_length) + ", message=" + message + ", byte_end=" + Arrays.toString(byte_end)
				+ "]";
	}

	
	
}
