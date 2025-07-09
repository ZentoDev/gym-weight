package com.gym.weight.repository;

import java.util.List;

import com.gym.weight.model.Barbell;
import com.gym.weight.model.Plate;
import com.gym.weight.model.PlateList;

public interface IWeightsRepository {
	
	public Barbell readBarbell();
	public List<Plate> readPlates();
}
