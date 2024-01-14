package matrix;
import algs.*;   
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

public class DtwMatrix {
	public CSVWriter writer;
	public CSVWriter writer1;
	public CSVWriter writer2;
	public CSVWriter writer3;
	public CSVReader reader;
	public CSVReader reader1;
	public CSVReader reader2;
	public File dir;
	public File[] filesList;
	public String[] bufferHeader;
	public File x;
	public File y;
	public File z;
	public File com;
	
	public DtwMatrix(String struttura) {
		if (struttura.equals("cross")) {
			x=new File("distanzaAsseX.csv");
			 y=new File("distanzaAsseY.csv");
			 z=new File("distanzaAsseZ.csv");
		}
		else {
			try {
				x=new File("distanzaAsseX.csv");
				if (!x.exists()) {
					x.createNewFile();
				}
				y=new File("distanzaAsseY.csv");
				if (!y.exists()) {
					y.createNewFile();
				}
				 z=new File("distanzaAsseZ.csv");
				if (!z.exists()) {
					z.createNewFile();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public HashMap<String,String> findUserIsMatchedInMatrixDtw(String Matrix) { //ritorna HashMap contente match tra tutti i file del probe;
		float MinDtw=Float.POSITIVE_INFINITY;
		int pos=0;;//posizione file match nella lista;
		int cont=0; // posizione file del probe nella lista;
		HashMap<String,String> map=new HashMap<String,String>();
		File file = null;
		CSVReader reader;	
		try {
		if(Matrix.equals("asseX")) {
			file=x;
		}
		if(Matrix.equals("asseY")) {
			 file=y;
		}
		if(Matrix.equals("asseZ")) {
			 file=z;
		}
		if(Matrix.equals("asseCom")) {
			 file=com;
		}		
			reader = new CSVReader(new FileReader(file));
			String[] nextLine;
		      while ((nextLine = reader.readNext()) != null){	    	  
		         if ((nextLine != null)){
		        	if (!nextLine[0].equals("fileName")) { 
		        		for (int i=1;i<nextLine.length;i++) {
		        			float dtw=Float.valueOf(nextLine[i]);		        			
		        			if ((dtw<=MinDtw) && (!nextLine[0].equals(filesList[i-1].getName()))) {	        				
		        				MinDtw=dtw;
		        				pos=i-1;
		        			}		        		
		        		}        	
		        	MinDtw=Float.POSITIVE_INFINITY;
		        	map.put(filesList[cont].getName().toString(),filesList[pos].getName().toString());
		        	pos=0;
		        	cont++;
		        	}		        	
		         }
		    }
		      reader.close();
		} catch (NumberFormatException | IOException | CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}	
public void setValori() {
	File dir=getPathDir();
	filesList = dir.listFiles();
	int i=1;
	bufferHeader=new String[filesList.length+1];
	bufferHeader[0]="fileName";
	for (File file:filesList) {
		bufferHeader[i]=file.getName();
		i++;
	}
	try {
		writer=new CSVWriter(new FileWriter(x));
		writer1=new CSVWriter(new FileWriter(y));
		writer2=new CSVWriter(new FileWriter(z));
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	writer.writeNext(bufferHeader);
	writer1.writeNext(bufferHeader);
	writer2.writeNext(bufferHeader);
	List<Float> Vettore1X = new ArrayList<>();
	List<Float> Vettore1Y = new ArrayList<>();
	List<Float> Vettore1Z = new ArrayList<>();
	List<Float> Vettore2X = new ArrayList<>();	
	List<Float> Vettore2Y = new ArrayList<>();	
	List<Float> Vettore2Z = new ArrayList<>();	//riga da inserire;	
	String[] bufferRowAsseY=new String[filesList.length+1];
	String[] bufferRowAsseX=new String[filesList.length+1];
	String[] bufferRowAsseZ=new String[filesList.length+1];
	CSVReader readerFile1;
	CSVReader readerFile2;	
	for(File file : filesList) {
		//inserisco nella riga il nome del file nel probe;
		bufferRowAsseX[0]=file.getName();
		bufferRowAsseY[0]=file.getName();
		bufferRowAsseZ[0]=file.getName();
		int cont=1;		
		try {
			readerFile1 = new CSVReader(new FileReader(file.getAbsolutePath()));
		String[] nextLine;
		//genero 3 vettori contenenti valori per ogni asse
			while ((nextLine = readerFile1.readNext()) != null) {
			     if (nextLine != null){
			    	 Vettore1X.add(Float.valueOf(nextLine[1]));
			    	 Vettore1Y.add(Float.valueOf(nextLine[2]));
			    	 Vettore1Z.add(Float.valueOf(nextLine[3]));
			     }
			     }

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//confronto con ogni file della gallery;
		
		for(File file2 : filesList) {
			try {
				readerFile2 = new CSVReader(new FileReader(file2.getAbsolutePath()));				
				if(!file.getName().equals(file2.getName())) {	
					String[] nextLine;
					while ((nextLine = readerFile2.readNext()) != null) {
						if (nextLine != null){
							Vettore2X.add(Float.valueOf(nextLine[1]));
					    	Vettore2Y.add(Float.valueOf(nextLine[2]));
					    	Vettore2Z.add(Float.valueOf(nextLine[3]));
						}
					}
					float valoreAsseX=Alg.Dtw(Vettore1X,Vettore2X);
					float valoreAsseY=Alg.Dtw(Vettore1Y,Vettore2Y);
					float valoreAsseZ=Alg.Dtw(Vettore1Z,Vettore2Z);
				//inserisco ogni valore trovata nella riga da inserire nel file;					
					bufferRowAsseX[cont]=String.valueOf(valoreAsseX);
					bufferRowAsseY[cont]=String.valueOf(valoreAsseY);
					bufferRowAsseZ[cont]=String.valueOf(valoreAsseZ);
			}
			else {				
				bufferRowAsseX[cont]="0";
				bufferRowAsseY[cont]="0";
				bufferRowAsseZ[cont]="0";
			}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CsvValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cont++;
			Vettore2X.clear();
			Vettore2Y.clear();
			Vettore2Z.clear();
		}
		writer.writeNext(bufferRowAsseX);
		writer1.writeNext(bufferRowAsseY);
		writer2.writeNext(bufferRowAsseZ);
		try {
			writer.flush();
			writer1.flush();
			writer2.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vettore1X.clear();
		Vettore1Y.clear();
		Vettore1Z.clear();
	}
	
	try {
		writer.close();
		writer1.close();
		writer2.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
}

public  File getPathDir() {
     dir = new File("files");
	return dir; 
}


public void setDistanceMatrixComb() {
	int cont=1;	
	List<String[]> csvBody;
	try {
		reader=new CSVReader(new FileReader(x));
		reader1=new CSVReader(new FileReader(y));
		reader2=new CSVReader(new FileReader(z));
		csvBody = reader1.readAll();
		List<String[]> csvBody2 = reader2.readAll();
		String[] nextLine;
		String[] row=new String[filesList.length+1];
		writer3 = new CSVWriter(new FileWriter("DistanceMatrixByAll.csv"));
		writer3.writeNext(bufferHeader);	
		writer3.flush();
		while ((nextLine = reader.readNext()) != null) {
			row[0]=nextLine[0];
			if (nextLine != null && !nextLine[0].equals("fileName")){
				for (int i=1;i<nextLine.length;i++) {
					float valore=Float.valueOf(nextLine[i]);
					String valore1=csvBody.get(cont)[i];
					String valore2=csvBody2.get(cont)[i];
					float tot=(valore+Float.valueOf(valore1)+Float.valueOf(valore2))/3;
					row[i]=String.valueOf(tot);
					}
					cont++;
					writer3.writeNext(row);
					writer3.flush();
				}		
			}
		reader.close();
		reader1.close();
		reader2.close();
		writer3.close();
	} catch (IOException | CsvException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
}


public float[] testDtwVerification() {
	float[] array=new float[8];
	 int numGiustiX=0;
	 int numErratiX=0;
	 int numGiustiY=0;
	 int numErratiY=0;
	 int numGiustiZ=0;
	 int numErratiZ=0;
	 int numGiustiCom=0;
	 int numErratiCom=0;
		int tot=0;
		float costante=(float) 0.50;
		for (int file=0;file<filesList.length;file++) {
			for (int x=0;x<filesList.length/18;x++) {
			 boolean valX=MatchWithsubsets(file,x,"asseX");
				 if(valX) {
					 numGiustiX++;
				 }
				 else {
					 numErratiX++;
				 }
			
			 boolean valY=MatchWithsubsets(file,x,"asseY");
				 if(valY) {
					 numGiustiY++;
				 }
				 else {
					 numErratiY++;
				 }
			 
			 boolean valZ=MatchWithsubsets(file,x,"asseZ");
				 if(valZ) {
					 numGiustiZ++;
				 }
				 else {
					 numErratiZ++;
				 }
			 
			boolean valCom=MatchWithsubsets(file,x,"Com");
				 if(valCom) {
					 numGiustiCom++;
				 }
				 else {
					 numErratiCom++;
				 }
				 tot++;
			 }
			}
		
		numGiustiX=numGiustiX/tot;
		numGiustiY=numGiustiY/tot;
		numGiustiZ=numGiustiZ/tot;
		numGiustiCom=numGiustiCom/tot;
		numErratiX=numErratiX/tot;
		numErratiY=numErratiY/tot;
		numErratiZ=numErratiZ/tot;
		numErratiCom=numErratiCom/tot;
		array[0]=numGiustiX;
		array[1]=numErratiX;
		array[2]=numGiustiY;
		array[3]=numErratiY;
		array[4]=numGiustiZ;
		array[5]=numErratiZ;
		array[6]=numGiustiCom;
		array[7]=numErratiCom;
		return array;
}

public boolean MatchWithsubsets(int file,int id,String asse) {
	List<Integer> listaFileForId=CreaListaVerification(id);
	
	boolean valore=confrontaForIdentification(id,file,listaFileForId,asse);
	return valore;
	}
	
	


private boolean confrontaForIdentification(int idsubset,int file, List<Integer> listaFileForId, String asse) {
	// TODO Auto-generated method stub
	int id=Integer.valueOf(filesList[file].getName().split("_")[0]);
	float costante=(float) 1.36;
	int pos=-1;
	float min=Float.POSITIVE_INFINITY;
	for (int x=0;x<listaFileForId.size();x++) {
		float valore=trova(file,x,asse);
		if(valore<min) {
			min=valore;
			pos=x;
		}
	}
	if(min<costante && filesList[pos].getName().split("_")[0].equals(filesList[file].getName().split("_")[0])) {
		return true;
	}
	else if(min>costante && !(id==idsubset)) {
		return true;
	}
	
	else if(min<costante && !filesList[pos].getName().split("_")[0].equals(filesList[file].getName().split("_")[0])) {
		return false;
	}
	else if(min>costante && (id==idsubset)) {
		return false;
	}
	else {
		return false;
	}
}

public float trova(int posP,int posG,String asse) {
	
	CSVReader reader = null;
	if (asse.equals("asseX")){
		try {
			reader=new CSVReader(new FileReader(x));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	else if (asse.equals("asseY")){
		try {
			reader=new CSVReader(new FileReader(y));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	else if (asse.equals("asseZ")){
		try {
			reader=new CSVReader(new FileReader(z));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	float valore = 0;
	try {
		List<String[]> l=reader.readAll();
		valore=Float.valueOf(l.get(posP+1)[posG+1]);
		reader.close();
	} catch (IOException | CsvException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return valore;
}

public List<Integer> CreaListaVerification(int id) {
	List<Integer> listaFileForId=new ArrayList<Integer>();
	// TODO Auto-generated method stub
	for (int x=0;x<filesList.length;x++) {
		if (filesList[x].getName().split("_")[0].equals(String.valueOf(id))) {
			listaFileForId.add(x);
		}
	}
	return listaFileForId;
}

}
		


