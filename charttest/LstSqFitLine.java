/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charttest;

public class LstSqFitLine extends FitLine {
    
    public static String METHODAME = "Least Square";

    protected float d_slope = 0;
    protected float d_intercept = 0;
    
    @Override
    public void calculateFitLine() {
        float s0 = 0;
        float s1 = 0;
        float s2 = 0;
        float t0 = 0;
        float t1 = 0;
        
        for(int i = 0; i < d_X.size(); i++) {
            s0++;
            s1 = s1 + d_X.get(i);
            s2 = s2 + (d_X.get(i) * d_X.get(i));
            t0 = t0 + d_Y.get(i);
            t1 = t1 + (d_X.get(i) * d_Y.get(i));
        }
        
        d_slope = (s0*t1 - s1*t0)/(s0*s2 - s1*s1);
        d_intercept = (s2*t0 - s1*t1)/(s0*s2 - s1*s1);
    }

    // calculateFitLine() must be called prior to this
    @Override
    public float evaluate(float x) {
        return x * d_slope + d_intercept;
    }

    @Override
    public int minPointsRequired() {
        return 2;
    }
}
