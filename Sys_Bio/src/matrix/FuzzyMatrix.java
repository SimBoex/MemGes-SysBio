package matrix;
import com.opencsv.CSVReader;      
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.util.Arrays.*;

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class FuzzyMatrix {
	public CSVWriter writer;
	public CSVWriter writer1;
	public CSVWriter writer2;
	public CSVWriter writer3;
	public CSVReader reader3;

	public CSVReader reader;
	public CSVReader reader1;
	public CSVReader reader2;
	public File dir;
	public File comb;
	public File x;
	public File y;
	public File z;
	public File[] filesList;
	public String[] bufferHeader;
	public String s="Nointer";	
	public FuzzyMatrix(String struttura,boolean inter) throws IOException {
		String s;
		if (inter) {
			s="WithInter";
		}
		else {
			s="WithoutInter";
		}
		if (struttura.equals("cross")) {
				comb=new File("distanzaFuzzyComb"+s+".csv");
				 x=new File("distanzaFuzzyAsseX"+s+".csv");
				 y=new File("distanzaFuzzyAsseY"+s+".csv");
				z=new File("distanzaFuzzyAsseZ"+s+".csv");
						
			File dir=getPathDir();
			filesList = dir.listFiles();
						
		}
		else {			
				comb=new File("distanzaFuzzyComb"+s+".csv");
				if (!comb.exists()) {
					comb.createNewFile();
				}
				x=new File("distanzaFuzzyAsseX"+s+".csv");
					if (!x.exists()) {
						x.createNewFile();
					}
				y=new File("distanzaFuzzyAsseY"+s+".csv");
					if (!y.exists()) {
						y.createNewFile();
					}
				 z=new File("distanzaFuzzyAsseZ"+s+".csv");
					if (!z.exists()) {
						z.createNewFile();
					}						
			}
	}
		
	public  File getPathDir() {
	     dir = new File("files");
		return dir; 
	}

	public void setValori(boolean inter) {
		try {
			writer=new CSVWriter(new FileWriter(x));
			writer1=new CSVWriter(new FileWriter(x));
			writer2=new CSVWriter(new FileWriter(x));

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		File dir=getPathDir();
		filesList = dir.listFiles();
		int i=1;
		bufferHeader=new String[filesList.length+1];
		bufferHeader[0]="fileName";
		for (File file:filesList) {
			bufferHeader[i]=file.getName();
			i++;
		}
		writer.writeNext(bufferHeader);
		writer1.writeNext(bufferHeader);
		writer2.writeNext(bufferHeader);
		
		try {
			writer.flush();
			writer1.flush();
			writer2.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
			readerFile1.close();
			int dim=Vettore1X.size();
			while (dim<300) {
				Vettore1X.add((float) 0);
				Vettore1Y.add((float) 0);
				Vettore1Z.add((float) 0) ;
				dim++;
			}	
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//confronto con ogni file della gallery;
 catch (CsvValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			for(File file2 : filesList) {
				try {
					readerFile2 = new CSVReader(new FileReader(file2.getAbsolutePath()));				
					if(!file.getName().equals(file2.getName())  ) {	
						String[] nextLine;
						while ((nextLine = readerFile2.readNext()) != null) {
							if (nextLine != null){
								Vettore2X.add(Float.valueOf(nextLine[1]));
						    	Vettore2Y.add(Float.valueOf(nextLine[2]));
						    	Vettore2Z.add(Float.valueOf(nextLine[3]));
							}
						}
						readerFile2.close();
						int dim=Vettore2X.size();
						while (dim<300) {
							Vettore2X.add((float) 0);
							Vettore2Y.add((float) 0);
							Vettore2Z.add((float) 0) ;
							dim++;
						}	
						float valoreAsseX=(float)algs.Alg.AlgFuzzyFunction(Vettore1X,Vettore2X,inter);
						float valoreAsseY=(float) algs.Alg.AlgFuzzyFunction(Vettore1Y,Vettore2Y,inter);
						float valoreAsseZ=algs.Alg.AlgFuzzyFunction(Vettore2Z,Vettore1Z,inter);
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
	public HashMap<String,String> findUserIsMatchedInMatrixFuzzy(String Matrix) { //ritorna HashMap contente match tra tutti i file del probe;
		float MinDis=Float.POSITIVE_INFINITY;
		int pos=0;;//posizione file match nella lista;
		int cont=0; // posizione file del probe nella lista;
		HashMap<String,String> map=new HashMap<String,String>();
		CSVReader reader;		
		try {
			reader = new CSVReader(new FileReader(Matrix));	
			String[] nextLine;		
		      while ((nextLine = reader.readNext()) != null){		    	  
		         if ((nextLine != null)){		        	 
		        	if (!nextLine[0].equals("fileName")) { 
		        		for (int i=1;i<nextLine.length;i++) {
		        			float value=Float.valueOf(nextLine[i]);
		        			if ((value<=MinDis) && (!nextLine[0].equals(filesList[i-1].getName()))) {
		        				MinDis=value;
		        				pos=i-1;
		        			}		        		
		        		}		        	
		        	MinDis=Float.POSITIVE_INFINITY;
		        	map.put(filesList[cont].getName().toString(),filesList[pos].getName().toString());
		        	pos=0;
		        	cont++;
		        	}
		         }
		        
		    }
		      reader.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return map;
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
			writer3 = new CSVWriter(new FileWriter(comb));
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}	
	
	public ArrayList<String> CreaLista() {
		ArrayList<String> lista=new ArrayList<String>();
		for (int id=0;id<filesList.length/18;id++) {
			ArrayList<String> cerchio=new ArrayList<String>();
			ArrayList<String> rettangolo=new ArrayList<String>();
			for (int pos=0;pos<filesList.length;pos++) {
				String nameFile=filesList[pos].getName();
				String id1=nameFile.split("_")[0];
				String figura=nameFile.split("_")[2];
				if (id1.equals(String.valueOf(id))) {
					if (figura.equals("cerchio")) {
						cerchio.add(String.valueOf(pos));
					}
					else {
						rettangolo.add(String.valueOf(pos));
					}
				}			
			}
			List<String> subsets=creaGruppi(cerchio);
			List<String> subsets2=creaGruppi(rettangolo);
			lista.addAll(subsets);
			lista.addAll(subsets2);
			subsets2.clear();
			subsets.clear();
			cerchio.clear();
			rettangolo.clear();
		}
		return (ArrayList<String>) lista;
	}
	
	public ArrayList<String> CreaLista(Pattern regexGallery) {
		ArrayList<String> lista=new ArrayList<String>();
		for (int id=0;id<filesList.length/18;id++) {
			ArrayList<String> cerchio=new ArrayList<String>();
			ArrayList<String> rettangolo=new ArrayList<String>();
			for (int pos=0;pos<filesList.length;pos++) {
				String nameFile=filesList[pos].getName();
				String id1=nameFile.split("_")[0];
				String figura=nameFile.split("_")[2];
				if (id1.equals(String.valueOf(id)) ){
					java.util.regex.Matcher d = regexGallery.matcher(filesList[pos].getName().toString());
					boolean checkGallery = d.matches(); 
					if (checkGallery) {
						if (figura.equals("cerchio")) {
						cerchio.add(String.valueOf(pos));
						}
						else {
							rettangolo.add(String.valueOf(pos));
						}
					}
				}
				
			}
			List<String> subsets=creaGruppi(cerchio);
			List<String> subsets2=creaGruppi(rettangolo);
			lista.addAll(subsets);
			lista.addAll(subsets2);
			subsets2.clear();
			subsets.clear();
			cerchio.clear();
			rettangolo.clear();	
		}
		return (ArrayList<String>) lista;
	}
	
	
	public ArrayList<String> creaGruppi(ArrayList<String> lista){
		ArrayList<String> Newlista=new ArrayList<String>();
		Newlista.addAll(creaSottoGruppi(lista,"",0,lista.size(),3));
		return Newlista;
		
	}
		
	public float[] trovaRate(boolean inter) {
		float[] buffer=new float[2];
		float RrId=0;
		float RrGes=0;
		ArrayList<String> listaSubset=CreaLista();
		for (int x=0;x<filesList.length;x++) {
			String MatchedSubset=confronta(String.valueOf(x),listaSubset);
			if(!MatchedSubset.equals("noMatched")) {							
				String id=filesList[Integer.valueOf(MatchedSubset.split(" ")[0])].getName().split("_")[0];
				String gesto=filesList[Integer.valueOf(MatchedSubset.split(" ")[0])].getName().split("_")[2];
				if (id.equals(filesList[x].getName().split("_")[0])) {
					RrId++;
				}
				if (gesto.equals(filesList[x].getName().split("_")[2])) {
					RrGes++;
				}
			}
		}
		RrId=RrId/(float)filesList.length;
		RrGes=RrGes/(float)filesList.length;
		buffer[0]=RrId;
		buffer[1]=RrGes;
	return buffer;
	}
	
	public float[] trovaRateCross(Pattern regexProbe,Pattern regexGallery) {
		int contProbe=0;
		float[] buffer=new float[2];
		float RrId=0;
		float RrGes=0;
		ArrayList<String> listaSubset=CreaLista(regexGallery);
		for (int x=0;x<filesList.length;x++) {
			java.util.regex.Matcher d = regexProbe.matcher(filesList[x].getName().toString());
			boolean checkProbe = d.matches(); 
			if (checkProbe) {
				contProbe++;
				String MatchedSubset=confronta(String.valueOf(x),listaSubset);
					
					
					if(!MatchedSubset.equals("noMatched")) {
					String id=filesList[Integer.valueOf(MatchedSubset.split(" ")[0])].getName().split("_")[0];
					String gesto=filesList[Integer.valueOf(MatchedSubset.split(" ")[0])].getName().split("_")[2];
					if (id.equals(filesList[x].getName().split("_")[0])) {
						RrId++;
					}
					if (gesto.equals(filesList[x].getName().split("_")[2])) {
						RrGes++;
					}
					}
					
					}
			}
			
			
		RrId=RrId/(float)contProbe;
		RrGes=RrGes/(float)contProbe;
		buffer[0]=RrId;
		buffer[1]=RrGes;
	return buffer;
	}
	
	public   String confronta(String fileProbe,ArrayList<String> listaSubset) {
		float old=Float.POSITIVE_INFINITY;
		int pos=-1;
		for (int x=0;x<listaSubset.size();x++) {
			String set=listaSubset.get(x);
			String[] array=set.split(" ");
			String file1=array[0];
			String file2=array[1];
			String file3=array[2];
			
			if(Integer.valueOf(file1)==Integer.valueOf(fileProbe)) {
				float distP2=distanza(Integer.valueOf(fileProbe),Integer.valueOf(file2));
				float distP3=distanza(Integer.valueOf(fileProbe),Integer.valueOf(file3));
				float totDistP=(distP2+distP3)/2;
				//float dist12=distanza(Integer.valueOf(file1),Integer.valueOf(file2),inter);
				//float dist13=distanza(Integer.valueOf(file1),Integer.valueOf(file3),inter);
				float dist23=distanza(Integer.valueOf(file2),Integer.valueOf(file3));
				//float totDist123=dist12+dist13+dist23/3;
				float totDist123=dist23;
				float rapporto=totDistP/totDist123;
					if (rapporto<old ) {
						old=rapporto;
						pos=x;
					}
				
			}
			else if(Integer.valueOf(file2)==Integer.valueOf(fileProbe)) {
				float distP1=distanza(Integer.valueOf(fileProbe),Integer.valueOf(file1));
				float distP3=distanza(Integer.valueOf(fileProbe),Integer.valueOf(file3));
				float totDistP=(distP1+distP3)/2;
				//float dist12=distanza(Integer.valueOf(file1),Integer.valueOf(file2),inter);
				float dist13=distanza(Integer.valueOf(file1),Integer.valueOf(file3));
				//float dist23=distanza(Integer.valueOf(file2),Integer.valueOf(file3),inter);
				//float totDist123=dist12+dist13+dist23/3;
				float totDist123=dist13;
				float rapporto=totDistP/totDist123;				
					if(rapporto<old ) {
						old=rapporto;
						pos=x;
					}				
			}
			else if(Integer.valueOf(file3)==Integer.valueOf(fileProbe)) {
				float distP2=distanza(Integer.valueOf(fileProbe),Integer.valueOf(file2));
				float distP1=distanza(Integer.valueOf(fileProbe),Integer.valueOf(file1));
				float totDistP=(distP1+distP2)/2;
				float dist12=distanza(Integer.valueOf(file1),Integer.valueOf(file2));
				//float dist13=distanza(Integer.valueOf(file1),Integer.valueOf(file3),inter);
				//float dist23=distanza(Integer.valueOf(file2),Integer.valueOf(file3),inter);
				float totDist123=dist12;
				//float totDist123=dist12+dist13+dist23/3;
				float rapporto=totDistP/totDist123;
				
					if(rapporto<old ) {
						old=rapporto;
						pos=x;
					
				}
			}
			else {
				
				float distP1=distanza(Integer.valueOf(fileProbe),Integer.valueOf(file1));				
				float distP2=distanza(Integer.valueOf(fileProbe),Integer.valueOf(file2));
				float distP3=distanza(Integer.valueOf(fileProbe),Integer.valueOf(file3));
				
				
				float totDistP=(distP1+distP2+distP3)/3;
				float dist12=distanza(Integer.valueOf(file1),Integer.valueOf(file2));
				float dist13=distanza(Integer.valueOf(file1),Integer.valueOf(file3));
				float dist23=distanza(Integer.valueOf(file2),Integer.valueOf(file3));				
				float totDist123=(dist12+dist13+dist23)/3;
				float rapporto=totDistP/totDist123;
				
					if(rapporto<old ) {
						old=rapporto;
						pos=x;
					}
					
				}
			}
		if(pos==-1) {
			System.out.println("non sono matched");
			System.out.println(filesList[Integer.valueOf(fileProbe)]);
			return "noMatched";
		}
		return listaSubset.get(pos);
	}
	
	public float distanza(int fileProbe,int fileGallery) {
		
		float X=trova(fileProbe,fileGallery,"asseX");
		float Y=trova(fileProbe,fileGallery,"asseY");
		float Z=trova(fileProbe,fileGallery,"asseZ");
		
		
		
		float res=(X+Y+Z)/3;
		return res;
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
	
	
	 public static   ArrayList<String> creaSottoGruppi(ArrayList<String> A, String out, int i, int n, int k){
	        // invalid input
		 
	 
	        // base case: combination size is `k`
	        if (k == 0)
	        {	
	        	ArrayList<String> l1=new ArrayList<String>();
	            l1.add(out);
	            return l1;
	        }
	        ArrayList<String> l1=new ArrayList<String>();
	        // start from the next index till the last index
	        for (int j = i; j < n; j++)
	        {
	            // add current element `A[j]` to the solution and recur for next index
	            // `j+1` with one less element `k-1`
	        	l1.addAll(creaSottoGruppi(A, out  + A.get(j)+" " , j + 1, n, k - 1));
	        }
	        return l1;
	 }
	 
	 
	 public float[] testFuzzy() {
		float[] array=new float[2];
		 int tot=0;
		 int numGiusti=0;
		 int numErrati=0;
		 for(int file=0;file<filesList.length;file++) {
			 for (int id=0;id<filesList.length/18;id++) {
				 boolean val=matchWithSubset(file,id);
				 if (val) {
					 numGiusti++;
				 }
				 else {
					 numErrati++;
				 }
				 tot++;
			 }
		 }
		 numGiusti=numGiusti/3;
		 numErrati=numErrati/3;
		 array[0]=numGiusti;
		 array[1]=numErrati;
		 return array;
	 }
	 private boolean matchWithSubset(int file, int id) {
		// TODO Auto-generated method stub
		 ArrayList<String> lista=CreaListaVerification( id);// da modificare
		 boolean valore=confrontaForIdentification(id,file,lista);
		return valore;
	}

	private boolean confrontaForIdentification(int idsubset, int file, ArrayList<String> lista) {
		// TODO Auto-generated method stub
		int idProbe=Integer.valueOf(filesList[file].getName().split("_")[0]);
		float costante=(float) 1.35;
		int pos=-1;
		float min=Float.POSITIVE_INFINITY;
		for (int x=0;x<lista.size();x++) {
			
			
			String set=lista.get(x);
			String[] array=set.split(" ");
			String file1=array[0];
			String file2=array[1];
			String file3=array[2];
			
			if(Integer.valueOf(file1)==idProbe) {
				float distP2=distanza(idProbe,Integer.valueOf(file2));
				float distP3=distanza(idProbe,Integer.valueOf(file3));
				float totDistP=(distP2+distP3)/2;
				//float dist12=distanza(Integer.valueOf(file1),Integer.valueOf(file2),inter);
				//float dist13=distanza(Integer.valueOf(file1),Integer.valueOf(file3),inter);
				float dist23=distanza(Integer.valueOf(file2),Integer.valueOf(file3));
				//float totDist123=dist12+dist13+dist23/3;
				float totDist123=dist23;
				float valore=totDistP/totDist123;
					if (valore<costante) {
						min=valore;
						pos=x;
					}
				
			}
			else if(Integer.valueOf(file2)==idProbe) {
				float distP1=distanza(idProbe,Integer.valueOf(file1));
				float distP3=distanza(idProbe,Integer.valueOf(file3));
				float totDistP=(distP1+distP3)/2;
				//float dist12=distanza(Integer.valueOf(file1),Integer.valueOf(file2),inter);
				float dist13=distanza(Integer.valueOf(file1),Integer.valueOf(file3));
				//float dist23=distanza(Integer.valueOf(file2),Integer.valueOf(file3),inter);
				//float totDist123=dist12+dist13+dist23/3;
				float totDist123=dist13;
				float valore=totDistP/totDist123;				
					if(valore<costante) {
						min=valore;
						pos=x;
					}				
			}
			else if(Integer.valueOf(file3)==idProbe) {
				float distP2=distanza(idProbe,Integer.valueOf(file2));
				float distP1=distanza(idProbe,Integer.valueOf(file1));
				float totDistP=(distP1+distP2)/2;
				float dist12=distanza(Integer.valueOf(file1),Integer.valueOf(file2));
				//float dist13=distanza(Integer.valueOf(file1),Integer.valueOf(file3),inter);
				//float dist23=distanza(Integer.valueOf(file2),Integer.valueOf(file3),inter);
				float totDist123=dist12;
				//float totDist123=dist12+dist13+dist23/3;
				float valore=totDistP/totDist123;			
					if(valore<costante ) {
						min=valore;
						pos=x;					
				}
			}
			else {			
			float valoreP1=distanza(file,Integer.valueOf(file1));
			float valoreP2=distanza(file,Integer.valueOf(file2));
			float valoreP3=distanza(file,Integer.valueOf(file3));
			float DistProbe=(valoreP1+valoreP2+valoreP3)/3;
			
			float valore12=distanza(Integer.valueOf(file1),Integer.valueOf(file2));
			float valore13=distanza(Integer.valueOf(file1),Integer.valueOf(file3));
			float valore23=distanza(Integer.valueOf(file2),Integer.valueOf(file3));
			float DistGallery=(valore12+valore13+valore23)/3;
			
			float valore=DistProbe/DistGallery;
			if(valore<costante) {
				min=valore;
				pos=x;
			}
			}
		}
		if(min<costante && filesList[pos].getName().split("_")[0].equals(filesList[file].getName().split("_")[0])) {
			return true;
		}
		else if((pos==-1) && !(idProbe==idsubset)) {
			return true;
		}
		
		else if(min<costante && !filesList[pos].getName().split("_")[0].equals(filesList[file].getName().split("_")[0])) {
			return false;
		}
		else if((pos==-1) && (idProbe==idsubset)) {
			return false;
		}
		else {
			return false;
		}
		
	}

	public ArrayList<String> CreaListaVerification(int id) {
			ArrayList<String> listaFileForId=new ArrayList<String>();
			// TODO Auto-generated method stub
			for (int x=0;x<filesList.length;x++) {
				if (filesList[x].getName().split("_")[0].equals(String.valueOf(id))) {
					listaFileForId.add(String.valueOf(x));
				}
			}
			ArrayList<String> lista=creaSottoGruppi(listaFileForId,"",0,listaFileForId.size(),3);
			return listaFileForId;
		}
	
	
	
	public ArrayList<Float> interpolationArray(ArrayList<Float> vettore){		
		ArrayList<Float> vettoreFinale=new ArrayList<Float>();
		double[] vettored=new double[vettore.size()];
		double[] vettoreIndexd=new double[vettore.size()];

		for(int x=0;x<vettore.size();x++) {
			float value=vettore.get(x);
			double valuef=Double.valueOf(value);
			double valueIndex=Double.valueOf(x);
			vettored[x]=valuef;
			vettoreIndexd[x]=valueIndex;
		}
		    LinearInterpolator li = new LinearInterpolator();
		    PolynomialSplineFunction psf = li.interpolate(vettored, vettoreIndexd);
		    
		    double step=((double)vettore.size()-1)/(double)10;
		    System.out.println("step is "+step);
		    vettoreFinale.add((float)1);
		    double inizio=1+step;
		    while((float)psf.value(inizio)!=vettore.get(vettore.size()-1)) {
		    	vettoreFinale.add((float)psf.value(inizio));
		    	System.out.println(vettoreFinale.toString());
		    	inizio=inizio+step;
		    	System.out.println("ultimo valore "+vettore.get(vettore.size()-1));
		    	System.out.println("inizio "+inizio);

		    }
		    vettoreFinale.add( vettore.get(vettore.size()-1));
		return vettoreFinale;
		
	}
}



