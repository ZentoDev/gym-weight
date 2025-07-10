package com.gym.weight.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "weights")
public class Weights {
    private Barbell barbell;
    private PlateList plates;
    
    public Weights() {
    	this.barbell = new Barbell();
    	this.plates = new PlateArrayList();
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
    public PlateList getPlates() {
        return plates;
    }

    public void setPlates(PlateList plates) {
        this.plates = plates;
    }
}
