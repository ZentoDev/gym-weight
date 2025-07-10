package com.gym.weight.model;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public interface PlateList extends List<Plate>{
	
	//public TreeMap<Double, Plates> groupPlatesByWeight();
	public TreeMap<Double, Integer> countPlatesByWeight();
	public Optional<Plate> dropPlateByWeight(double weight);
	public void sortListbyWeight();
	public double getMin();

}
