/**
 * Implement this interface and attach you object to the GeneticAlgorithm object
 * to listen for errors during the optimisation process.
 */
package uk.ac.leeds.mass.optimisation.genetic;

/**
 *
 * @author kirkharland
 */
public interface IErrorListener {
    public void catchError(Exception e);
}
