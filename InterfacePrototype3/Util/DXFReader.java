/*------------------------------------------------------------------------------
 * DXFReader.java
 * Author: James McCormick
 * Description: Used to read in a DXF file.  Returns a Path2D object containing
 * the geometry.  If a problem is enountered, NULL is returned.
 *----------------------------------------------------------------------------*/
package Util;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class DXFReader {

    public static Path2D readFile(String filename) {
        try {
            Scanner s = new Scanner(new BufferedReader(new FileReader(filename)));
            Path2D.Float shape = new Path2D.Float();
            while(s.hasNext())
                if("ENTITIES".equals(s.next()))   // Scan till you find the ENTITIES
                    break;
            
            boolean inLine = false;
            float[] points = new float[6];
            String line;
            while(s.hasNext()){
                line = s.next();
                if("ENDSEC".equals(line))
                    break;
                else if(inLine) {
                    OUTER:
                    while (true) {
                        switch (line) {
                            case "10":
                                points[0] = s.nextFloat();
                                break;
                            case "20":
                                points[1] = s.nextFloat();
                                break;
                            case "30":
                                points[2] = s.nextFloat();
                                break;
                            case "11":
                                points[3] = s.nextFloat();
                                break;
                            case "21":
                                points[4] = s.nextFloat();
                                break;
                            case "31":
                                points[5] = s.nextFloat();
                                break OUTER;
                        }
                        line = s.next();
                    }
                    shape.moveTo(points[0], points[1]);
                    shape.lineTo(points[3], points[4]);
                    inLine = false;
                }
                else {
                    if(line.equals("LINE"))
                        inLine = true;
                }
            }
            AffineTransform t = new AffineTransform();
            t.scale(50, -50);  // Flip the shape since drawing area is reverse
            return shape;
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
            return null;
        }
    }
}
