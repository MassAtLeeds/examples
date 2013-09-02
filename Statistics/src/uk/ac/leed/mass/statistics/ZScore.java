/*
 * ZScore.java
 */
package uk.ac.leed.mass.statistics;

/**
 *
 * @author kirkharland
 */
public class ZScore {
    
    public static double calcZScore(double T, double P, double N){

        double pij = P / N;
        double tij = T / N;
        double numerator = 0.0;

        //if pij = 0 then the pij value in the denominator is altered to be a
        //suitably small value 1/N.
        if ( pij == 0 ){
            pij = 1 / N;
            return tij / ( Math.pow( ((pij*(1-pij)) / N) , 0.5) );

        //if tij - pij > 0 (is a positive number)
        }else if (tij-pij > 0){
            numerator = (tij - pij) - (1 / (2*N));

        //if tij - pij < 0 (is a negative number)
        }else if (tij-pij < 0){
            numerator = (tij - pij) + (1 / (2*N));

        //catch everything else (should be where tij - pij = 0) and return 0
        }else{
            return 0;
        }

        return numerator / ( Math.pow( ( (pij*(1-pij)) / N ) , 0.5) );

    }

    
}
