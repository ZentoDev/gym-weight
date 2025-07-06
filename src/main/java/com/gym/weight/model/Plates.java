package com.gym.weight.model;

public class Plates {
	
	private final double weight;
	private int totalPlate;
	private int availablePlate;
	
	public Plates(double weight) {
		this.weight = weight;
		this.totalPlate = 0;
		this.availablePlate = 0;
	}
	
	public Plates(double weight, int totalCount) {
		this.weight = weight;
		this.totalPlate = totalCount;
		this.availablePlate = totalCount;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;  // same reference
	    if (obj == null || getClass() != obj.getClass()) return false;
	    // same key (weight)
	    Plates plates = (Plates) obj;
	    return Double.compare(plates.weight, weight) == 0;
	}
	
	@Override
	public int hashCode() {
	    return Double.hashCode(weight);
	}
	
	public int getTotalPlate() {
		return totalPlate;
	}
	public int getAvailablePlate() {
		return availablePlate;
	}
	public double getWeight() {
		return weight;
	}

	public void addPlate() {
		this.totalPlate++;
		this.availablePlate++;
	}
	
	public boolean releasePlate() {
		if (this.availablePlate < this.totalPlate) {
			this.availablePlate++;
			return true;
		}
		return false;
	}
	
	public boolean usePlate() {
		if (this.availablePlate > 0) {
			this.availablePlate--;
			return true;
		}
		return false;
	}

}
