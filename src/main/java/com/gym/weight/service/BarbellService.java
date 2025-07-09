package com.gym.weight.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.gym.weight.model.Barbell;
import com.gym.weight.model.Operation;
import com.gym.weight.model.Plate;
import com.gym.weight.model.PlatesMap;
import com.gym.weight.repository.IWeightsRepository;

@Service("activeService")
public class BarbellService {
	
	private Barbell barbell;
	private List<Plate> plateList;
	
	
	public BarbellService(@Qualifier("activeRepository") IWeightsRepository repository) {
		
	    try {
	    	
	        this.barbell = repository.readBarbell();
	        if (this.barbell == null) {
	            System.err.println("Warning: barbell is null!");
	            this.barbell = new Barbell();
	        }	        
	        this.plateList = repository.readPlates();
	        if (this.plateList == null) {
	            System.err.println("Warning: plateList is null!");
	            this.plateList = new ArrayList<>();
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	}
		
	public List<Operation> getCombination(double targetWeight) {
		
		PlatesMap Plates = new PlatesMap(plateList);
		List<Operation> operation = new ArrayList<>();
		
		operation = Schedule(targetWeight, Plates, barbell);
		operation.forEach(op -> System.out.println(op.getAction() + " " + op.getWeight()));
					
		return operation;
	}
	
	private List<Operation> Schedule(double targetWeight,  PlatesMap availablePlates, Barbell barbell) {
		
		List<Operation> operation = new ArrayList<>();
		
		// ottengo il peso per un singolo lato del bilanciere
		
		double targetWeightBranch = (targetWeight - barbell.getWeightBarbell()) / 2;
		System.out.println("weightBrach: "+targetWeightBranch+" = " + targetWeight +" - "+barbell.getWeightBarbell() + " / 2");
		
		operation = CalculateSteps(targetWeightBranch, PlatesMap.halfPlates(availablePlates), barbell.getLeft());
		operation.forEach(op -> System.out.println(op.getAction() + " " + op.getWeight()));
		applyOperationsToBarbellStack(barbell.getLeft(), operation, availablePlates);
		applyOperationsToBarbellStack(barbell.getRight(), operation, availablePlates);
		
		return operation;
	}

	
	private List<Operation> CalculateSteps(double targetWeight,  PlatesMap availablePlates, Deque<Plate> barbellStack) {
		
		List<Operation> bestSolution = new ArrayList<>();
		List<Operation> solution = new ArrayList<>();
		List<Operation> pullOp = new ArrayList<>();
		List<Double> sortedWeights = new ArrayList<>(availablePlates.keySet());
		
		sortedWeights.sort(Comparator.reverseOrder()); // Dischi grandi prima
		double minWeight = sortedWeights.getLast();
		
		Deque<Plate> barbellStackCPY = new ArrayDeque<>(barbellStack); // copia per non modificare l'originale
		
		for (int currentStack = barbellStack.size(); currentStack >= 0; currentStack--) {
			
			double barbellweight = calculateTotalWeight(barbellStackCPY);
			double diffWeight = barbellweight - targetWeight;
			
			System.out.println(barbellweight + " - " + targetWeight);
			System.out.println("for " + currentStack);
			System.out.println("Double.compare(" + Math.abs(diffWeight) + ", " + minWeight + ") < 0");
			
			/* se la differenza di peso è nell'intorno di un valore minimo (solitamente il disco più piccolo) 
			 * considero la soluzione accettabile   */
		    if (Double.compare(Math.abs(diffWeight), minWeight) < 0) {
		    	System.out.println("Sol Double.compare(" + Math.abs(diffWeight) + ", " + minWeight + ") < 0");
		    	
		    	// bilanciere già caricato correttamente
		    	if (bestSolution.isEmpty() && solution.isEmpty()) {
		    		System.out.println("bilanciere già caricato correttamente");
		    		return bestSolution;
		    	}
		    	
		    	// valuto se è miglior soluzione
		    	else if (bestSolution.isEmpty() || solution.size() < bestSolution.size()) {
		    		bestSolution.clear();
					bestSolution.addAll(new ArrayList<>(solution));
		    	}
		    }
		    
		    // Se il numero di PULL supera la soluzione ideale è inutile continuare
		    if (!bestSolution.isEmpty() && bestSolution.size() <= (barbellStack.size() - currentStack)) {
		    	return bestSolution;
		    }
		    
		    // inizializzo la potenziale soluzione con i PULL effettuati
		    solution = new ArrayList<>(pullOp);
		    
		    // entro se il peso è inferiore a quello richiesto (eventuali PUSH)
		    if(Double.compare(diffWeight, -minWeight) <= 0) {
			    System.out.println("PUSH Double.compare(" + diffWeight + ", " + -minWeight + ") < 0");
		    	List<Operation> steps = new ArrayList<>(findMinStepsToTarget(-diffWeight, minWeight, new TreeMap<>(availablePlates.toCountMap())));
		    	// se vuoto non è stata trovata soluzione
		    	if (!steps.isEmpty()) {
		    		System.out.println("steps");
		    		solution.addAll(steps);
		    		applyOperationsToBarbellStack(barbellStackCPY, steps, availablePlates);
		    		barbellStackCPY.forEach(Plate -> System.out.println(Plate.getWeight()));
		    		continue;
		    	}
		    }
		    /* se il bilaciere è vuoto ma il peso è maggiore ma il bilanciere è troppo alto (barbell.weight) non è possibile ottenere il target*/
			if (barbellStackCPY.isEmpty()) {
				System.out.println("barbellEmpty");
				return Collections.emptyList();
			}
			
			// diffWeight > minWeight
			double w = barbellStackCPY.getLast().getWeight();
			System.out.println("call");
			availablePlates.addPlate(w, barbellStackCPY.removeLast());
			pullOp.add(new Operation("PULL", w));
			solution = new ArrayList<>(pullOp);
			
			continue;
		}
		if (bestSolution.isEmpty() && !solution.isEmpty()) {
			// caso in cui vi è un PUSH con bilanciere scarico
			return solution;
		}
		return bestSolution;
	}
	
	
	private List<Operation> findMinStepsToTarget(double targetWeight, double minWeight, Map<Double, Integer> availablePlates) {

		List<Operation> bestSolution = new ArrayList<>();
		List<Double> sortedWeights = new ArrayList<>(availablePlates.keySet());
		sortedWeights.sort(Comparator.reverseOrder()); // Dischi grandi prima

		findBestCombination(
				targetWeight,
				minWeight,
				0,
				new ArrayList<>(),
				new HashMap<>(availablePlates), // copia per non modificare l'originale
				sortedWeights,
				bestSolution
				);

		return bestSolution;
	}
	
	/* metodo ricorsivo 
	 * da chiamare quando aggiungere dei pesi 
	 * trova la combinazione di operazioni di PUSH ideale*/
	private void findBestCombination(
			double target,
			double tollerance,
			double currentSum,
			List<Operation> currentOps,
			Map<Double, Integer> available,
			List<Double> weights,
			List<Operation> bestSolution
			) {
		
		System.out.println("currentSum " + currentSum + "target" + target);
		// se il peso è diventato eccessivo -> esco
		if (Double.compare(currentSum - target, tollerance) > 0) return;
		System.out.println("bestComb2");
		// raggiunto il target
		if (Double.compare(Math.abs(currentSum - target), tollerance) < 0) {
			// Se è la prima soluzione o è migliore di quella precedente, salvala
			if (bestSolution.isEmpty() || currentOps.size() < bestSolution.size()) {
				System.out.println("newBest");
				bestSolution.clear();
				bestSolution.addAll(new ArrayList<>(currentOps));
				
			}
			return;
		}

	    // Calcolo euristica: stima min numero di dischi mancanti
	    double remaining = target - currentSum;
	    int minRemainingOps = estimateMaxOps(remaining, tollerance, available);
	    System.out.println("max stima " + minRemainingOps);
	    
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
				findBestCombination(target, tollerance, currentSum + w, currentOps, available, weights, bestSolution);
			}
			// Backtracking
			available.put(w, count);
			currentOps.remove(currentOps.size() - 1);
		}
	}

