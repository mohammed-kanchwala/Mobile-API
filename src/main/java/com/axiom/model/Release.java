package com.axiom.model;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Release {
	private String announceDate;
	private Double priceEur;

	public Release() {
		super();
	}
}
