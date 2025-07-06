package com.gym.weight.model;

public class Operation {
    private String action;
    private double weight;
    
    public Operation(String action, double weight) {
        this.action = action;
        this.weight = weight;
    }

	public String getAction() {
		return action;
	}

	public double getWeight() {
		return weight;
	}
    
}