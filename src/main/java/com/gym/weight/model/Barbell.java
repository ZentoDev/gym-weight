package com.gym.weight.model;

import java.util.ArrayDeque;
import java.util.Deque;

import jakarta.xml.bind.annotation.XmlElement;

public class Barbell {
	
	private double weightBarbell; 
	private Deque<Plate> right;
	private Deque<Plate> left;
	
	public Barbell() {
		right = new ArrayDeque<>();
		left  = new ArrayDeque<>();
	}
	
	public Barbell(double weight) {
		this();
		this.weightBarbell = weight;
	}
	
	@XmlElement
	public double getWeightBarbell() {
		return weightBarbell;
	}
	public void setWeightBarbell(double weight) {
		this.weightBarbell = weight;
	}
	
	public Deque<Plate> getRight() {
		return right;
	}
	public Deque<Plate> getLeft() {
		return left;
	}
	
	public void pushRight(Plate plate) {
		right.addFirst(plate);
	}
	public void pushLeft(Plate plate) {
		left.addFirst(plate);
	}
	
	public Plate pullRight() {
		return right.removeFirst();
	}
	public Plate pullLeft() {
		return left.removeFirst();
	}
	
	private double getWeightOf(Deque<Plate> plates) {
		double total = 0;
		for (Plate p : plates) {
			total += p.getWeight();
		}
		return total;
	}
	public double getRightWeight() {
		return getWeightOf(right);
	}
	public double getLeftWeight() {
		return getWeightOf(left);
	}
	public double getTotalWeight() {
		return weightBarbell + getRightWeight() + getLeftWeight();
	}
	
}
