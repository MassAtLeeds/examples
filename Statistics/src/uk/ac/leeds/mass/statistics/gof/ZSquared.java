/*
 * ZSquared.java
 */

package uk.ac.leeds.mass.statistics.gof;

import java.io.Serializable;
import uk.ac.leed.mass.statistics.ZScore;

/**
 * tij = observed proportion in a cell (cell count / total count)
 * pij = expected or projected proportion in a cell (cell count / total count)
 * N = total count
 * for the exact reasoning behind these statistics see Voas and Williamson 2001
 * Geographical and Environmental Modelling, Vol. 5, No. 2, 177-200
 * @author Kirk Harland
 */
public class ZSquared implements Serializable, IGOF{

    @Override
    public String fieldName(){return "Z sq";}
    @Override
    public String testName(){return "<html>Z<sup>2</sup></html>";}
    @Override
    public boolean calibrateToLessThan(){return true;}

    @Override
    public double test(double[][] calib, double[][] test, double N){
        double Z = 0.0;

        for (int i=0; i<calib.length; i++) {
            for (int j=0; j<calib[i].length; j++) {
                double z = ZScore.calcZScore( test[i][j], calib[i][j], N);
                Z += (z*z);
            }
        }

        if ( Double.isNaN(Z) ){
            return 0.0;
        }

        return Z;
    }

    @Override
    public double test(double[][] calib, double[][] test){

        double N=0.0;

        // Calculate total count
        for (int i=0; i<calib.length; i++) {
            for (int j=0; j<calib[i].length; j++) {
                N += calib[i][j];
            }
        }

        return test(calib, test, N);

    }

    @Override
    public boolean isPerfect(double testStat){

        //this method needs to be completed
        //should return true if the testStat represents a perfect fit
        return false;
    }

    
}