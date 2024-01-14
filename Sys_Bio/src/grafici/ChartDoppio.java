package grafici;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartDoppio extends JFrame {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public ChartDoppio(String tipo,String nome,String xName,String yName,List<Float> fAR,List<Float> fRR,List<Float> th) {
	        super(nome);
	 
	        JPanel chartPanel = createChartPanel(tipo,nome,xName,yName, fAR,fRR,th);
	        add(chartPanel, BorderLayout.CENTER);
	 
	        setSize(640, 480);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        
	         
	       
	    }
	   
	    		
	    public JPanel createChartPanel(String tipo,String nome,String xName,String yName,List<Float> fAR,List<Float> fRR,List<Float> th) {
	    	    String chartTitle = nome;
	    	    String xAxisLabel = xName;
	    	    String yAxisLabel = nome;
	    	 
	    	    XYDataset dataset = createDataset(fAR,fRR,th);
	    	 
	    	    JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
	    	            xAxisLabel, yAxisLabel, dataset);
	    	    XYPlot plot = chart.getXYPlot();
	    	    plot.setRenderer(new XYLineAndShapeRenderer());
	            plot.setBackgroundPaint(Color.white);
	            plot.setRangeGridlinesVisible(false);
	            plot.setDomainGridlinesVisible(false);
	            
	            
	            
	            
	    	    File imageFile = new File(tipo+"_"+nome);
	    	    if(!imageFile.exists()) {
	    	    	try {
						imageFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	    }
	    	    int width = 640;
	    	    int height = 480;
	    	     
	    	    try {
	    	        ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
	    	    } catch (IOException ex) {
	    	        System.err.println(ex);
	    	    }
	    	    return new ChartPanel(chart);
	    	}
	    
	 
	  
	    public XYDataset createDataset(List<Float> fAR2,List<Float> fRR2,List<Float> th) {
	    		XYSeriesCollection dataset = new XYSeriesCollection();
	    	    boolean autoSort = false;
	    	    XYSeries FAR = new XYSeries("FAR", autoSort);	    
	    	    XYSeries FRR = new XYSeries("FRR", autoSort);	 
	    	    for(int i=0;i<fAR2.size();i++) {
	    	    	FAR.add(th.get(i),fAR2.get(i));
	    	    	FRR.add(th.get(i),fRR2.get(i));
	    	    }
	    	    dataset.addSeries(FAR);
	    	    dataset.addSeries(FRR);
	    	    return dataset;
	    	
	    }
	    
	}

