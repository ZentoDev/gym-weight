package com.gym.weight.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "weight", "material" })
public class Plate {
	
	private double weight;
	private String material;
	
	public Plate(double weight) {
		this.weight = weight;
	}
	
	public Plate(int weight, String material) {
		this(weight);
		this.material = material;
	}

	@XmlElement
	public double getWeight() {
		return weight;
	}
	
	@XmlElement
	public String getMaterial() {
		return material;
	}

}
