package grafici;

import java.util.List;

public class FAR {

	public FAR(String tipo,List<Float> fAR,List<Float> th) {
		disegna(tipo,fAR,th);
	}
	
	public  void disegna(String tipo,List<Float> fAR,List<Float> th) {
		CharSingle c=new CharSingle(tipo,"FAR","FAR ","threshold",fAR,th,false);
		c.setVisible(false);
	}
}
