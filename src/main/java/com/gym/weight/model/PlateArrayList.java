package com.gym.weight.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.TreeMap;

public class PlateArrayList extends ArrayList<Plate> implements PlateList{
	
	// indicates the class version
	private static final long serialVersionUID = 1L;
	
	/*@Override
	public TreeMap<Double, Plates> groupPlatesByWeight() {
		TreeMap<Double, Plates> groupPlates = new TreeMap<>(Comparator.reverseOrder());		
		// group the plates by weight
		for(Plate p : this) {
			
			// add Plate in group if already exist
			if (groupPlates.containsKey(p.getWeight()) ) {
				groupPlates.get(p.getWeight()).addPlate();
			} 
			
			// create group and add Plate
			else {
				Plates newWeightPlates = new Plates(p.getWeight());
				newWeightPlates.addPlate();
				groupPlates.put(p.getWeight(), newWeightPlates);
			}
		}
		return groupPlates;
	}
	*/
	@Override
	public TreeMap<Double, Integer> countPlatesByWeight() {
		TreeMap<Double, Integer> groupPlates = new TreeMap<>(Comparator.reverseOrder());		
		
		// count the plates by weight			
		for (Plate p : this) {
			groupPlates.merge(p.getWeight(), 1, Integer::sum);
		}
		    
		return groupPlates;
	}
	
	@Override
	public Optional<Plate> dropPlateByWeight(double weight) {
		for(Iterator<Plate> it = this.iterator(); it.hasNext(); ) {
			Plate p = it.next();
			if (Double.compare(p.getWeight(), weight) == 0) {
				it.remove();
				return Optional.of(p);
			}
		}
		return Optional.empty();
	}
	
	@Override
	public void sortListbyWeight() {
		this.sort(Comparator.comparingDouble(Plate::getWeight).reversed());
	}

}
