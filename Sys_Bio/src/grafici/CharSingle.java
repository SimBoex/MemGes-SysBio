package grafici;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class CharSingle extends JFrame {
	
	
	private static final long serialVersionUID = 1L;



	public CharSingle(String tipo,String nome,String xName,String yName,List<Float> fRR,List<Float> th,boolean fill) {
        super(nome);

        JPanel chartPanel = createChartPanel(tipo,nome,xName,yName, fRR,th,fill);
        add(chartPanel, BorderLayout.CENTER);
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
         
       
    }
   
    		
    public JPanel createChartPanel(String tipo,String nome,String xName,String yName,List<Float> fRR,List<Float> th,boolean fill) {
    	    String chartTitle = nome;
    	    String xAxisLabel = xName;
    	    String yAxisLabel = yName;
    	 
    	    XYDataset dataset = createDataset(nome,fRR,th);
    	 
    	    JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
    	            xAxisLabel, yAxisLabel, dataset);
    	    XYPlot plot = chart.getXYPlot();
            
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
    
 
  
    public XYDataset createDataset(String nome,List<Float> fRR,List<Float> th) {
    		XYSeriesCollection dataset = new XYSeriesCollection();
    	    boolean autoSort = false;
    	    XYSeries series = new XYSeries(nome, autoSort);	        	      	   
    	    for(int i=0;i<th.size();i++) {
    	    	series.add(th.get(i),fRR.get(i));
    	    } 	 
    	    dataset.addSeries(series);
    	    return dataset;   	
    }
}
