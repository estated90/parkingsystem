package com.parkit.parkingsystem.config;

public class Url {

	private String url() {
			String url = "jdbc:mysql://localhost:3306/prod";
		return url;
	}

	public String getURL() {
		return url();
	}

}
