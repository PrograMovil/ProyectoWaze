package PG;

import Model.Ruta;
import java.util.ArrayList;
import java.util.List;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.SwappingMutationOperator;

public class Genetica {

    private Configuration conf;
    private SwappingMutationOperator swapper;
    private Fitness fitnessFunction = null;
    private List<Ruta> rutas = new ArrayList();
    private static final int MAX_EVOLUCIONES_PERMITIDAS = 1500;
    private Chromosome rutasChromosome = null;

    public Genetica(ArrayList<Ruta> rutas) {
        try {
            this.rutas = rutas;
            conf = new DefaultConfiguration();
            Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
            conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
            conf.getGeneticOperators().clear();

            swapper = new SwappingMutationOperator(conf);
            conf.addGeneticOperator(swapper);
            conf.setPreservFittestIndividual(true);
            conf.setPopulationSize(1000);
            conf.setKeepPopulationSizeConstant(false);

            fitnessFunction = new Fitness(this.rutas);
            conf.setFitnessFunction(fitnessFunction);

            Gene[] genesRuta = new Gene[4];

            genesRuta[0] = new IntegerGene(conf, 0, this.rutas.size() - 1);
            genesRuta[1] = new IntegerGene(conf, 0, this.rutas.size() - 1);
            genesRuta[2] = new IntegerGene(conf, 0, this.rutas.size() - 1);
            genesRuta[3] = new IntegerGene(conf, 0, this.rutas.size() - 1);
            rutasChromosome = new Chromosome(conf, genesRuta);
            genesRuta[0].setAllele(new Integer(0));
            genesRuta[1].setAllele(new Integer(1));
            genesRuta[2].setAllele(new Integer(3));
            genesRuta[3].setAllele(new Integer(4));

            conf.setSampleChromosome(rutasChromosome);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public Ruta calcularMejorRuta(ArrayList<Ruta> rutas) throws Exception {

        this.rutas = rutas;

        Genotype population = Genotype.randomInitialGenotype(conf);

        IChromosome bestSolutionSoFar = rutasChromosome;

        for (int i = 0; i < MAX_EVOLUCIONES_PERMITIDAS; i++) {
            population.evolve();
            IChromosome candidateBestSolution = population.getFittestChromosome();
            if (candidateBestSolution.getFitnessValue() > bestSolutionSoFar.getFitnessValue()) {
                bestSolutionSoFar = candidateBestSolution;
            }
        }
        printSolution(bestSolutionSoFar, this.rutas);
        return rutas.get(0);
    }
    
    public void printSolution(IChromosome solution, List<Ruta> rutas)
    {
        System.out.println("#################################################################################################################");
        System.out.println("Fitness value: " + solution.getFitnessValue());
 
            for (int i = 0; i < solution.size(); i++) {
                int index = (Integer) solution.getGene(i).getAllele();
                  Ruta ruta = rutas.get(index);
                  System.out.println(ruta.toString());
                }
        System.out.println("#################################################################################################################");
    }

}
