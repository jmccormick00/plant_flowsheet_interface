
package charttest;

import java.util.ArrayList;

public class RRModel {
    private RRModel() {}
    
    public static void convertRR_Y_ToRetainedValues(ArrayList<Float> RR, ArrayList<Float> dest) {
        dest.clear();
        ArrayList<Float> retained = new ArrayList<>();
        for(Float rr: RR) {
            retained.add(RRModel.fromRRYtoRetained(rr));
        }
    }
    
    // gets the percent retained from the size
    // Evaluates R(d)
    public static float fromRRYtoRetained(float size) {
        return (float)(100 / (java.lang.Math.pow(10, 
                    java.lang.Math.pow(10, (0.301 - ((size - 1) / 36.83))))));
    }
    
    public static float toRR_X_Axis(float size) {
        return (float)java.lang.Math.log10(size);
    }
    
    public static void createRR_X_Values(ArrayList<Float> size, ArrayList<Float> dest) {
        dest.clear();
        for(float x: size) {
           dest.add(RRModel.toRR_X_Axis(x)); 
        }
    }
    
    public static void createRR_Y_Values(ArrayList<Float> retainedArr, ArrayList<Float> dest) {
        dest.clear();
        for(float ret: retainedArr) {
            float x = (float)( 1 + (98 * (0.301 - java.lang.Math.log10(java.lang.Math.log10(100.0 / ret)))/2.661) );
            dest.add(x);
        }
    }
    
    public static ArrayList<Float> createRR_X_Values(ArrayList<Float> size) {
        ArrayList<Float> rrX = new ArrayList<>();
        rrX.clear();
        for(float x: size) {
           rrX.add(RRModel.toRR_X_Axis(x)); 
        }
        return rrX;
    }
    
    public static ArrayList<Float> createRR_Y_Values(ArrayList<Float> retainedArr) {
        ArrayList<Float> rrY = new ArrayList<>();
        for(float ret: retainedArr) {
            float x = (float)( 1 + (98 * (0.301 - java.lang.Math.log10(java.lang.Math.log10(100.0 / ret)))/2.661) );
            rrY.add(x);
        }
        return rrY;
    }
}
