package com.shop.bike.consumer.dto;

import javax.validation.constraints.NotBlank;

public class ResetPasswordInitVM {

	private String login;

	private String otp;

	private String password;

	private String activatedKey;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getActivatedKey() {
		return activatedKey;
	}

	public void setActivatedKey(String activatedKey) {
		this.activatedKey = activatedKey;
	}
}