	private int estimateMaxOps(double remaining, double tollerance, Map<Double, Integer> available) {
		int ops = 0;
		List<Double> sorted = new ArrayList<>(available.keySet());
		sorted.sort(Collections.reverseOrder()); // Dischi grandi prima

		for (double w : sorted) {
			int count = available.get(w);
			while (count > 0 && Double.compare(remaining, w - 1e-6) >= tollerance) {
				remaining -= w;
				count--;
				ops++;
			}
			// raggiunto peso target
			if (Double.compare(remaining, tollerance + 1e-6) < 0) break;
		}
				
		return (Double.compare(remaining, tollerance + 1e-6) < 0) ? ops : Integer.MAX_VALUE; // Se impossibile, scarta ramo
	}
	

	private void applyOperationsToBarbellStack(Deque<Plate> barbellStack, List<Operation> operations, PlatesMap availablePlatesByWeight) {
		for (Operation op : operations) {
			double w = op.getWeight();

			switch (op.getAction()) {
			case "PUSH":
				Plate plate = availablePlatesByWeight.removePlate(w);
				if (plate == null) {
					throw new IllegalStateException("Nessun disco disponibile da " + w + "kg");
				}
				barbellStack.addLast(plate);
				break;

			case "PULL":
				if (barbellStack.isEmpty()) {
					throw new IllegalStateException("Stack vuoto, impossibile fare PULL");
				}
				Plate removed = barbellStack.removeLast();
				if (Double.compare(removed.getWeight(), w) != 0)
					throw new IllegalStateException("Mismatch PULL: atteso " + w + ", trovato " + removed.getWeight());

				availablePlatesByWeight.addPlate(w, removed);
				break;

			default:
				throw new IllegalArgumentException("Operazione sconosciuta: " + op.getAction());
			}
		}
	}
		
	private double calculateTotalWeight(Deque<Plate> stack) {
	    return stack.stream().mapToDouble(Plate::getWeight).sum();
	}

	public List<Double> getSideWeights(String side) {
	    Deque<Plate> stack = barbell.getLeft();

	    if (stack == null || stack.isEmpty()) {
	        return Collections.emptyList();
	    }

	    return stack.stream()
	                .map(Plate::getWeight)
	                .collect(Collectors.toList());
	}

}
