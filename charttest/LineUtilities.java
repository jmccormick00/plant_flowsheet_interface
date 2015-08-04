package charttest;

import java.util.ArrayList;

public class LineUtilities {
    
    public static ArrayList<Float> linespace(float start, float end, int num, boolean endpoint) {
        ArrayList<Float> result = new ArrayList<>();
        if(num > 0) {
            if(endpoint) {
                if(num == 1) {
                    result.add(start);
                } else {
                    float step = (end - start) / ((float)num - 1.0f);
                    for(int i = 0; i < num; i++) {
                        result.add((i*step) + start);
                    }
                }
            } else {
                float step = (end - start) / ((float)num);
                for(int i = 0; i < num; i++) {
                        result.add((i*step) + start);
                    }
            }
        }
        
        return result;
    }
}
