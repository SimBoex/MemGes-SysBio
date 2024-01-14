package dataset;
import matrix.*;

import java.io.IOException;
import java.util.HashMap; 
import java.util.Set;
public class ProbequalsGallery {
	public HashMap<String,String> X,Y,Z,c;
	public float IdrateX,IdrateY,IdrateZ,IdrateCom;
	public float GesrateX,GesrateY,GesrateZ,GesrateCom;
	
	public float IdrateF,GesrateF;
	public ProbequalsGallery(String alg,boolean inter ) throws IOException {
		String s;
		if(inter) {
			s="WithInter";
		}
		else {
			s="WithoutInter";
		}
		if(!alg.equals("Dtw")) {
			FuzzyMatrix m=new FuzzyMatrix("probe=gallery",inter);
			m.setValori(inter);
			m.setDistanceMatrixComb();
			X=m.findUserIsMatchedInMatrixFuzzy("distanzaFuzzyAsseX"+s+".csv"); //creo mappe che legano file a file piu' simile trovato dal dtw
			Y=m.findUserIsMatchedInMatrixFuzzy("distanzaFuzzyAsseY"+s+".csv");
			Z=m.findUserIsMatchedInMatrixFuzzy("distanzaFuzzyAsseZ"+s+".csv");
			c=m.findUserIsMatchedInMatrixFuzzy("DistanceFuzzyMatrixByAll"+s+".csv");
			rateForEach();
			rateLikeArt(m,inter);			
			}
		}
	
	public ProbequalsGallery(String x) {
		if(x.equals("Dtw")) {
			DtwMatrix m=new DtwMatrix("probe=gallery");//invoco matrici che si possono scrivere;
			m.setValori();
			m.setDistanceMatrixComb();
			 X=m.findUserIsMatchedInMatrixDtw("distanzaAsseX.csv"); //creo mappe che legano file a file piu' simile trovato dal dtw
			 Y=m.findUserIsMatchedInMatrixDtw("distanzaAsseY.csv");
			 Z=m.findUserIsMatchedInMatrixDtw("distanzaAsseZ.csv");
			 c=m.findUserIsMatchedInMatrixDtw("DistanceMatrixByAll.csv");
			 rateForEach();	 
		}	
	}

	public void rateLikeArt(FuzzyMatrix m,boolean inter) {
		float[] valori=m.trovaRate(inter);
		IdrateF=valori[0];
		GesrateF=valori[1];
	}
	
	public float[] rateGruppo(HashMap<String,String> map) {
		float[] buffer=new float[2];
		float RrId=0;
		float Rrges=0;
		Set<String> keys = map.keySet();
		float dimProbe=keys.size();	
		String matchedFile,matchedGesto,matchedId;
		for (String key: keys) {
			String id=key.split("_")[0];
			String gesto=key.split("_")[2];
			matchedFile=(String) map.get(key);
			matchedGesto=matchedFile.split("_")[2];
			matchedId=matchedFile.split("_")[2];			
			if (gesto.equals(matchedGesto)) {
				Rrges++;			
			}
			 if (id.equals(matchedId)) {
				RrId++;			
			}		
		}
		buffer[0]=RrId/dimProbe;//inserire tasso poi
		buffer[1]=Rrges/dimProbe;
		return buffer;
	}
			
	public  float[] rateAsseSingola(HashMap<String,String> x) { // incrementa recognition rate per asse singola del id e del gesto; restituisce (RrId,Rrges)
		float[] buffer=new float[2];
		float RrId=0;
		float Rrges=0;
		Set<String> keys = x.keySet();
		float dimProbe=keys.size();
		String matchedFile,matchedGesto,matchedId;
		for (String key: keys) {
			String id=key.split("_")[0];
			String gesto=key.split("_")[2];
			matchedFile=(String) x.get(key);
			
				matchedGesto=matchedFile.split("_")[2];
				matchedId=matchedFile.split("_")[0];
				if (gesto.equals(matchedGesto)) {
					Rrges++;							
				}
				if (id.equals(matchedId)) {
					RrId++;				
				}						
			
		}
		buffer[0]=RrId/dimProbe;//inserire tasso poi
		buffer[1]=Rrges/dimProbe;
		return buffer;
	}
	public   float[] rateAsseCombinata(HashMap<String,String> xyz) {
		float Rrid=0;
		float Rrges=0;
		float [] buffer=new float[2];
		Set<String> set=xyz.keySet();
		float dimProbe=set.size();
		for (String key:set) {
			String id=key.split("_")[0];
			String gesto=key.split("_")[2];
			String matchedFile=(String) xyz.get(key);
			
				if (gesto.equals(matchedFile.split("_")[2])) {
					Rrges++;
					}
				if (id.equals(matchedFile.split("_")[0])){
						Rrid++;
				}
				
			
		}
		buffer[0]=Rrid/dimProbe;
		buffer[1]=Rrges/dimProbe;
		return buffer;
	}

	public void rateForEach() {
		// calcolo i rate in percentuale(contatori divisi numero di probe) per asse Singola
		 float [] resAsseSingolaX=rateAsseSingola(X);
		 IdrateX=resAsseSingolaX[0];
		 GesrateX=resAsseSingolaX[1];
		 float [] resAsseSingolaY=rateAsseSingola(Y);
		 IdrateY=resAsseSingolaY[0];
		 GesrateY=resAsseSingolaY[1];
		 float [] resAsseSingolaZ=rateAsseSingola(Z);
		 IdrateZ=resAsseSingolaZ[0];
		 GesrateZ=resAsseSingolaZ[1];
		//calcolo rate per asse combinata come prima
		float [] resAsseCombinata=rateAsseCombinata(c);
		IdrateCom=resAsseCombinata[0];
		 GesrateCom=resAsseCombinata[1];
		
	}
	public void release() {
		// TODO Auto-generated method stub
		X.clear();
		Y.clear();
		Z.clear();
		c.clear();
	}
		
}
