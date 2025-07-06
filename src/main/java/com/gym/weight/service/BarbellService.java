package com.gym.weight.service;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.gym.weight.model.Barbell;
import com.gym.weight.model.Operation;
import com.gym.weight.model.Plate;
import com.gym.weight.model.PlateArrayList;
import com.gym.weight.model.PlateList;
import com.gym.weight.model.Plates;
import com.gym.weight.repository.IWeightsRepository;

@Service("active")
public class BarbellService {
	
	private Barbell barbell;
	private PlateList plateList;
	
	@Autowired
	public BarbellService(@Qualifier("active") IWeightsRepository repository) {
		this.barbell = repository.readBarbell();
		this.plateList = repository.readPlates();
	}
	
	
	private List<Operation> CalcSteps(double targetWeight, Deque<Plate> barbellStack,  Map<Double, Integer> availablePlate) {
		List<Operation> opList = new ArrayList<>();
		double actualWeight = 0;
		
		if (Double.compare(actualWeight, targetWeight) == 0) {
			return List.of();
		}
		
		for (Map.Entry<Double, Integer> entry : availablePlate.entrySet()) {
			Double weight = entry.getKey();
		    Integer numPlates = entry.getValue();
		    
		    int cursor = 0;
		    int exit = 0;
		    while (Double.compare(actualWeight, targetWeight) <= 0 && cursor < numPlates && exit == 0) {
		    	
		    	actualWeight += weight;
			    if (actualWeight > targetWeight) {
			    	exit = 1;
			    	actualWeight -= weight;
			    }
			    else if (Double.compare(actualWeight, targetWeight) == 0) {
			    	Operation newOp = new Operation("PUSH", weight);
			    	opList.add(newOp);
			    	return opList;
			    }
			    Operation newOp = new Operation("PUSH", weight);
		    	opList.add(newOp);
		    }
		     
		}
		
		
		
		return opList;
	}
	
	public PlateList getCombination(double targetWeight) {
		PlateList combination = new PlateArrayList();
		
		List<Operation> idealOperation = CalcSteps(targetWeight, barbell.getLeft(), plateList.countPlatesByWeight());
		
		double differentWeight = ( targetWeight - barbell.getTotalWeight() ) / 2;
		double weight = 0;
		
		Iterator<Map.Entry<Double, Plates>> it = groupPlates.entrySet().iterator();
		while ( Double.compare(differentWeight, weight) != 0 && it.hasNext() ) {
			
			if (differentWeight < 0)
				
				
			it.next();
		}
			
		return combination;
	}
	
}
