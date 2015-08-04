
package charttest;

import java.util.ArrayList;

/**
 *
 * @author James McCormick
 */
public class CubicSpline extends FitLine {
    
    public static String METHODAME = "Cubic Spline";
    private final static float ZEROCHECK = 0.0001f;

    // The coefficients for the cubic spline polynomial
    protected ArrayList<Float> d_fACoef = new ArrayList<>(); 
    protected ArrayList<Float> d_fBCoef = new ArrayList<>();
    protected ArrayList<Float> d_fCCoef = new ArrayList<>();
    protected ArrayList<Float> d_fDCoef = new ArrayList<>();

    @Override
    public float evaluate(float x) {
        for(int i = 0; i < d_X.size()-1; ++i) {
            if( (d_X.get(i) <= x && x <= d_X.get(i+1)) || (d_X.get(i) >= x && x >= d_X.get(i+1)) ) {
                float temp = d_fACoef.get(i) + d_fBCoef.get(i)*x + d_fCCoef.get(i)*x*x + d_fDCoef.get(i)*(x-d_X.get(i))*(x-d_X.get(i))*(x-d_X.get(i));
                if( temp < ZEROCHECK && temp > -ZEROCHECK ) {
                    return 0.0f;
                } else {
                    return temp;
                }
            }
	}
	return Float.NEGATIVE_INFINITY;
    }

    @Override
    public void calculateFitLine() {
        clean();
        float[] deltaX = new float[d_X.size()];    // The delta's for the x values
        float[] deltaY = new float[d_X.size()];    // The delta's for the y values
        float[] S = new float[d_X.size()];         // The second derivative of each polynomial
        
        deltaX[0] = 0.0f;
        deltaY[0] = 0.0f;
        for(int i = 1; i < d_X.size(); ++i) {
            deltaX[i] = d_X.get(i) - d_X.get(i-1);
            deltaY[i] = d_Y.get(i) - d_Y.get(i-1);
        }
        // Calculate the second derivatives
        computeSecondDerivatives(S, deltaX, deltaY);
        
        // Compute the B coefficients
        int i = 1;
        for(int j = 0; j < d_X.size()-1; ++j) {
            d_fBCoef.add( (deltaY[i]/deltaX[i]) - S[i-1]*(deltaX[i]*0.5f + d_X.get(i-1)) - ((S[i]-S[i-1])*deltaX[i]) / 6.0f );
            ++i;
        }
        
        // Compute the A coefficients
        i = 1;
        for(int j = 0; j < d_X.size()-1; ++j) {
            d_fACoef.add( d_Y.get(i-1) - d_fBCoef.get(j)*d_X.get(i-1) - S[i-1]*(d_X.get(i-1)*d_X.get(i-1)) * 0.5f );
            ++i;
        }
        
        // Compute the C coefficients
        i = 1;
	for(int j = 0; j < d_X.size()-1; ++j) {
            d_fCCoef.add(S[i-1] * 0.5f); 
            ++i;
	}
        
        // Compute the D coefficients
        i = 1;
	for(int j = 0; j < d_X.size()-1; ++j) {
            d_fDCoef.add( (S[i]-S[i-1])/(deltaX[i] * 6.0f) ); 
            ++i;
	}
    }
    
    public void clean() {
       d_fACoef.clear();
       d_fBCoef.clear();
       d_fCCoef.clear();
       d_fDCoef.clear();
    }
    
    public float evalFirstDerivative(float x) {
        for(int i = 1; i < d_X.size(); ++i) {
            if( (d_X.get(i) <= x && x <= d_X.get(i+1)) || (d_X.get(i) >= x && x >= d_X.get(i+1)) ) {
                float temp = d_fBCoef.get(i) + 2.0f*d_fCCoef.get(i)*x + 3.0f*d_fDCoef.get(i)*(x-d_X.get(i))*(x-d_X.get(i));
                if( temp < ZEROCHECK && temp > -ZEROCHECK ) {
                    return 0.0f;
                } else {
                    return temp;
                }
            }
	}
	return Float.NEGATIVE_INFINITY;
    }
    
