package charttest;

import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class CubicTest {
    public static void main(String[] args) {
        
        Float[] X = {0.0f, 2.0f, 3.0f, 5.0f, 8.0f, 10.0f};
        Float[] Y = {0.0f, 0.91f, 0.14f, -0.96f, 0.99f, 0.54f};
        ArrayList<Float> xArray = new ArrayList<>(java.util.Arrays.asList(X));
        ArrayList<Float> yArray = new ArrayList<>(java.util.Arrays.asList(Y));
        
        CubicSpline spline = new CubicSpline();
        spline.setXArray(xArray);
        spline.setYArray(yArray);
        spline.calculateFitLine();
        
        ArrayList<Float> newX = LineUtilities.linespace(0.0f, 4.0f, 30, true);
        ArrayList<Float> intY = spline.evaluateArray(newX);
        ArrayList<Float> realY = new ArrayList<>();
        
        for(int i = 0; i < newX.size(); ++i) {
            realY.add((float)Math.sin(newX.get(i)));
        }
        
        XYSeries series = new XYSeries("spline");
        for(int i = 0; i < newX.size(); ++i) {
            series.add(newX.get(i), intY.get(i));
        }
        
        XYSeries series1 = new XYSeries("sine");
        for(int i = 0; i < newX.size(); ++i) {
            series1.add(newX.get(i), realY.get(i));
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(series1);
        
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Cubic Spline",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        
        ChartFrame frame = new ChartFrame("Cubic Spline", chart);
        frame.pack();
        frame.setVisible(true);
    }
}
