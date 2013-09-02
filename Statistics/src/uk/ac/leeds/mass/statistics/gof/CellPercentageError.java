/*
 * TCellPercentageError.java
 */

package uk.ac.leeds.mass.statistics.gof;

/**
 *
 * @author Kirk Harland
 */
public class CellPercentageError implements IGOF {

    @Override
    public double test(double[][] calib, double[][] test, double N){
        return test(calib,test);
    }
    
    @Override
    public double test(double[][] calib, double[][] test) {

        double ret = 0.0;
        double N = 0.0;

        for (int i = 0; i < calib.length; i++) {

            for (int j = 0; j < calib[i].length; j++) {

                N += calib[i][j];

                ret += Math.abs(calib[i][j] - test[i][j]);

            }

        }

        if ( N > 0.0 && ret > 0.0 ) {
            return ( ( ret ) / N * 100.0 );
        } else if ( N == 0.0 && ret > 0.0 ) {
            return Math.abs( ret ) * 100.0;
        } else {
            return 0.0;
        }
    }

    @Override
    public String testName() {
        return "Cell Percentage Error";
    }

    @Override
    public String fieldName() {
        return "CPE";
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
