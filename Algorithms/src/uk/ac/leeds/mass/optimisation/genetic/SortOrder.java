/**
 * Comparator to make sure that the sort order of the collections is in the 
 * correct direction.
 *
 */
package uk.ac.leeds.mass.optimisation.genetic;


import java.util.Comparator;

/**
 *
 * @author kirkharland
 */
public class SortOrder implements Comparator<Chromosome>{

    private boolean ascending = true;
    
    private SortOrder(){}
    
    public SortOrder(boolean ascending){
        this.ascending = ascending;
    }
    
    @Override
    public int compare(Chromosome o1, Chromosome o2) {
        double val = 0.0;
        if (this.ascending){
            val = o1.getFitness() - o2.getFitness();
        }else{
            val = o2.getFitness() - o1.getFitness();
        }
        if(val > 0){
            return 1;
        }else if(val < 0){
            return -1;
        }else{
            return 0;
        }

    }
    
}
