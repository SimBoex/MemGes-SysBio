package grafici;

import java.util.List;

public class EER {
	public EER(String tipo,List<Float> fAR,List<Float> fRR,List<Float> th) {
		disegna(tipo,fAR,fRR,th);
	}
	
	public  void disegna(String tipo,List<Float> fAR,List<Float> fRR,List<Float> th) {
		ChartDoppio c=new ChartDoppio(tipo,"EER ","threshold","FAR FRR",fAR,fRR,th);
		c.setVisible(false);
	}
}
