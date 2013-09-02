/*
 * TotalError.java
 */

package uk.ac.leeds.mass.statistics.gof;

/**
 *
 * @author Kirk Harland
 */
public class TotalError implements IGOF{

    @Override
    public double test(double[][] calib, double[][] test, double N){
        return test(calib,test);
    }
    
    @Override
    public double test(double[][] calib, double[][] test) {

        double ret = 0.0;

        for (int i = 0; i < calib.length; i++) {

            for (int j = 0; j < calib[i].length; j++) {

                ret += Math.abs(calib[i][j] - test[i][j]);

            }

        }

        return ( ret / 2.0 );
    }

    @Override
    public String testName() {
        return "Total Error";
    }

    @Override
    public String fieldName() {
        return "TE";
    }

    @Override
    public boolean calibrateToLessThan() {
        return true;
    }

    @Override
    public boolean isPerfect(double testStat) {
        if (testStat == 0.0) { return true; } else { return false; }
    }

}
