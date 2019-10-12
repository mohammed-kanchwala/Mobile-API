package com.axiom.model;

import java.net.URL;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Mobile {
	@Id
	private Long id;
	private String brand;
	private String phone;
	private URL picture;

	private Release release;

	private String sim;
	private String resolution;

	private Hardware hardware;

	public Mobile () {
		super();
	}
}
