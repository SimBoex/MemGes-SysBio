package matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import algs.Alg;

public class CreaMatrixFuzzy {
	private boolean inter;
	private File dir;
	private File[] filesList;
	public CSVWriter writer;
	public CSVReader reader;
	public String[] listaTemplate;
	public ArrayList<String> listaProbe;
	public File matrice;
	
	public CreaMatrixFuzzy(boolean inter,String tipo) {
		getPathDir();
		String[] regex=new String[2];
		regex[0]="(.+_[32]_cerchio_[012]\\.csv)|(.+_[32]_rettangolo_[012]\\.csv)";
		regex[1]="_[1]_cerchio_[012]\\.csv _[1]_rettangolo_[012]\\.csv";	
		listaTemplate=CreaListaTemplate(regex[1],tipo);
		listaProbe=TrovaFileProbe(Pattern.compile(regex[0]));
		this.inter=inter;	
		CreaFile(inter,tipo);
		HashMap<String,Double> map=trovaMap();
		ScriviSvUt(map,inter);
	}
	
	public ArrayList<String> TrovaFileProbe(Pattern regex) {
		ArrayList<String> lista=new ArrayList<String>();	
		for (int x=0;x<filesList.length;x++) {
			String nomeFile=filesList[Integer.valueOf(x)].getName();
			java.util.regex.Matcher d = regex.matcher(nomeFile);
			boolean checkGallery = d.matches(); 
			if (checkGallery) {
				lista.add(String.valueOf(x));
			}
		}	
		
		return lista;
	}
	public String TrovaTemplateGallery(int id,Pattern regex) {
		String TemplateSessione="";	
		for (int x=0;x<filesList.length;x++) {
			String nomeFile=filesList[Integer.valueOf(x)].getName();
			
			java.util.regex.Matcher d = regex.matcher(nomeFile);
			boolean checkGallery = d.matches(); 
			if (checkGallery) {
				TemplateSessione=TemplateSessione+x+" ";
			}
		}
		
		return TemplateSessione;
	}
	private String[] CreaListaTemplate(String regex,String tipo) {
		// TODO Auto-generated method stub
		String[] listaTemplate=new String[21];
		String r1 = null;
		String r2 = null;
		if(tipo.equals("1a")) {
		 r1=regex.split(" ")[0];
		 r2=regex.split(" ")[1];
		}
		else {
			r2=regex.split(" ")[0];
			r1=regex.split(" ")[1];
		}
		int[] i=new int[21];
		i[0]=0;
		i[1]=1;
		i[2]=2;
		i[3]=3;
		i[4]=4;
		i[5]=5;
		i[6]=6;
		i[7]=7;
		i[8]=8;
		i[9]=9;
		i[10]=10;
		i[11]=11;
		i[12]=12;
		i[13]=13;
		i[14]=14;
		i[15]=15;
		i[16]=16;
		i[17]=17;
		i[18]=18;
		i[19]=19;
		i[20]=20;
		
		int meta1=i.length/2;	
		
		for(int id=0;id<i.length;id++) {
			
			Pattern re;
			if(id<meta1) {
				System.out.println("id "+i[id]);
				String file=String.valueOf(i[id])+r1;
				 re=Pattern.compile(file);
			}
			else {
				System.out.println("id "+i[id]);
				String file=String.valueOf(i[id])+r2;
				 re=Pattern.compile(file);
			}
			String template=TrovaTemplateGallery(i[id],re);
			
			listaTemplate[id]=template;
		}
		
		return listaTemplate;
	}
	public HashMap<String,Double> trovaMap() {
		HashMap<String, Double> map=new HashMap<String,Double>();
		for(int temp=0;temp<listaTemplate.length;temp++) {
			double valore=trovaUt(listaTemplate[temp],inter);
			map.put(listaTemplate[temp], valore);
		}
		return map;
	}
	public  void getPathDir() {
	    this.dir = new File("files");
		this.filesList = dir.listFiles(); 
	}
	public void CreaFile(boolean inter,String tipo) {
		String s;
		if (inter) {
			s="WithInter";
		}
		else {
			s="WithoutInter";
		}
		matrice=new File("MatrixFuzzy"+"_"+s+"_"+tipo+"_"+".csv");
		if (!matrice.exists()) {
			try {
				matrice.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void ScriviSvUt(HashMap<String, Double> map,boolean inter) {
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(matrice));
		
		String[] row=new String[map.keySet().size()+1];
		int cont=0;
		row[cont]="";
		cont++;
		for(String c:map.keySet()) {
			

			String numFile1=c.split(" ")[0];
			String nomefile1=filesList[Integer.valueOf(numFile1)].getName();
			String id=nomefile1.split("_")[0];
			String sessione=nomefile1.split("_")[1];
			String figura=nomefile1.split("_")[2];
			String template=id+"_"+sessione+"_"+figura;
			row[cont]=template;
			cont++;
		}
		writer.writeNext(row);
		try {
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cont=1;
		
		for (int x=0;x<listaProbe.size();x++) {
			row=new String[map.keySet().size()+1];
			row[0]=filesList[Integer.valueOf(listaProbe.get(x))].getName();
			for (String c:map.keySet()) {
				String[] l=c.split(" ");
				String file1=l[0];
				String file2=l[1];
				String file3=l[2];
				double valore=trovaSv(Integer.valueOf(listaProbe.get(x)),file1,file2,file3,inter);
				double ut=map.get(c);
				double rapporto=valore/ut;
				row[cont]=String.valueOf(rapporto);
				cont++;
			}
			writer.writeNext(row);
			try {
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cont=1;
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}
	
	
	private Double trovaSv(int x, String file1, String file2, String file3,boolean inter) {
		// TODO Auto-generated method stub
		double Sv=0;
		
		ArrayList<Double> PX=new ArrayList<Double>();
		ArrayList<Double> PY=new ArrayList<Double>();
		ArrayList<Double> PZ=new ArrayList<Double>();
		ArrayList<Double> G1X=new ArrayList<Double>();
		ArrayList<Double> G1Y=new ArrayList<Double>();
		ArrayList<Double> G1Z=new ArrayList<Double>();
		ArrayList<Double> G2X=new ArrayList<Double>();
		ArrayList<Double> G2Y=new ArrayList<Double>();
		ArrayList<Double> G2Z=new ArrayList<Double>();
		ArrayList<Double> G3X=new ArrayList<Double>();
		ArrayList<Double> G3Y=new ArrayList<Double>();
		ArrayList<Double> G3Z=new ArrayList<Double>();
		
		InserisciValori(x,PX,PY,PZ);
		InserisciValori(Integer.valueOf(file1),G1X,G1Y,G1Z);
		InserisciValori(Integer.valueOf(file2),G2X,G2Y,G2Z);
		InserisciValori(Integer.valueOf(file3),G3X,G3Y,G3Z);
		
		double distPG1X=Alg.AlgFuzzyFunction(PX, G1X, inter);
		double distPG1Y=Alg.AlgFuzzyFunction(PY, G1Y, inter);
		double distPG1Z=Alg.AlgFuzzyFunction(PZ, G1Z, inter);
		double distPG1=(distPG1X+distPG1Y+distPG1Z)/3;
		
		double distPG2X=Alg.AlgFuzzyFunction(PX, G2X, inter);
		double distPG2Y=Alg.AlgFuzzyFunction(PY, G2Y, inter);
		double distPG2Z=Alg.AlgFuzzyFunction(PZ, G2Z, inter);
		double distPG2=(distPG2X+distPG2Y+distPG2Z)/3;
		
		double distPG3X=Alg.AlgFuzzyFunction(PX, G3X, inter);
		double distPG3Y=Alg.AlgFuzzyFunction(PY, G3Y, inter);
		double distPG3Z=Alg.AlgFuzzyFunction(PZ, G3Z, inter);
		double distPG3=(distPG3X+distPG3Y+distPG3Z)/3;
		
	
		Sv= (distPG1+distPG2+distPG3)/3;
		return Sv;
	}


	private void InserisciValori(int x, ArrayList<Double> pX, ArrayList<Double> pY, ArrayList<Double> pZ) {
		// TODO Auto-generated method stub
		CSVReader readerFile1;
		try {
			readerFile1 = new CSVReader(new FileReader(filesList[x].getAbsolutePath()));
		
		String[] nextLine;
			while ((nextLine = readerFile1.readNext()) != null) {
			     if (nextLine != null){
			    	pX.add(Double.valueOf(nextLine[1]));
			    	 pY.add(Double.valueOf(nextLine[2]));
			    	 pZ.add(Double.valueOf(nextLine[3]));	    
			     }
			     }
		readerFile1.close();
		
		int dim=pX.size();
		while (dim<300) {
			pX.add((double) 0);
			pY.add((double) 0);
			pZ.add((double) 0) ;
			dim++;
		}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public double trovaUt(String template,boolean inter) {
		
		double ut=(double) 0.0;
		String[] l=template.split(" ");
		String file1=l[0];
		String file2=l[1];
		String file3=l[2];
		
		ArrayList<Double> G1X=new ArrayList<Double>();
		ArrayList<Double> G1Y=new ArrayList<Double>();
		ArrayList<Double> G1Z=new ArrayList<Double>();
		ArrayList<Double> G2X=new ArrayList<Double>();
		ArrayList<Double> G2Y=new ArrayList<Double>();
		ArrayList<Double> G2Z=new ArrayList<Double>();
		ArrayList<Double> G3X=new ArrayList<Double>();
		ArrayList<Double> G3Y=new ArrayList<Double>();
		ArrayList<Double> G3Z=new ArrayList<Double>();
		
		InserisciValori(Integer.valueOf(file1),G1X,G1Y,G1Z);
		InserisciValori(Integer.valueOf(file2),G2X,G2Y,G2Z);
		InserisciValori(Integer.valueOf(file3),G3X,G3Y,G3Z);
		
		double distG1G2X=Alg.AlgFuzzyFunction(G1X, G2X, inter);
		double distG1G2Y=Alg.AlgFuzzyFunction(G1Y, G2Y, inter);
		double distG1G2Z=Alg.AlgFuzzyFunction(G1Z, G2Z, inter);
		double distG1G2=(distG1G2X+distG1G2Y+distG1G2Z)/3;
		
		double distG1G3X=Alg.AlgFuzzyFunction(G1X, G3X, inter);
		double distG1G3Y=Alg.AlgFuzzyFunction(G1Y, G3Y, inter);
		double distG1G3Z=Alg.AlgFuzzyFunction(G1Z, G3Z, inter);
		double distG1G3=(distG1G3X+distG1G3Y+distG1G3Z)/3;
		
		double distG2G3X=Alg.AlgFuzzyFunction(G2X, G3X, inter);
		double distG2G3Y=Alg.AlgFuzzyFunction(G2Y, G3Y, inter);
		double distG2G3Z=Alg.AlgFuzzyFunction(G2Z, G3Z, inter);
		double distG2G3=(distG2G3X+distG2G3Y+distG2G3Z)/3;
		
		ut= (distG1G2+distG1G3+distG2G3)/3;
		return ut;
	}
	

	

	
}
