package com.gym.weight.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gym.weight.model.Operation;
import com.gym.weight.service.BarbellService;

@RestController
public class BarbellController {
	
	@Autowired
	@Qualifier("activeService")
	private BarbellService barbellService;
	
	public BarbellController() {
		
	}
		
    @GetMapping("/combination")
    public ResponseEntity<List<Operation>> getCombination(@RequestParam double targetWeight) {
    	List<Operation> result = barbellService.getCombination(targetWeight);
    	result.forEach(op -> System.out.println(op.getAction() + " " + op.getWeight()));
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/side")
    public List<Double> getSideWeights(@RequestParam String side) {
        return barbellService.getSideWeights(side); // es. ["left"] o ["right"]
    }
}
