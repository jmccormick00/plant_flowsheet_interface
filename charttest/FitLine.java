package charttest;

import java.util.ArrayList;


public abstract class FitLine {

    public static String METHODAME = ""; 
    
    protected ArrayList<Float> d_X = null;
    protected ArrayList<Float> d_Y = null;
    
    public void setXArray(ArrayList<Float> x) {
        d_X = x;
    }
    
    public void setYArray(ArrayList<Float> y) {
        d_Y = y;
    }
    
    public ArrayList<Float> evaluateArray(ArrayList<Float> xArr) {
        ArrayList<Float> yArr = new ArrayList<>();
        
        for(float x : xArr) {
            yArr.add(evaluate(x));
        }
        return yArr;
    }
    
    public abstract float evaluate(float x);
    public abstract void calculateFitLine();
    public abstract int minPointsRequired();
}
