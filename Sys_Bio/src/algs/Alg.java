package algs;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
public class Alg {
	
	public Alg() {}
	
	public static double AlgFuzzyFunction(ArrayList<Double> g1x, ArrayList<Double> g2x, Boolean inter){
		
		String[][] matriceDir=new String[g1x.size()][g2x.size()];
		double[][] matrice=new double[g1x.size()][g2x.size()];
        double h;
        double sigma;
        if (inter) {
            h = (double) 0.45;
            sigma = (double) 0.85;
        }
        else{
             h = (double) 0.05;
             sigma = (double) 0.55;
        }
        for (int i=1;i<g1x.size();i++){
                matrice[i][0]=h*(i+1); 
                matriceDir[i][0]="su";
        }
        for (int j=1;j<g2x.size();j++){
        	matrice[0][j]=h*(j+1);
        	matriceDir[0][j]="sx";
        }
        matrice[0][0]=0;
        for (int i=1;i<g1x.size();i++){
            for (int j=1;j<g2x.size();j++){ //scorro le colonne
                double diag = (double) (matrice[i - 1][j - 1] + fuzzy(g1x.get(i-1),g2x.get(j-1),sigma));
                double su=matrice[i-1][j]+h;
                double sx=matrice[i][j-1]+h;
                matrice[i][j]=Math.max(Math.max(diag,su),sx);

                if(matrice[i][j]==diag) {
                	matriceDir[i][j]="diag";
                }
                else if(matrice[i][j]==su){
                	matriceDir[i][j]="su";
                }
                else {
                	matriceDir[i][j]="sx";
                }
    			}
    			} 
        
		List<Double> vetI = new ArrayList<Double>();
        List<Double> vetJ = new ArrayList<Double>();
        
        int i2=g1x.size()-2;
        int j2=g2x.size()-2;        
        
        
        
        for (int x=0;x<g1x.size();x++) {
        	vetI.add(g1x.get(x));
        	vetJ.add(g2x.get(x));
        }
      

  do {
        	
        	String dir=matriceDir[i2][j2];
        	if(dir.equals("diag")) {
        		j2--;
        		i2--;
        	}
        	else if(dir.equals("su")) {       		
        		vetJ.add(j2+1,(double) 0);
        		i2--;
        	}
        	else {
        		vetI.add(i2+1,(double)0);    
        		j2--;
        	}
        //System.out.println(dir);	
        }while (matrice[i2][j2]!=0);
        
  	
  		
        if (inter){
            interpolating(vetI);
            interpolating(vetJ);
        }
      
        
	//System.out.println(vetI.toString());
	//System.out.println(vetJ.toString());
	double distEuclidea=distE(vetI,vetJ);
	
    return distEuclidea;
    }
	
	 public static double fuzzy(Double double1, Double double2, double sigma){
        	 double var=(double) Math.exp((double1-double2)*(double1-double2)/(2*sigma*sigma));
       	 
        	 double var3=(double) (1.0/var); 
        	// System.out.println("il valore fuzzy tra "+double1+" e "+double2+" è "+var3);

        	 return var3;
	 }
	 
	 public static List<Double> interpolating(List<Double> vetI){
			 	
		 	for (int i=1;i<vetI.size();i++){
	        	Double estremosx=vetI.get(i-1);
	            if (vetI.get(i)==0 &&  estremosx!=0) {
	            	int j=i;
	                while(vetI.get(j)==0 && j<vetI.size()-1){
	                    j++;
	                }
	                
                	if (vetI.get(j)!=0) {
	                Double estremodx=vetI.get(j);  
	                
	               double valore=(estremodx+estremosx)/(double)2;
	                vetI.set(i,valore);
                	}
	                
	            }
	                
	        }
		 ;
			 return vetI;
	    }
	 public static  double distE(List<Double> vetI, List<Double> vetJ){
		 double tot=0;
		
	        for (int i=0;i<vetI.size();i++){
	        	double valoreI=vetI.get(i);
	        	double valoreJ=vetJ.get(i);	        	
	        	double quad=(valoreI-valoreJ)*(valoreI-valoreJ);
	        	
	            tot=tot+quad;
	            
	            	
	            
	            	
	        }
	        double rad=(double) Math.sqrt(tot);
	       
	        return   rad;
	    }

	
	public static  double Dtw(ArrayList<Double> pX,ArrayList<Double> g1x) {
	
		int size1=pX.size();
		int size2=g1x.size();
		double[][] matrice=new double[size1+1][size2+1];
		double buffer;
		int cont=0;// inserisco prima riga di ogni matrice;
		while (cont<size1+1) {
				matrice[cont][0]= (double) Double.POSITIVE_INFINITY;
				cont++;
		}
		cont=0;// inserisco prima colonna
		while (cont<size2+1) {
			matrice[0][cont]= (double) Double.POSITIVE_INFINITY;
			cont++;
		}
		
		matrice[0][0]= (double) 0;
		for (int i=1;i<size1+1;i++) {
			for (int j=1;j<size2+1;j++) {		
			matrice[i][j]= Math.abs(Double.valueOf(pX.get(i-1))-Double.valueOf(g1x.get(j-1))) +Math.min(Math.min(matrice[i-1][j-1], matrice[i][j-1]),matrice[i-1][j]);
			}
			
			}	
		
		buffer=matrice[size1][size2];
		return buffer;
	}
}



