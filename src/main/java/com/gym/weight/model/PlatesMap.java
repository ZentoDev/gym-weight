package com.gym.weight.model;

import java.util.Map.Entry;
import java.util.Map;
import java.util.TreeMap;

public class PlatesMap extends TreeMap<Double, PlateList>{

	// indicates the class version
	private static final long serialVersionUID = 1L;
	
	public PlatesMap() {
		super();
	}
	
	public PlatesMap(PlateList plates) {
	    for (Plate plate : plates) {
	        this.addPlate(plate.getWeight(), plate);
	    }
	}
	
	public static PlatesMap loadPlateList(PlateList plates) {
	    PlatesMap map = new PlatesMap();
	    for (Plate plate : plates) {
	        double weight = plate.getWeight();
	        map.addPlate(weight, plate);  // usa il tuo metodo che fa computeIfAbsent
	    }
	    return map;
	}

	public void addPlate(double weight, Plate plate) {
	    this.computeIfAbsent(weight, k -> new PlateArrayList()).add(plate);
	}
	
	public Plate removePlate(double weight) {
	    PlateList list = get(weight);
	    if (list != null && !list.isEmpty()) {
	        Plate removed = list.removeLast(); // o remove(0) se FIFO
	        if (list.isEmpty()) {
	            remove(weight); // pulizia
	        }
	        return removed;
	    }
	    
	    return null;
	}
	
			
	public int totalPlates() {
		return values().stream().mapToInt(PlateList::size).sum();
	}
	
    public boolean hasPlate(double weight) {
        PlateList list = get(weight);
        return list != null && !list.isEmpty();
    }
    
    public Map<Double, Integer> toCountMap() {
        Map<Double, Integer> plateCounts = new TreeMap<>();

        for (Entry<Double, PlateList> entry : this.entrySet()) {
            plateCounts.put(entry.getKey(), entry.getValue().size());
        }
        
        return plateCounts;
    }
    
    // restituisce una copia dell'oggetto con la metà dei piatti
    public static PlatesMap halfPlates(PlatesMap fullMap) {
        PlatesMap halfMap = new PlatesMap();

        for (Map.Entry<Double, PlateList> entry : fullMap.entrySet()) {
            double weight = entry.getKey();
            PlateList plates = entry.getValue();
            int halfSize = plates.size() / 2;

            for (int i = 0; i < halfSize; i++) {
                halfMap.addPlate(weight, plates.get(i));
            }
        }

        return halfMap;
    }
}
