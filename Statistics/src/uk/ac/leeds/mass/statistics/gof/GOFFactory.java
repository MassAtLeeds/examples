/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.leeds.mass.statistics.gof;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author geo8kh
 */
public class GOFFactory {

    public static final int GOF_TEST_COUNT = 12;

    public static final int GOF_SRMSE = 0;
    public static final int GOF_AED = 1;
    public static final int GOF_R2 = 2;
    public static final int GOF_ENTROPY = 3;
    public static final int GOF_CHI2 = 4;
    public static final int GOF_TAE = 5;
    public static final int GOF_SAE = 6;
    public static final int GOF_PE = 7;
    public static final int GOF_TE = 8;
    public static final int GOF_CPE = 9;
    public static final int GOF_ZSQ = 10;
    public static final int GOF_Z = 11;


    JCheckBox[] cb = new JCheckBox[GOF_TEST_COUNT];
    
    public IGOF createTest(int testType){
        switch(testType){   
            case GOF_SRMSE: 
                return new SRMSE();
            case GOF_AED:
                return new AED();
            case GOF_R2:
                return new R2();
            case GOF_ENTROPY:
                return new Entropy();
            case GOF_CHI2:
                return new CHI2();
            case GOF_TAE:
                return new TAE();
            case GOF_SAE:
                return new SAE();
            case GOF_TE:
                return new TotalError();
            case GOF_PE:
                return new PercentageError();
            case GOF_CPE:
                return new CellPercentageError();
            case GOF_ZSQ:
                return new ZSquared();
            case GOF_Z:
                return new Z();
            default:
                return null;
        }
    }

    public JPanel getGOFPanel(){

        JPanel jp = new JPanel();
        GridLayout grid = new GridLayout();
        grid.setRows(cb.length);
        jp.setLayout( grid );
        
        for (int i = 0; i < cb.length; i++) {
            cb[i] = new JCheckBox(createTest(i).testName());
            jp.add(cb[i]);
        }

        return jp;

    }



    public IGOF[] getGOFTestsSelected(){
        ArrayList<IGOF> selGof = new ArrayList<IGOF>();
        for (int i = 0; i < cb.length; i++) {
            if ( cb[i].isSelected() ){
                selGof.add( createTest(i) );
            }
        }

        IGOF[] g = new IGOF[selGof.size()];
        selGof.toArray(g);

        return g;
    }


}
