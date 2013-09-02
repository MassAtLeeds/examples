/**
 * Genetic Algorithm controls the optimisation process.  This is the class to be 
 * invoked to run the optimisation process.  This class implements the runnable 
 * interface an can be run as a separate thread.
 * 
 * Parameters for the algorithm can be set through the public get and set methods
 */
package uk.ac.leeds.mass.optimisation.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.mass.statistics.gof.IGOF;
import uk.ac.leeds.mass.statistics.gof.SRMSE;

/**
 *
 * @author kirkharland
 */

public class GeneticAlgorithm implements Runnable{
    
    private Random random = null;
    
    private int generations = 200;
    private int breedPopulationSize = 25;
    private int candidatePopulationSize = 250;
    private double mutationRate = 0.25;
    
    //fitness calculation
    private IGOF gof = new SRMSE();
    
    private List<Chromosome> candidatePopulation = new ArrayList<Chromosome>();
    private List<Chromosome> breedPopulation = new ArrayList<Chromosome>();
    private List<IErrorListener> errorListeners = new ArrayList<IErrorListener>();
    private Chromosome chromosome = null;
    
    //private constructor so that nobody calls it!
    private GeneticAlgorithm(){}
    
    /**
     * Creates a new GeneticAlgorithm with random 
     * generation, selection, breeding and mutation.
     * 
     * @param A valid object which extends the abstract Chromosome class 
     */
    public GeneticAlgorithm(Chromosome chromosome){
        random = new Random();
        this.chromosome = chromosome;
    }
    
    /**
     * Creates a GeneticAlgorithm using a set seed to 
     * allow the algorithm to be reproduced by reusing the 
     * same seed.
     * 
     * @param seed valid long value to be used as the seed for the 
     * random number generator.
     * @param A valid object which extends the abstract Chromosome class
     */
    public GeneticAlgorithm(Chromosome chromosome, long seed){
        this(chromosome);
        random = new Random(seed);
    }
    
    
    /**
     * Runs the genetic algorithm.  Errors are reported through the IErrorListeners
     * if they are present else through the default logging stream.
     */
    
    @Override
    public void run(){
        
        //create the initial candidate population
        //if there is an error report it and end.
        for (int i = 0; i < candidatePopulationSize; i++) {
            try{
                candidatePopulation.add(chromosome.createNewChromosome(this));
            }catch(Exception e){
                this.reportError(e);
                return;
            }
        }
        
        //cycle through the generations
        //the + 1 is to allow for the final cycle to store the best
        //candidates into the breed population
        for (int i = 0; i < getGenerations() + 1; i++) {
                    
            //test the finess of the candidate population
            for (Chromosome c : candidatePopulation) {
                c.getFitness();
            }
            
            //store best performing chromosomes in the breed population
            for (Chromosome c : getBreedPopulation()) {
                candidatePopulation.add(c);
            }
            
            //order the candidate population and select the new breed population
            if (gof.calibrateToLessThan()){
                Collections.sort(candidatePopulation, new SortOrder(true));
            }else{
                Collections.sort(candidatePopulation, new SortOrder(false));
            }
            
            //clear the breed population
            getBreedPopulation().clear();
            int counter = 0;
            //Add the best performing chromosomes from the candidate population
            for (int j = 0; j < candidatePopulation.size(); j++) {
                if (!breedPopulation.contains(candidatePopulation.get(j))){
                    breedPopulation.add(candidatePopulation.get(j));
                    counter++;
                }
                
                //if we have added all of the breed population then exit the loop
                if (counter == breedPopulationSize){break;}
                
            }
        
            //if not the last generation
            if( i < (getGenerations()) ){
                //breed new candidate population
                candidatePopulation.clear();
                int breedPopulationCounter = 0;
                for (int j = 0; j < candidatePopulationSize; j++) {
                    //select other random chromosome to breed with
                    int breedIndex = random.nextInt(breedPopulationSize);
                    //if we have selected the same chromosome to breed with
                    //shunt the index out by 1.
                    if ( breedIndex == breedPopulationCounter ){
                        
                        //are we at the top?
                        if ( breedIndex == breedPopulationSize-1 ){
                            breedIndex--;
                        //are we at the bottom?
                        }else if ( breedIndex == 0 ){
                            breedIndex++;
                        }else{
                            breedIndex = random.nextBoolean() ? breedIndex++ : breedIndex--;
                        }
                    }
                    
                    Chromosome newChromosome = null;
                    try{
                        newChromosome = getBreedPopulation().get(breedPopulationCounter).breed(getBreedPopulation().get(breedIndex));
                    }catch(Exception e){
                        reportError(e);
                        return;
                    }
                    //mutate random members of the candidate population
                    if ( random.nextDouble() < mutationRate ){
                        newChromosome.mutate();
                    }
                    
                    candidatePopulation.add(newChromosome);
                    
                    breedPopulationCounter++;
                    if (breedPopulationCounter == breedPopulationSize){breedPopulationCounter = 0;}
                }
        
                
            }        
        
        }
        
        
    }

    /**
     * @return the random
     */
    public Random getRandom() {
        return random;
    }

    /**
     * @return the generations
     */
    public int getGenerations() {
        return generations;
    }

    /**
     * @param generations the generations to set
     */
    public void setGenerations(int generations) {
        this.generations = generations;
    }

    /**
     * @return the breedPopulationSize
     */
    public int getBreedPopulationSize() {
        return breedPopulationSize;
    }

    /**
     * @param breedPopulationSize the breedPopulationSize to set
     */
    public void setBreedPopulationSize(int breedPopulationSize) {
        this.breedPopulationSize = breedPopulationSize;
    }

    /**
     * @return the candidatePopulationSize
     */
    public int getCandidatePopulationSize() {
        return candidatePopulationSize;
    }

    /**
     * @param candidatePopulationSize the candidatePopulationSize to set
     */
    public void setCandidatePopulationSize(int candidatePopulationSize) {
        this.candidatePopulationSize = candidatePopulationSize;
    }

    /**
     * @return the mutationRate
     */
    public double getMutationRate() {
        return mutationRate;
    }

    /**
     * @param mutationRate the mutationRate to set
     */
    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    /**
     * @return the gof
     */
    public IGOF getGof() {
        return gof;
    }

    /**
     * @param gof the gof to set
     */
    public void setGof(IGOF gof) {
        this.gof = gof;
    }
    
    private void reportError(Exception e){
        if ( errorListeners.isEmpty() ){
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }else{
            for (IErrorListener listener : errorListeners) {
                listener.catchError(e);
            }
        }
    }
    
    public void addErrorListener(IErrorListener listener){
        this.errorListeners.add(listener);
    }
    
    public void removeErrorListener(IErrorListener listener){
        this.errorListeners.remove(listener);
    }
    
    public void clearErrorListeners(){
        this.errorListeners.clear();
    }

    /**
     * @return the breedPopulation
     */
    public List<Chromosome> getBreedPopulation() {
        return breedPopulation;
    }
	
}
