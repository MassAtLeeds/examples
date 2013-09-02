/*
 * PercentageEror.java
 */

package uk.ac.leeds.mass.statistics.gof;

/**
 * Returns the percentage error found between the two matricies.
 * The sum of error is divided by two to give the result for misclasification
 * i.e. an error is counted twice if missing in one category and present in another
 * (+1 in one and -1 in the other with abs count this becomes 2).
 * In some instances it is more useful to know the percentage of mis-classified counts rather
 * than the standard error.
 *
 * @author Kirk Harland
 */
public class PercentageError implements IGOF {

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
            return ( ( ret / 2.0 ) / N * 100.0 );
        } else if ( N == 0.0 && ret > 0.0 ) {
            return 100.0;
        } else {
            return 0.0;
        }
    }

    @Override
    public String testName() {
        return "Percentage Error";
    }

    @Override
    public String fieldName() {
        return "PE";
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
