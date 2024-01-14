package grafici;

import java.util.List;

public class FRR {
	public FRR(String tipo,List<Float> fRR,List<Float> th) {
		disegna(tipo,fRR,th);
	}
	
	public  void disegna(String tipo,List<Float> fRR,List<Float> th) {
		CharSingle c=new CharSingle(tipo,"FRR ","threshold","FRR",fRR,th,false);
		c.setVisible(true);
	}
}
