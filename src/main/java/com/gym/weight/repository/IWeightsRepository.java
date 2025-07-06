package com.gym.weight.repository;

import com.gym.weight.model.Barbell;
import com.gym.weight.model.PlateList;

public interface IWeightsRepository {
	
	public Barbell readBarbell();
	public PlateList readPlates();
}
