package matrix;

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import algs.Alg;

public class CreaMatrixDtw {
	private File dir;
	private File[] filesList;
	public CSVWriter writer;
	public CSVReader reader;
	public File matrice;
	public String[] listaTemplate;// indici del filesList
	public ArrayList<String> listaProbe;//indici del filesList
	
	public CreaMatrixDtw(String tipo,String asse) {
		String[] regex=new String[2];
		regex[0]="(.+_[32]_cerchio_[012]\\.csv)|(.+_[32]_rettangolo_[012]\\.csv)";
		regex[1]="_[1]_cerchio_[012]\\.csv _[1]_rettangolo_[012]\\.csv";	
		getPathDir();
		listaTemplate=CreaListaTemplate(regex[1],tipo);
		System.out.println("dimListaTemplate "+listaTemplate.length);
		System.out.println("lung filesList "+filesList.length);
		listaProbe=TrovaFileProbe(Pattern.compile(regex[0]));
		System.out.println("listaProbe "+listaProbe.size());
		CreaFile(tipo);
		scriviValCom(asse);
	}
	
	

	public  void getPathDir() {
	    this.dir = new File("files");
		this.filesList = dir.listFiles(); 
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
		
		
		
		for(int id=0;id<i.length;id++) {
			Pattern re;
			
			int meta1=i.length/2;
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
	public void CreaFile(String tipo) {
		matrice=new File("MatrixDtw"+"_"+tipo+"_"+".csv");
		if (!matrice.exists()) {
			try {
				matrice.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	public void scriviValCom(String asse) {
		
		CSVWriter writer;
		try {
			writer = new CSVWriter(new FileWriter(matrice));
		
		String[] row=new String[listaTemplate.length+1];
		
		row[0]="";
	
		for(int cont=1;cont<listaTemplate.length+1;cont++) {
			String nomeTemplate="";
			String nomeFile1=filesList[Integer.valueOf(listaTemplate[cont-1].split(" ")[0])].getName();
			String id=nomeFile1.split("_")[0];
			String gesture=nomeFile1.split("_")[2];
			String sessione=nomeFile1.split("_")[1];
			nomeTemplate=id+"_"+sessione+"_"+gesture;
			row[cont]=nomeTemplate;
		}
		writer.writeNext(row);
		try {
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int cont=1;
		
		for (int x=0;x<listaProbe.size();x++) {
			row=new String[listaTemplate.length+1];
			row[0]=filesList[Integer.valueOf(listaProbe.get(x))].getName();
			for (int temp=0;temp<listaTemplate.length;temp++) {
				String[] l=listaTemplate[temp].split(" ");
				String file1=l[0];
				String file2=l[1];
				String file3=l[2];
				double valoreFinale;
				double valore1=trovaValore(asse,Integer.valueOf(listaProbe.get(x)),file1);
				double valore2=trovaValore(asse,Integer.valueOf(listaProbe.get(x)),file2);
				double valore3=trovaValore(asse,Integer.valueOf(listaProbe.get(x)),file3);
				valoreFinale=Math.min(Math.min(valore1,valore2),valore3);
				row[cont]=String.valueOf(valoreFinale);
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
	private double trovaValore(String asse,int x, String file1) {
		
		// TODO Auto-generated method stub
		ArrayList<Double> PX=new ArrayList<Double>();
		ArrayList<Double> PY=new ArrayList<Double>();
		ArrayList<Double> PZ=new ArrayList<Double>();
		ArrayList<Double> G1X=new ArrayList<Double>();
		ArrayList<Double> G1Y=new ArrayList<Double>();
		ArrayList<Double> G1Z=new ArrayList<Double>();
		InserisciValori(x,PX,PY,PZ);
		InserisciValori(Integer.valueOf(file1),G1X,G1Y,G1Z);
		double valoreX=Alg.Dtw(PX, G1X);
		double valoreY=Alg.Dtw(PY, G1Y);
		double valoreZ=Alg.Dtw(PZ, G1Z);
		double res=0;
		if(asse.equals("X")) {
			res=valoreX;
		}
		else if(asse.equals("Y")) {
			res=valoreY;
		}
		else if(asse.equals("Z")) {
			res=valoreZ;
		}
		else if(asse.equals("COMB")) {
			double z1=(valoreZ/2);
			double x1=(valoreX/4);
			double y1=(valoreY/4);
			res=(valoreX+valoreY+valoreZ)/3;
			//res=z1+x1+y1;
		}
		return res;
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

}


