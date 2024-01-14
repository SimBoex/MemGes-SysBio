package grafici;

import java.util.List;

public class AUC {
	public float valoreAuc=0;
	public AUC(String tipo,List<Float> tPR,List<Float> fPR) {
		
		valoreAuc=get(tPR,fPR);
		disegna(tipo,tPR,fPR);
	}
	
	public  float get(List<Float> tPR,List<Float> fPR) {    
		float sum=0;	
	      for (int i = 0; i < tPR.size()-1; i++) {
	    	  float baseM=tPR.get(i);
	    	  float baseMin=tPR.get(i+1);
	    	  float Area=(baseM+baseMin)*(fPR.get(i+1)-fPR.get(i))/2;
	         sum = sum + Area;	         
	      }
	      
	      return sum ;
	}
	
	public  void disegna(String tipo,List<Float> tPR,List<Float> fPR) {
		CharSingle c=new CharSingle(tipo,"AUC","FAR","1-FRR", tPR,fPR,true);
		c.setVisible(true);
	}
}
