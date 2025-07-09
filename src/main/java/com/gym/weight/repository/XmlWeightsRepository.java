package com.gym.weight.repository;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gym.weight.model.Barbell;
import com.gym.weight.model.Plate;
import com.gym.weight.model.PlateArrayList;
import com.gym.weight.model.PlateList;
import com.gym.weight.model.Weights;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

@Repository("activeRepository")
public class XmlWeightsRepository implements IWeightsRepository{
	
	private Weights weights;
	InputStream input = getClass().getClassLoader().getResourceAsStream("plates.xml");
	
	public XmlWeightsRepository() {
		try {
			// Crea un contesto JAXB per la classe Weights (cioè l'oggetto radice del tuo XML)
			JAXBContext context = JAXBContext.newInstance(Weights.class);
			// Crea un oggetto Unmarshaller, cioè il "lettore" che trasforma XML in oggetti Java
			Unmarshaller unmarshaller = context.createUnmarshaller();
			// Legge il file XML e lo converte in un oggetto Weights
			weights = (Weights) unmarshaller.unmarshal(input);
			
			// Accede ai dati dentro l'oggetto Weights creato da XML
            System.out.println("Peso bilanciere: " + weights.getBarbell().getWeightBarbell());

            // Scorre la lista di piatti nel file XML e stampa peso e materiale di ognuno
            for (Plate plate : weights.getPlates()) {
                System.out.println("Plate - Peso: " + plate.getWeight() + ", Materiale: " + plate.getMaterial());
            }   
		} catch (Exception e) {
		    // Se c'è un errore durante la lettura/parsing del file XML, stampa lo stack trace
		    e.printStackTrace();
		}
	}
	
	@Override
	public Barbell readBarbell() {
		return weights.getBarbell();
	}
	@Override
	public List<Plate> readPlates() {
		return weights.getPlates();
	}
	
}
