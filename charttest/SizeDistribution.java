package charttest;

import java.util.ArrayList;
import java.util.Observable;

public class SizeDistribution extends Observable {
    
    public String d_name;
    
    // The data
    public ArrayList<Float> d_sizeList = new ArrayList<>();
    public ArrayList<Float> d_cumulativeWt = new ArrayList<>();
    
    // The arrays to store the points converted to RR coordinates
    public ArrayList<Float> d_rrX = new ArrayList<>();
    public ArrayList<Float> d_rrY = new ArrayList<>();
    
    protected FitLine d_fitLine = null;
    
    public FitLine getFitLine() {
        return d_fitLine;
    }
    
    public void setFitLine(FitLine l) {
        d_fitLine = l;
        d_fitLine.setXArray(d_rrX);
        d_fitLine.setYArray(d_rrY);
        update();
        setChanged();
        notifyObservers();
    }
    
    // Tests rather a fitline is setup
    public boolean hasFitReq() {
        if(d_fitLine == null) {
            return false;
        }
        
        int goodSizeValues = 0;
        for(float x :  d_sizeList) {
            if(x > 0.0f) {
                goodSizeValues++;
            }     
        }
        int goodWtValues = 0;
        for(float x :  d_cumulativeWt) {
            if(x > 0.0f && x < 100.0f) {
                goodWtValues++;
            }     
        }
        if(goodSizeValues >= d_fitLine.minPointsRequired() && goodWtValues >= d_fitLine.minPointsRequired()) {
            return true;
        } else {
            return false;
        }
    }
    
    public void changeName(String name) {
        String OldName = d_name;
        d_name = name;
        setChanged();
        notifyObservers(OldName);
    }
    
    public void setCumulativeWt(int row, float wt) {
        d_cumulativeWt.set(row, wt);
        update();
        setChanged();
        notifyObservers();
    }
    
    public void setItems(float size, float ret, boolean replace) {
        if(d_sizeList.isEmpty()) {
            d_sizeList.add(size);
            d_cumulativeWt.add(ret);
            return;
        }
        int index = d_sizeList.indexOf(ret);
        if(index != -1 && replace) {
            d_cumulativeWt.set(index, ret);
        }
        for(int i = 0; i < d_sizeList.size(); ++i) {
            if(size > d_sizeList.get(i)) {
                d_sizeList.add(i, size);
                d_cumulativeWt.add(i, ret);
                return;
            }
        }
        d_sizeList.add(size);
        d_cumulativeWt.add(ret);
    }
    
    public void setSize(int row, float f) {
        d_sizeList.set(row, f);
        update();
        setChanged();
        notifyObservers();
    }
    
    // Evalutes size and returns the wt retained. 
    public float evaluate(float x) {
        return RRModel.fromRRYtoRetained(d_fitLine.evaluate(x));
    }
    
    // Evalutes the xArr and returns the Y axis.  The values are in RR scale
    public ArrayList<Float> evaluateRRArray(ArrayList<Float> xArr) {
        return d_fitLine.evaluateArray(xArr);
    }
    
    public void update() {
        if(hasFitReq()) {
            RRModel.createRR_X_Values(d_sizeList, d_rrX);
            RRModel.createRR_Y_Values(d_cumulativeWt, d_rrY);
            cleanRRValues();
            d_fitLine.calculateFitLine();
        }
    }
    
    private void cleanRRValues() {
        for(int i = 0; i < d_rrX.size(); i++) {
            if(d_rrX.get(i) == Float.NEGATIVE_INFINITY || d_rrX.get(i) == Float.POSITIVE_INFINITY) {
                d_rrX.remove(i);
                d_rrY.remove(i);
                i--;
            }
        }
        for(int i = 0; i < d_rrY.size(); i++) {
            if(d_rrY.get(i) == Float.NEGATIVE_INFINITY || d_rrY.get(i) == Float.POSITIVE_INFINITY) {
                d_rrX.remove(i);
                d_rrY.remove(i);
                i--;
            }
        }
    }
}


//public class SizeDistribution {
//    
//    public static class SizeFraction {
//            public float retained;      // In millimeters
//            public float passing;       // In millimeters
//            public float cumWt;         // decimal
//            public float fractionalWt;  // decimal
//            public float avgSize;       // Used with avg grain size calcs - calculated value
//            
//            public SizeFraction(float pass, float ret, float fractWt, float cWt) {
//                retained = ret;
//                passing = pass;
//                cumWt = cWt;
//                fractionalWt = fractWt;
//                avgSize = (pass + ret) / 2.0f;
//            }
//            
//            private SizeFraction() {}
//    }
//    
//    public ArrayList<SizeFraction> d_fractionList = new ArrayList<>();
//    
//    public SizeDistribution() {}
//    
//    public void clear() {
//        d_fractionList.clear();
//    }
//    
//    public int getNumFractions() {
//        return d_fractionList.size();
//    }
//    
//    public ArrayList<SizeFraction> getFractions() {
//        return d_fractionList;
//    }
//    
//    public int findStart(float pass) {
//       // Test to see if pass is the top size
//        if(d_fractionList.get(0).passing < pass) {
//            return 0;
//        }
//        
//        for(int i = 0; i < d_fractionList.size(); i++) {
//            if(d_fractionList.get(i).passing == pass) {
//                return i;
//            }
//        }
//        return 0;
//    }
//    
//    public int findStop(float retained) {
//        for(int i = 0; i < d_fractionList.size(); i++) {
//            if(d_fractionList.get(i).retained == retained) {
//                return i;
//            }
//        }
//        return 0;
//    }
//    
//    public float getAvgGrainSize(float pass, float retained) {
//        float avgSum = 0.0f;
//        float fractSum = 0.0f;
//        
//        int start = this.findStart(pass);
//        int stop = this.findStop(retained);
//        
//        for(int i = start; i <= stop; i++) {
//            fractSum += d_fractionList.get(i).fractionalWt;
//            avgSum += (d_fractionList.get(i).fractionalWt / d_fractionList.get(i).avgSize);
//        }
//        
//        return (fractSum/avgSum);
//    } 
//}
