package grafici;

import java.util.List;

public class ROC {
	public ROC(String tipo,List<Float> tPR,List<Float> fPR) {
		disegna(tipo,tPR,fPR);
	}
	
	public  void disegna(String tipo,List<Float> tPR,List<Float> fPR) {
		CharSingle c=new CharSingle(tipo,"Roc","FAR","1-FRR",tPR,fPR,false);
		System.out.println("dimensione "+fPR.size());
		c.setVisible(false);
	}
}
