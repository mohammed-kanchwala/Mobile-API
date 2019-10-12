package com.axiom.model;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Hardware {
	private String audioJack;
	private String gps;
	private String battery;

	public Hardware() {
		super();
	}
}
