/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceprototype2;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class TestBlock extends FSElement {
    
    private Path2D.Float d_shape;
    
    public TestBlock(Point2D p) {
        try {
            Scanner s = new Scanner(new BufferedReader(new FileReader("Banana.dxf")));
            d_shape = new Path2D.Float();
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
                else if(inLine) {  // Read in a new line
                    while(true) {
                        if("10".equals(line))
                            points[0] = s.nextFloat();
                        else if("20".equals(line))
                            points[1] = s.nextFloat();
                        else if("30".equals(line))
                            points[2] = s.nextFloat();
                        else if("11".equals(line))
                            points[3] = s.nextFloat();
                        else if("21".equals(line))
                            points[4] = s.nextFloat();
                        else if("31".equals(line)) {
                            points[5] = s.nextFloat();
                            break;  // read last line
                        }
                        line = s.next();
                    }
                    d_shape.moveTo(points[0], points[1]);
                    d_shape.lineTo(points[3], points[4]);
                    inLine = false;
                }
                else {
                    if(line.equals("LINE"))
                        inLine = true;
                }
            }
            AffineTransform t = new AffineTransform();
            t.setToTranslation(p.getX(), p.getY());
            t.scale(50, -50);
            d_shape.transform(t);
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TestBlock.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found.");
        } 
    }
    
    @Override
    public Rectangle2D getBounds() {
        return d_shape.getBounds2D();
    }

    @Override
    protected void drawShape(Graphics2D g) {
        // get a copy of the Graphics
        Graphics2D b = (Graphics2D)g.create();
        
        b.draw(d_shape);
    }
}
