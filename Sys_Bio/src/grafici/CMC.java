package grafici;

import java.util.ArrayList;
import java.util.List;

public class CMC {
	
	public CMC(String tipo,ArrayList<Float> l) {
		disegna(tipo,l);
	}
	
	
	public  void disegna(String tipo,ArrayList<Float> l) {//inverso nome ordine
		CharSingleInteger c=new CharSingleInteger(tipo,"CMC","ranghi","CMC",l,false);
		c.setVisible(false);
	}
}
