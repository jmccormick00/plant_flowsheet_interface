
package charttest;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author James McCormick
 */
public class RosinRamlerChart implements Observer {
    private XYSeriesCollection d_dataSet = new XYSeriesCollection();
    private JFreeChart d_chart;
    
    private static final Float[] OrYLabels = {1.0f, 2.0f, 3.0f, 5.0f, 7.0f, 10.0f, 15.0f, 20.0f, 25.0f, 30.0f, 35.0f, 40.0f, 45.0f, 50.0f, 
             55.0f, 60.0f, 65.0f, 70.0f, 75.0f, 80.0f, 82.0f, 85.0f, 88.0f, 90.0f, 91.0f, 92.0f, 93.0f, 94.0f, 
             95.0f, 96.0f, 97.0f, 97.5f, 98.0f, 98.5f, 99.0f};
        
    private static final Float[] OrXLabels = {0.037f, 0.044f, 0.053f, 0.074f, 0.088f, 0.104f, 0.147f, 0.20f, 0.246f, 
                  0.295f, 0.4f, 0.5f, 0.589f, 0.8f, 0.9f, 1.0f, 1.168f, 1.397f, 1.651f, 1.981f, 2.362f, 
                  3.327f, 4.7625f, 6.35f, 7.9375f, 9.525f, 12.7f, 15.875f, 19.05f,
                  22.225f, 25.4f, 31.75f, 38.1f, 44.45f, 50.8f, 63.5f, 76.2f, 88.9f, 101.6f, 127f};
    
    private static ArrayList<Float> rrYLabels = RRModel.createRR_Y_Values(new ArrayList<>(java.util.Arrays.asList(OrYLabels))); 
    private static ArrayList<Float> rrXLabels = RRModel.createRR_X_Values(new ArrayList<>(java.util.Arrays.asList(OrXLabels)));
    
    public RosinRamlerChart() {

        
        d_chart = ChartFactory.createXYLineChart(
            "Rosin Rammler Screen Analysis Chart",
            "Particle Size (mm)",    // x axis label
            "% Retained",           // y axis label
            d_dataSet,
            PlotOrientation.VERTICAL,
            true, // Legend?
            true, // Tooltips?
            false // URLs?
           );
        
        XYPlot plot = (XYPlot)d_chart.getPlot();
        
        NumberAxis xAxis = new NumberAxis(plot.getDomainAxis().getLabel()) {
            @Override
            public List refreshTicks(Graphics2D g2, AxisState state, Rectangle2D dataArea, RectangleEdge edge) {
                List myTicks = new ArrayList(); 
                for(int i = 0; i < rrXLabels.size(); i++) {
                   myTicks.add(new NumberTick(rrXLabels.get(i), Float.toString(OrXLabels[i]), TextAnchor.CENTER_RIGHT, TextAnchor.CENTER_RIGHT, 5.495));
                }
                return myTicks;
            }
        };
        
        NumberAxis yAxis = new NumberAxis(plot.getRangeAxis().getLabel()) {
            @Override
            public List refreshTicks(Graphics2D g2, AxisState state, Rectangle2D dataArea, RectangleEdge edge) {
                List myTicks = new ArrayList(); 
                for(int i = 0; i < rrYLabels.size(); i++) {
                   myTicks.add(new NumberTick(rrYLabels.get(i), Float.toString(OrYLabels[i]), TextAnchor.CENTER_RIGHT, TextAnchor.CENTER_RIGHT, 0.0));
                }
                return myTicks;
            }
        };
        
        xAxis.setLabelInsets(new RectangleInsets(15.0, 5.0, 5.0, 5.0));
        xAxis.setTickLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10) );
        yAxis.setTickLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10) );
        xAxis.setAutoRange(false);
        yAxis.setAutoRange(false);
        xAxis.setRange(rrXLabels.get(0), rrXLabels.get(rrXLabels.size()-1));
        yAxis.setRange(rrYLabels.get(0), rrYLabels.get(rrYLabels.size()-1));
        yAxis.setInverted(true);

        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);
    }
    
    public void add(SizeDistribution s) {
        // Add this chart to the observer list for the SizeDistribution
        s.addObserver(this);
        
        XYSeries series = new XYSeries(s.d_name);
        XYSeries seriesLine = new XYSeries(s.d_name + "-line");
        d_dataSet.addSeries(series);
        d_dataSet.addSeries(seriesLine);

        
        // Set it up so the real values dont show lines and the fit line only renders the line
        XYPlot plot = (XYPlot)d_chart.getPlot();
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)plot.getRenderer();
        int seriesID = d_dataSet.getSeriesIndex(s.d_name + "-line");
        xylineandshaperenderer.setSeriesLinesVisible(seriesID, true);
        xylineandshaperenderer.setSeriesShapesVisible(seriesID, false);
        seriesID = d_dataSet.getSeriesIndex(s.d_name);
        xylineandshaperenderer.setSeriesLinesVisible(seriesID, false);
        xylineandshaperenderer.setSeriesShapesVisible(seriesID, true);
        
        this.updatePlot(s);
    }
    
    private void updatePlot(SizeDistribution s) {
        int seriesLineID = d_dataSet.getSeriesIndex(s.d_name + "-line");
        int seriesID = d_dataSet.getSeriesIndex(s.d_name);
        XYSeries series = d_dataSet.getSeries(seriesID);
        XYSeries seriesLine = d_dataSet.getSeries(seriesLineID);
        
        seriesLine.clear();
        series.clear();
        if(s.hasFitReq()) {
            ArrayList<Float> x = LineUtilities.linespace(rrXLabels.get(0), rrXLabels.get(rrXLabels.size()-2), 40, true);
            ArrayList<Float> y = s.evaluateRRArray(x);
            for(int i = 0; i < x.size(); i++) {
                if(y.get(i) != Float.NEGATIVE_INFINITY) {
                    seriesLine.add(x.get(i), y.get(i));
                }
            }
            
            for(int i = 0; i < s.d_rrX.size(); i++) {
                series.add(s.d_rrX.get(i), s.d_rrY.get(i));
            }
        }
    }
    
    public ChartPanel getChartPanel() {
        return new ChartPanel(d_chart);
    }

    @Override
    public void update(Observable o, Object o1) {
        SizeDistribution s = (SizeDistribution)o;
        if(o1 == null) {
            updatePlot(s);
        } else { // Got passed the old name of the size Distribution 
            // TODO - there is a bug in jFreeChart 1.0.14 that causes this to fail.  Need to upgrade to version 1.0.15 for the fix
            int seriesLineID = d_dataSet.getSeriesIndex((String)o1 + "-line");
            int seriesID = d_dataSet.getSeriesIndex((String)o1);
            XYSeries series = d_dataSet.getSeries(seriesID);
            XYSeries seriesLine = d_dataSet.getSeries(seriesLineID);
            series.setKey(s.d_name);
            seriesLine.setKey(s.d_name + "-line");
        }
    }
}
