package com.gym.weight.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "weights")
public class Weights {
    private Barbell barbell;
    private List<Plate> plates;
    
    public Weights() {
    	this.barbell = new Barbell();
    	this.plates = new ArrayList<Plate>();
    }
    
    @XmlElement
    public Barbell getBarbell() {
        return barbell;
    }

    public void setBarbell(Barbell barbell) {
        this.barbell = barbell;
    }
    
    @XmlElementWrapper(name="plates")
    @XmlElement(name = "plate")
    public List<Plate> getPlates() {
        return plates;
    }

    public void setPlates(List<Plate> plates) {
        this.plates = plates;
    }
}
