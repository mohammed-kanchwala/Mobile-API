package com.axiom.model;

import java.net.URL;

import lombok.Data;

@Data
public class Mobile {

	private Integer id;
	private String brand;
	private String phone;
	private URL picture;

	private Release release;

	private String sim;
	private String resolution;

	private Hardware hardware;

	public Mobile() {
		super();
	}
}
