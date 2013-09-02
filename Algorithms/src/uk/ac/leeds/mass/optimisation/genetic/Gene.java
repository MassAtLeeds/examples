/*
 * Abstract class for the genes in a chromosone in the genetic algorithm
 */
package uk.ac.leeds.mass.optimisation.genetic;

/**
 *
 * @author kirkharland
 */
public abstract class Gene {
    /**
     * Method to mutate the particular Gene
     */
    public abstract void mutate();
    
    /**
     * Override of the toString method to return 
     * a sensible value.
     * @return A human interpretable string value
     */
    @Override
    public abstract String toString();
    
    /**
     * Override of the equals method to ensure that 
     * equivalence is assessed correctly.
     * @param o the object to be compared against
     * @return true if the two objects are the same otherwise false
     */
    @Override
    public abstract boolean equals(Object o);
    
    /**
     * Create a clone of this object that holds no reference to this 
     * instance.
     * @return An independent exact replica of this Gene object
     */
    @Override
    public abstract Gene clone();

    /**
     * Create a hashcode value for this object
     * @return the hash code for this object
     */
    @Override
    public abstract int hashCode();
}