    protected void computeSecondDerivatives(float[] S, float[] deltaX, float[] deltaY) {
        // The second derivates are found by the following equation
	// (DeltaX[i])*(S[i-1]) + 2*(DeltaX[i] + deltaX[i+1]) + (DeltaX[i+1]*S[i+1]) = 6*((DeltaY[i+1]/DeltaX[i+1]) - (DeltaY[i]/DeltaX[i]))
	// The system to solve for the second derivatives will be:
	// [A][S] = [B]
	
	// For natural splines S[0] and S[n] equal 0
	S[0] = 0.0f;
	S[S.length-1] = 0.0f;

	// To solve for the S array a system of equations will be used.
	// The coefficient matrix [A] is made up as follows
	// |b0 c0 0  0 |
	// |a1 b1 c1 0 |
	// |0  a2 b2 c2|
	// |0  0  an bn|
	int numRows = d_X.size() - 2;

	// Instead of storing an entire matrix (most of which would be zero for large number of points), the values on each diagonal 
	// in the matrix will be stored in arrays
	float[] a = new float[numRows];
	float[] b = new float[numRows];
	float[] c = new float[numRows];
	float[] B = new float[numRows];

	// Fill up the 3 arrays that make up the tri diagonal matrix [A]
	int i = 1;	// start with S[1]
	for(int row = 0; row < numRows; ++row) {
            // Calculate B
            B[row] = 6.0f * ( deltaY[i+1]/deltaX[i+1] - deltaY[i]/deltaX[i] );

            if(row != 0) {
                a[row] = deltaX[i];
            }
            b[row] = 2.0f * (deltaX[i] + deltaX[i+1]);
            if(row != (numRows-1)) {
                c[row] = deltaX[i+1];
            }
            ++i;
	}

	// Use the tri diagonal matrix algorithm to solve for S
        float[] x = new float[numRows];
	TDMA(a, b, c, B, x, numRows);
        System.arraycopy(x, 0, S, 1, x.length);
    }
    
    protected void TDMA(float[] a, float[] b, float[] c, float[] B, float[] X, int size) {
        // The system ([A][X] = [B]) will be solved using LU decomposition
	// So decompose [A] into [L] and [U] such that [L][U][X] = [B]

	// Fill L and U
	// [A] =	|b0 c0 0   0|
	//		|a1 b1 c1  0|
	//		|0  a2 b2 c2|
	//		|0..........|
	//		|0  0  an bn|

	// [L] =	|f0 0  0   0|
	//		|e1 f1 0   0|
	//		|0  e2 f2  0|
	//		|0 ....en fn|

	// [U] =	|1  g0 0   0|
	//		|0  1  g1  0|
	//		|0..........|
	//		|0  0  1  gn-1|
	//		|0  0  0   1|

	// Where f0 = b0, g0 = c0/f0
	// for k = 1, 2,....,(n-1)
	//	ek = ak
	//	fk = bk - (ek)*(gk-1)
	//	gk = ck/fk
	// Then en = an, fn = bn - en*(gn-1)
	
	// To increase memory efficiency the values of e, f, g can be
	// stored in place of a, b, c

	c[0] = c[0]/b[0];
	for(int i = 1; i < size-1; ++i) {
            b[i] = b[i] - a[i]*c[i-1];
            c[i] = c[i]/b[i];
	}
	b[size-1] = b[size-1] - a[size-1]*c[size-2];
	
	// Now that we have the [L] and [U] matrices.  Solve the system by
	// first solving [L][Z] = [B] then [U][X] = [Z]

	// First solve [L][Z] = [B]
	// To be efficient, store the Z in place of B
	B[0] = B[0]/b[0];
	for(int i = 1; i < size; ++i) {
            B[i] = (B[i]-a[i]*B[i-1])/b[i];
        }

	// Then solve for [X]
	X[size-1] = B[size-1];
	for(int i = size-2; i >= 0; --i) {
            X[i] = B[i]-c[i]*X[i+1];
        }
    }

    @Override
    public int minPointsRequired() {
        return 4;
    }
    
}
