package grafici;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class CharSingleInteger extends JFrame  {
	
	public CharSingleInteger(String tipo,String nome,String xName,String yName,ArrayList<Float> l,boolean fill) {
        super(nome);

        JPanel chartPanel = createChartPanel(tipo,nome,xName,yName, l,fill);
        add(chartPanel, BorderLayout.CENTER);
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
         
       
    }
   
    		
    public JPanel createChartPanel(String tipo,String nome,String xName,String yName,ArrayList<Float> l,boolean fill) {
    	    String chartTitle = nome;
    	    String xAxisLabel = xName;
    	    String yAxisLabel = yName;
    	 
    	    XYDataset dataset = createDataset(nome,l);
    	    JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
    	            xAxisLabel, yAxisLabel, dataset);
    	    XYPlot plot = chart.getXYPlot();
    	    NumberAxis xAxis = new NumberAxis();
    	    xAxis.setTickUnit(new NumberTickUnit(1));
    	    xAxis.setAutoRangeIncludesZero(false);
    	    xAxis.setRange(1,l.size());
    	    plot.setDomainAxis(xAxis);
    	    NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
    	    rangeAxis.setRange(0.0, 1);
            if(fill) {
            	 XYAreaRenderer renderer = new XYAreaRenderer();            
            	plot.setRenderer(renderer );
            }
            else {
            plot.setRenderer(new XYLineAndShapeRenderer());
            }
            plot.setBackgroundPaint(Color.white);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);
    	    File f=new File(tipo+"_"+nome);
    	    if(!f.exists()) {
    	    	try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
			
    	    int width = 640;
    	    int height = 480;
    	     
    	    try {
    	        ChartUtilities.saveChartAsPNG(f, chart, width, height);
    	    } catch (IOException ex) {
    	        System.err.println(ex);
    	    }
    	    return new ChartPanel(chart);
    	}
    
 
  
    public XYDataset createDataset(String nome,ArrayList<Float> l) {
    		XYSeriesCollection dataset = new XYSeriesCollection();
    	    boolean autoSort = false;
    	    XYSeries series = new XYSeries(nome, autoSort);	        	      	   
    	    for(int i=0;i<l.size();i++) {
    	    	int pos=i+1;
    	    	series.add(pos,l.get(i));
    	    } 	 
    	    dataset.addSeries(series);
    	    return dataset;   	
    }
}
