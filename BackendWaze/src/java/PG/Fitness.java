
package PG;

import Model.Distancia;
import Model.LatLng;
import Model.Ruta;
import java.util.ArrayList;
import java.util.List;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
import org.jgap.impl.IntegerGene;


public class Fitness extends FitnessFunction{
    
    private List rutas = new ArrayList();

    public Fitness(List rutas) {
        this.rutas=rutas;
    }
    
    @Override
    protected double evaluate(IChromosome chromosome) {
        double score=0;
        List dups = new ArrayList();
        int badSolution = 1;
        
        for (int i = 0; i < chromosome.size(); i++) {
            IntegerGene agene =  (IntegerGene)chromosome.getGene(i);
            int index = (Integer) chromosome.getGene(i).getAllele();
            
            if (dups.contains(index))
             {
              badSolution = 0;
             }
            else{
              dups.add(index);
            }
            
            Ruta ruta=(Ruta) rutas.get(index);
            score=score+ruta.getDistance().value;
        }
        return (score*badSolution);
    }
    

    
}
