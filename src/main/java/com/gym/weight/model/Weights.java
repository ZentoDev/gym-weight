package com.gym.weight.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "weights")
public class Weights {
    private Barbell barbell;
    private PlateList plates;
    
    @XmlElement
    public Barbell getBarbell() {
        return barbell;
    }

    public void setBarbell(Barbell barbell) {
        this.barbell = barbell;
    }

    @XmlElement(name = "plate")
    public PlateList getPlates() {
        return plates;
    }

    public void setPlates(PlateList plates) {
        this.plates = plates;
    }
}
