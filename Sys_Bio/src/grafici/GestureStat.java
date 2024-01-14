package grafici;

import java.util.ArrayList;
import java.util.List;

public class GestureStat {
	
	List<Float> l;
	List<Float> threshold;
	
	public GestureStat(String tipo,List<Float> gestTasso,List<Float> thGesto) {
	this.l= gestTasso;
	this.threshold= thGesto;
	disegna(tipo,l,threshold);
	}
	
	public void disegna(String tipo,List<Float> lista,List<Float> th) {
		CharSingle c=new CharSingle(tipo,"%Gesture","threshold","y",lista,th,false);
		c.setVisible(true);
	}
}