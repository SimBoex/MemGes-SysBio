package dataset;
import java.io.File;  
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;
import matrix.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class crossValidification {
	public float Idrate1vs2X,Idrate1vs2Y,Idrate1vs2Z,Idrate1vs2Com;
	public float Gesrate1vs2X,Gesrate1vs2Y,Gesrate1vs2Z,Gesrate1vs2Com;
	public float Idrate2vs1X,Idrate2vs1Y,Idrate2vs1Z,Idrate2vs1Com;
	public float Gesrate2vs1X,Gesrate2vs1Y,Gesrate2vs1Z,Gesrate2vs1Com;
	
	public float Idrate1vs2F,Idrate2vs1F;
	public float Gesrate1vs2F,Gesrate2vs1F;
	
	public Pattern[] arrayRegexProbe;
	public Pattern[] arrayRegexGallery;
	public crossValidification() {
		arrayRegexProbe=new Pattern[6];
		arrayRegexGallery=new Pattern[6];
		//inserisco varie regex
		arrayRegexProbe[0]=Pattern.compile("(.+_[1]_cerchio_[012]\\.csv)|(.+_[1]_rettangolo_[012]\\.csv)");
		arrayRegexGallery[0]=Pattern.compile("(.+_[23]_cerchio_[012]\\.csv)|(.+_[23]_rettangolo_[012]\\.csv)");
		
		arrayRegexProbe[1]=Pattern.compile("(.+_[2]_cerchio_[012]\\.csv)|(.+_[2]_rettangolo_[012]\\.csv)");
		arrayRegexGallery[1]=Pattern.compile("(.+_[13]_cerchio_[012]\\.csv)|(.+_[13]_rettangolo_[012]\\.csv)");
		
		arrayRegexProbe[2]=Pattern.compile("(.+_[3]_cerchio_[012]\\.csv)|(.+_[3]_rettangolo_[012]\\.csv)");
		arrayRegexGallery[2]=Pattern.compile("(.+_[12]_cerchio_[012]\\.csv)|(.+_[12]_rettangolo_[012]\\.csv)");
		
		arrayRegexProbe[3]=Pattern.compile("(.+_[12]_cerchio_[012]\\.csv)|(.+_[12]_rettangolo_[012]\\.csv)");
		arrayRegexGallery[3]=Pattern.compile("(.+_[3]_cerchio_[012]\\.csv)|(.+_[3]_rettangolo_[012]\\.csv)");
		
		arrayRegexProbe[4]=Pattern.compile("(.+_[32]_cerchio_[012]\\.csv)|(.+_[32]_rettangolo_[012]\\.csv)");
		arrayRegexGallery[4]=Pattern.compile("(.+_[1]_cerchio_[012]\\.csv)|(.+_[1]_rettangolo_[012]\\.csv)");
		
		arrayRegexProbe[5]=Pattern.compile("(.+_[13]_cerchio_[012]\\.csv)|(.+_[13]_rettangolo_[012]\\.csv)");
		arrayRegexGallery[5]=Pattern.compile("(.+_[2]_cerchio_[012]\\.csv)|(.+_[2]_rettangolo_[012]\\.csv)");
	}

	public void rateForEach(String alg) { 
		// TODO Auto-generated method stub
		if(alg.equals("DTW")) {
		for (int y=0;y<3;y++) {
			float[] rateX=getRateDtw(arrayRegexProbe[y],arrayRegexGallery[y],"x"); 
			float[] rateY=getRateDtw(arrayRegexProbe[y],arrayRegexGallery[y],"y");
			float[] rateZ=getRateDtw(arrayRegexProbe[y],arrayRegexGallery[y],"z");
			float [] rateCom=getRateByAllDtw(arrayRegexProbe[y],arrayRegexGallery[y]);
			Idrate1vs2X+=rateX[0];
			Gesrate1vs2X+=rateX[1];
			Idrate1vs2Y+=rateY[0];
			Gesrate1vs2Y+=rateY[1];
			Idrate1vs2Z+=rateZ[0];
			Gesrate1vs2Z+=rateZ[1];
			Idrate1vs2Com+=rateCom[0];
			Gesrate1vs2Com+=rateCom[0];		
			}
		
		Idrate1vs2X=Idrate1vs2X/3;
		Idrate1vs2Y=Idrate1vs2Y/3;
		Idrate1vs2Z=Idrate1vs2Z/3;
		Idrate1vs2Com=Idrate1vs2Com/3;
		Gesrate1vs2X=Gesrate1vs2X/3;
		Gesrate1vs2Y=Gesrate1vs2Y/3;
		Gesrate1vs2Z=Gesrate1vs2Z/3;
		Gesrate1vs2Com=Gesrate1vs2Com/3;
		
		for (int y=3;y<6;y++) {
			float[] rateX=getRateDtw(arrayRegexProbe[y],arrayRegexGallery[y],"x"); 
			float[] rateY=getRateDtw(arrayRegexProbe[y],arrayRegexGallery[y],"y");
			float[] rateZ=getRateDtw(arrayRegexProbe[y],arrayRegexGallery[y],"z"); 
			float [] rateCom=getRateByAllDtw(arrayRegexProbe[y],arrayRegexGallery[y]);
			Idrate2vs1X+=rateX[0];
			Gesrate2vs1X+=rateX[1];
			Idrate2vs1Y+=rateY[0];
			Gesrate2vs1Y+=rateY[1];
			Idrate2vs1Z+=rateZ[0];
			Gesrate2vs1Z+=rateZ[1];
			Idrate2vs1Com+=rateCom[0];
			Gesrate2vs1Com+=rateCom[1];
			}
		Idrate2vs1X=Idrate2vs1X/3;
		Idrate2vs1Y=Idrate2vs1Y/3;
		Idrate2vs1Z=Idrate2vs1Z/3;
		Idrate2vs1Com=Idrate2vs1Com/3;
		Gesrate2vs1X=Gesrate2vs1X/3;
		Gesrate2vs1Y=Gesrate2vs1Y/3;
		Gesrate2vs1Z=Gesrate2vs1Z/3;
		Gesrate2vs1Com=Gesrate2vs1Com/3;
	}
	}
	
	public void rateFuzzy(String alg,boolean inter) throws IOException { 
		for (int y=0;y<3;y++) {
			float[] rate=getRateFuzzy(arrayRegexProbe[y],arrayRegexGallery[y],inter); 		
			Idrate1vs2F+=rate[0];
			Gesrate1vs2F+=rate[1];
			
			
		}
		Idrate1vs2F=(float)Idrate1vs2F/(float)3;
		Gesrate1vs2F=(float)Gesrate1vs2F/(float)3;
		for (int y=3;y<6;y++) {
			float[] rateX=getRateFuzzy(arrayRegexProbe[y],arrayRegexGallery[y],inter); 		
			Idrate2vs1F+=rateX[0];
			Gesrate2vs1F+=rateX[1];
		
			}
		Idrate2vs1F=Idrate2vs1F/3;
		Gesrate2vs1F=Gesrate2vs1F/3;
		
	}
		
	private float[] getRateFuzzy(Pattern regexProbe,Pattern regexGallery,boolean inter) throws IOException {
		FuzzyMatrix m=new FuzzyMatrix("cross",inter); //invoco matrici che si possono soltanto leggere;
		float[] buffer=m.trovaRateCross(regexProbe, regexGallery);
		try {
			m.reader.close();
			m.reader1.close();
			m.reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return buffer;
	}
	
	private float[] getRateByAllDtw(Pattern regexProbe, Pattern regexGallery) { 
		DtwMatrix m=new DtwMatrix("cross");
		File dir=m.getPathDir();
		CSVReader r;
		File f=new File("DistanceMatrixByAll.csv");
		HashMap<String,String> map=new HashMap<String,String>();
		try {
			r=new CSVReader(new FileReader(f));
			java.util.List<String[]> valori;
			valori = r.readAll();		
		float min=Float.POSITIVE_INFINITY;
		int pos=0;
		int posP=0;
		File filesList[] = dir.listFiles();
		for (int x=0;x<filesList.length;x++) {
			java.util.regex.Matcher d = regexProbe.matcher(filesList[x].getName().toString());
			boolean checkProbe = d.matches(); 
			if (checkProbe) {
				for (int y=0;y<filesList.length;y++) {
					java.util.regex.Matcher d1 = regexGallery.matcher(filesList[y].getName().toString());
					boolean checkGallery = d1.matches(); 
					if (checkGallery) {
						float v=Float.valueOf(valori.get(x+1)[y+1]);
						if ((v<=min) && (!filesList[x].getName().equals(filesList[y].getName()))) {
							min=v;
							pos=y;
							posP=x;
						}						
					}				
				}
				map.put(filesList[posP].getName().toString(), filesList[pos].getName().toString());
				min=Float.POSITIVE_INFINITY;
				pos=0;
				posP=0;
			}		
		}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float[] l=rateAsseSingola(map);
		return l;
}
	
	public float[] getRateDtw(Pattern regexProbe,Pattern regexGallery,String asse) {
		DtwMatrix m=new DtwMatrix("cross"); //invoco matrici che si possono soltanto leggere;
		File dir=m.getPathDir();
		CSVReader r;
		if(asse.equals("x")) {
			r=m.reader;
		}
		else if (asse.equals("y")) {
			r=m.reader1;
		}
		else {
			r=m.reader2;
		}
		HashMap<String,String> map=new HashMap<String,String>();
		try {
			java.util.List<String[]> valori;
			valori = r.readAll();		
		float min=Float.POSITIVE_INFINITY;
		int pos=0;
		int posP=0;
		File filesList[] = dir.listFiles();
		for (int x=0;x<filesList.length;x++) {
			java.util.regex.Matcher d = regexProbe.matcher(filesList[x].getName().toString());
			boolean checkProbe = d.matches(); 
			if (checkProbe) {
				for (int y=0;y<filesList.length;y++) {
					java.util.regex.Matcher d1 = regexGallery.matcher(filesList[y].getName().toString());
					boolean checkGallery = d1.matches(); 
					if (checkGallery) {
						float v=Float.valueOf(valori.get(x+1)[y+1]);
						if ((v<=min) && (!filesList[x].getName().equals(filesList[y].getName()))) {
							min=v;
							pos=y;
							posP=x;
						}						
					}				
				}
				map.put(filesList[posP].getName().toString(), filesList[pos].getName().toString());
				min=Float.POSITIVE_INFINITY;
				pos=0;
				posP=0;
			}		
		}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		float[] l=rateAsseSingola(map);
		return l;
	}
	
	public  float[] rateAsseSingola(HashMap<String,String> x) { // incrementa recognition rate per asse singola del id e del gesto; restituisce (RrId,Rrges)
		float[] buffer=new float[2];
		float RrId=0;
		float Rrges=0;
		Set<String> keys = x.keySet();
		float dimProbe=(float) keys.size();
		String matchedFileX,matchedGestoX,matchedIdX;
		for (String key: keys) {
			String id=key.split("_")[0];
			String gesto=key.split("_")[2];			
			matchedFileX=(String) x.get(key);
			matchedGestoX=matchedFileX.split("_")[2];
			matchedIdX=matchedFileX.split("_")[0];		 			
			if (gesto.equals(matchedGestoX)) {
				Rrges++;
				}
			if (id.equals(matchedIdX)) {
					RrId++;
				}				
			}	
		
		buffer[0]=RrId/dimProbe;
		buffer[1]=Rrges/dimProbe;
		return buffer;	
	}
	
}
