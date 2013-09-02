/**
 * Abstract class for creating Chromosome objects to work with the genetic algorithms.
 * Extend this class into a class that controls a process to be optimised.  There are 
 * two methods to be implemented to ensure correct operation populateGenes and calculateFitness.
 * 
 */
package uk.ac.leeds.mass.optimisation.genetic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;
import sun.reflect.ReflectionFactory;
import uk.ac.leeds.mass.statistics.gof.IGOF;

/**
 *
 * @author kirkharland
 */


public abstract class Chromosome {
    
    private ArrayList<Gene> genes = new ArrayList<Gene>();
    private Double fitness = null;
    
    private GeneticAlgorithm parent = null;
    
    
    protected Chromosome(){}
    
    protected Chromosome(GeneticAlgorithm parent){
        this.parent = parent;
        
    }
    
    protected Chromosome(GeneticAlgorithm parent, ArrayList<Gene> genes){
        this.parent = parent;
        this.genes = genes;
    }
    
    /**
     * breeds the two chromosomes together creating a new chromosome with an 
     * internal gene list.  If the two chromosome have gene lists of different
     * lengths the returned chromosome will have a gene list somewhere between 
     * the two.
     * 
     * @param partner The Chromosome with which to breed this one
     * @return a new Chromosome object which is completely separate from the
     * two Chromosome objects used to breed it, the genes used in the new 
     * Chromosome are cloned to ensure no unexpected mutations happen.
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException 
     */
    Chromosome breed(Chromosome partner) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
        int min = genes.size();
        int max = genes.size();
        
        if( partner.getGenes().size() > min ){
            max = partner.getGenes().size();
        }else{
            min = partner.getGenes().size();
        }
        
        ArrayList<Gene> bredGenes = new ArrayList<Gene>();
        
        for (int i = 0; i < max; i++) {
            if ( i > min ){
                //randomly decide whether to add anymore genes if we 
                //over the min limit.
                if ( parent.getRandom().nextBoolean() ){break;}
            }
            
            //randomly decide which way round to add genes.
            //When the genes are added they are cloned to make sure 
            //any mutations in the genetic algorithm don't have 
            //undesired consequences.
            if (parent.getRandom().nextBoolean() ){
                //make sure we have more genes to add from this 
                //chromosome if not use the partner
                if (i<genes.size()){
                    bredGenes.add(genes.get(i).clone());
                }else{
                    bredGenes.add(partner.getGenes().get(i).clone());
                }
            }else{
                //make sure we have more genes to add from the 
                //partener if not add them from this chromosome
                if (i<partner.getGenes().size()){
                    bredGenes.add(partner.getGenes().get(i).clone());
                }else{
                    bredGenes.add(genes.get(i).clone());
                }
            }
            
        }
        
