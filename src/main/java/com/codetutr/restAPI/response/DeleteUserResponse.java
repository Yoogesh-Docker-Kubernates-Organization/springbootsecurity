package com.codetutr.restAPI.response;

public class DeleteUserResponse {
	
	private boolean success;
	
	public DeleteUserResponse(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
