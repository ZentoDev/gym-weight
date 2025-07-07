package com.gym.weight.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
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
	
	
	private double calculateTotalWeight(Deque<Plate> stack) {
	    return stack.stream().mapToDouble(Plate::getWeight).sum();
	}

	private List<Operation> findMinStepsToTarget(double targetWeight, Map<Double, Integer> availablePlates) {

		List<Operation> bestSolution = new ArrayList<>();
		List<Double> sortedWeights = new ArrayList<>(availablePlates.keySet());
		sortedWeights.sort(Comparator.reverseOrder()); // Dischi grandi prima

		findBestCombination(
				targetWeight,
				0,
				new ArrayList<>(),
				new HashMap<>(availablePlates), // copia per non modificare l'originale
				sortedWeights,
				bestSolution
				);

		return bestSolution;
	}

	private void findBestCombination(
			double target,
			double currentSum,
			List<Operation> currentOps,
			Map<Double, Integer> available,
			List<Double> weights,
			List<Operation> bestSolution
			) {

		if (Double.compare(currentSum, target) > 0) return;
		
		if (Double.compare(currentSum, target) == 0) {
			// Se è la prima soluzione o migliore di quella precedente, salvala
			if (bestSolution.isEmpty() || currentOps.size() < bestSolution.size()) {
				bestSolution.clear();
				bestSolution.addAll(new ArrayList<>(currentOps));
				
			}
			return;
		}

	    // Calcolo euristica: stima min numero di dischi mancanti
	    double remaining = target - currentSum;
	    int minRemainingOps = estimateMinOps(remaining, available);
	    if (!bestSolution.isEmpty() && currentOps.size() + minRemainingOps >= bestSolution.size()) {
	        return; // potatura: anche il miglior caso sarebbe peggiore
	    }
	    
		for (Double w : weights) {
			int count = available.getOrDefault(w, 0); // controlla se è 0, con getOrDefault() gestiamo casi particolari (null unboxing) 
			if (count == 0) continue; // se non ci sono dischi di quel peso, salta a quello successivo

			// Prova ad aggiungere questo disco
			available.put(w, count - 1);
			currentOps.add(new Operation("PUSH", w));
			
			// ha senso continuare solo se non è peggiore o uguale alla miglior soluzione
			if (bestSolution.isEmpty() || currentOps.size() < bestSolution.size()) { 
				findBestCombination(target, currentSum + w, currentOps, available, weights, bestSolution);
			}
			// Backtracking
			available.put(w, count);
			currentOps.remove(currentOps.size() - 1);
		}
	}

	private int estimateMinOps(double remaining, Map<Double, Integer> available) {
		int ops = 0;
		List<Double> sorted = new ArrayList<>(available.keySet());
		sorted.sort(Collections.reverseOrder()); // Dischi grandi prima

		for (double w : sorted) {
			int count = available.get(w);
			while (count > 0 && Double.compare(remaining, w - 1e-6) >= 0) {
				remaining -= w;
				count--;
				ops++;
			}
			if (Double.compare(remaining, 1e-6) < 0) break;
		}

		return (Double.compare(remaining, 1e-6) < 0) ? ops : Integer.MAX_VALUE; // Se impossibile, scarta ramo
	}



	public PlateList getCombination(double targetWeight) {
		PlateList combination = new PlateArrayList();
		
		List<Operation> idealOperation = findMinStepsToTarget(targetWeight, plateList.countPlatesByWeight());
		
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