        Class[] parameterDefinition = {parent.getClass(),bredGenes.getClass()};
        Object[] parameters = {parent, bredGenes};
        Chromosome chromosome = instantiateNewChromosomeSubObject(parameterDefinition, parameters);
        return chromosome;
        
    }
    
    /**
     * method call made to calculate the fitness.  This is only 
     * calculated once so if changes are made to this object the
     * fitness cannot be recalculated.
     * 
     * @return a double value representing the fitness of this 
     * Chromosome using the IGOF object set in the parent GeneticAlgorithm
     * object.
     */
    double getFitness(){
        if ( fitness==null ){
            fitness = new Double(calculateFitness(parent.getGof()));
        }
        return fitness.doubleValue();
    }
    
    /**
     * Create a new Chromosome object from this class and return it after 
     * calling the abstract method to populate the genes.
     * 
     * @param parent The calling GeneticAlgorithm Object
     * @return A new Chromosome Object
     * 
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    protected Chromosome createNewChromosome(GeneticAlgorithm parent) throws NoSuchMethodException, 
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        
        Class[] parameterDefinition = {parent.getClass()};
        Object[] parameter = {parent};
        Chromosome chromosome = instantiateNewChromosomeSubObject(parameterDefinition, parameter);
        chromosome.populateGenes();
        return chromosome;

    }
    
    
    /**
     * Abstract method to be overridden to populate the genes as 
     * object is created
     */
    protected abstract void populateGenes();
    
    /**
     * Abstract method to be overridden by the implementing class
     * where any calculations should be done to calculate the
     * goodness of fit using the IGOF supplied by the parent 
     * Genetic Algorithm
     */
    public abstract double calculateFitness(IGOF gof);
    
    
    /**
     * If this Chromosome has a list of Genes, randomly picks a gene 
     * and mutates it.
     */
    public void mutate(){
        if (!genes.isEmpty()){
            genes.get( parent.getRandom().nextInt( genes.size() ) ).mutate();
        }
    }
    
    /**
     * Return the list of Gene objects that make up this Chromosome
     * @return An ArrayList of Gene objects
     */
    public ArrayList<Gene> getGenes(){
        return genes;
    }
    
    
    /**
     * Override of the Object classes toString method 
     * to ensure a sensible String is returned.
     * @return the String representation of this class
     */
    @Override
    public String toString(){
        String s = "Genes: ";
        for (Gene e : genes) {
            s+=e.toString()+" ";
        }
        s+= "Fitness: "+getFitness();
        return s.trim();
    }
    
    /**
     * Override of the Object classes equals method to 
     * ensure that comparisons of two Chromosome objects 
     * are truly meaningfully equal.
     * 
     * @param o the object to be compared against
     * @return returns true is the two Chromosomes are meaningfully equal.  This will 
     * always return false is the object passed in is not a Chromosome object
     */
    @Override
    public boolean equals(Object o){
        //set up a boolean to track if the objects are the same
        boolean same = true;
        //check if we a comparing a Chromosome object
        if ( o instanceof Chromosome ){
            //get the array of Genes from the object passed in
            ArrayList<Gene> genesToCompare = ((Chromosome)o).getGenes();
            //if the two sizes are different then set same value to false
            //these are not meaningfully the same!
            if ( genes.size() != genesToCompare.size() ){
                same = false;
            }else{
                //cycle through the gene objects in both objects 
                //comparing them
                for (int i = 0; i < genes.size(); i++) {
                    //if one gene is not the same set the same variable to 
                    //false and then break out of the for-loop, no point in
                    //continuing with the comparison.
                    if (!genes.get(i).equals(genesToCompare.get(i))){
                        same = false;
                        break;
                    }
                }
            }
        //If the object is not a Chromosome object set same to false
        }else{
            same = false;
        }
        
        //return whether the objects are the same or not
        return same;
    }
    
    /**
     * Method to create a inheriting class object using the super class 
     * constructor.
     * 
     * @param parameterDefinition a Class array of the class definitions required for the 
     * desired constructor in the declared , so that array[0] is the first parameter.
     * @param parameters an Object array of the parameters to be used to create the object 
     * in the order that they are required, so that array[0] is the first parameter.
     * 
     * @return a sub class object using the constructor from the Chromosome super class
     * 
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    private Chromosome instantiateNewChromosomeSubObject(Class[] parameterDefinition, Object[] parameters) throws 
            NoSuchMethodException, InstantiationException, IllegalAccessException, 
            IllegalArgumentException, InvocationTargetException{
        
        //get the Class of this sub class
        Class subClass = this.getClass();
        //get the Chromosome super class where the required constructor is
        Class chromosomeClass = Chromosome.class;
        
        //Get the Constructor object for the required constructor definition from
        //the Chromosome super Class object using the parameter definitions
        Constructor superConstructor = chromosomeClass.getDeclaredConstructor(parameterDefinition);
        
        //Use the ReflectionFactory to create the same constructor definition in 
        //the sub Class.
        Constructor intConstr = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(subClass, superConstructor);

        //Use the new constructor to create a new instance using the parameters passed in
        Object o = subClass.cast(intConstr.newInstance(parameters));
        
        //check that the object created is an instance of Chromosome
        if (o instanceof Chromosome){
            //if it is (and it should always be) cast the object and return it
            return (Chromosome) o;
        }else{
            //otherwise throw and InstantiationException!
            throw new InstantiationException("Cast failed.");
        }
        
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.genes);
        return hash;
    }
    
    
}
