package matrix;

import java.io.File;  
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import grafici.*;

public class ReadMatrixFuzzy {
	private boolean inter;
	private File dir;
	private File[] filesList;
	public CSVWriter writer;
	public CSVReader reader;
	public List<String> listaTemplate;
	public int dimFilesProbe;
	public int dimId;
	public ReadMatrixFuzzy(boolean inter,String test){
		this.inter=inter;
		getPathDir();		
		if (test.equals("verification")) {
			// do test
			System.out.println("read mode");
			listaTemplate=trovaListaGallery(inter,"1a");
			dimId=listaTemplate.size();
			float[] res;
			float AucMed=0;
			float EERMed=0;
			res=getCurves("1a",inter);
			float EER= (float) (Math.round(Math.round(res[0] * 1000) / 10.0) / 100.0);
			float AUC=(float) (Math.round(Math.round(res[1] * 1000) / 10.0) / 100.0);
			System.out.println("EER "+EER);
			System.out.println("AUC "+AUC);
			listaTemplate=trovaListaGallery(inter,"2a");
			dimId=listaTemplate.size();
			res=getCurves("2a",inter);
			float EER1=(float) (Math.round(Math.round(res[0] * 1000) / 10.0) / 100.0);
			float AUC1=(float) (Math.round(Math.round(res[1] * 1000) / 10.0) / 100.0);
			AucMed=(AUC+AUC1)/2;
			EERMed=(EER+EER1)/2;
			System.out.println("EER1 "+EER1);
			System.out.println("AUC1 "+AUC1);
			System.out.println("EER medio "+Math.round(Math.round(EERMed * 1000) / 10.0) / 100.0);
			System.out.println("AUC medio "+Math.round(Math.round(AucMed * 1000) / 10.0) / 100.0);
		}
		else if(test.equals("identification")) {
			//do test
			System.out.println("read mode");
			listaTemplate=trovaListaGallery(inter,"1a");
			dimId=listaTemplate.size();
			IdentificationResults res1=identification(inter,"1a");
			listaTemplate=trovaListaGallery(inter,"2a");
			dimId=listaTemplate.size();
			IdentificationResults res2=identification(inter,"2a");
			disegna(res1.cmc,res2.cmc);
			float gestureMedio=(res1.GestureRate+res2.GestureRate)/2;
			float gestureMedioA=(float) (Math.round(Math.round(gestureMedio * 1000) / 10.0) / 100.0);
			System.out.println("recognition rate gesture medio "+gestureMedioA);
		}
	}
	
	private float[] getCurves(String tipo,boolean inter) {
		// TODO Auto-generated method stub
		String s;
		if (inter) {
			s="WithInter";
		}
		else {
			s="WithoutInter";
		}
		List<Float> GestTasso=new ArrayList<Float>();
		List<Float> thGesto=new ArrayList<Float>();

		List<Float> TPR=new ArrayList<Float>();// true positive rate
		List<Float> FPR=new ArrayList<Float>();//
		List<Float> FAR=new ArrayList<Float>();
		List<Float> FRR=new ArrayList<Float>();
		List<Float> th=new ArrayList<Float>();
		float lF1=0;
		float lF2=0;
		boolean incontro=true;
		float threshold;
		int cont=0;
		float v=-1;
		float c=-1;
		
		float resEER = 0;
		while( v!=1 || c!=1 ){
			threshold=(float) (0.05*cont);//FAR>FRR
			float[] b=verification(inter,threshold,tipo);
			 
			if (b[2]==b[1] && incontro) {
				resEER=b[2];
				incontro=false;
			}
			else if(b[2]>b[1]) {
				if(incontro) {
					lF1=FAR.get(FAR.size()-1);
					lF2=b[2];
					resEER=(lF1+lF2)/(float)2;
					incontro=false;
				}
				
				
						
			}
			FAR.add(b[2]);
			FRR.add(b[1]);
			TPR.add(1-b[1]);
			FPR.add(b[2]);
			if(b[4]>=0) {
			GestTasso.add(b[4]);
			thGesto.add(threshold);
			}
			
			v=b[2];
			c=1-b[1];
			th.add(threshold);
			cont++;
			
		}
		float[] match=new float[2];		
		GestureStat g=new GestureStat(tipo,GestTasso,thGesto);
		FRR r=new FRR(s+tipo,FRR,th);
		FAR fa=new FAR(s+tipo,FAR,th);
		EER e=new EER(s+tipo,FAR,FRR,th);
		ROC roc=new ROC(s+tipo,TPR,FPR);
		AUC a=new AUC(s+tipo,TPR,FPR);
		float val=a.valoreAuc;
		match[0]=resEER;
		match[1]=val;
		return match;
	}
	public  void getPathDir() {
	    this.dir = new File("files");
		this.filesList = dir.listFiles(); 
	}
	
	public void disegna(ArrayList<Float> cmc1,ArrayList<Float> cmc2) {
		ArrayList<Float> cmcFinale=new ArrayList<Float>();
		for(int x=0;x<cmc1.size();x++) {
			float v1=cmc1.get(x);
			float v2=cmc2.get(x);
			float medio=(v1+v2)/2;
			cmcFinale.add(medio);
		}
		CMC c=new CMC("CMC",cmcFinale);
		System.out.println("CMC(1) "+Math.round(Math.round(cmcFinale.get(0) * 1000) / 10.0) / 100.0);
		System.out.println("CMC(5) "+Math.round(Math.round(cmcFinale.get(4) * 1000) / 10.0) / 100.0);
		System.out.println("CMC(10) "+Math.round(Math.round(cmcFinale.get(9) * 1000) / 10.0) / 100.0);
	}
	
	private  float[] verification(boolean inter,double threshold,String tipo) {
		// TODO Auto-generated method stub
		String s;
		if (inter) {
			s="WithInter";
		}
		else {
			s="WithoutInter";
		}
		float[] res=new float[5];
		List<Float> b=new ArrayList<Float>();
		b.add((float)0);//GA
		b.add((float)0);//FR
		b.add((float)0);//FA
		b.add((float)0);//GR
		b.add((float ) 0); //numero gesto genuino
		b.add((float ) 0);
		int cont=0;
		try {
			reader = new CSVReader(new FileReader("MatrixFuzzy"+"_"+s+"_"+tipo+"_"+".csv"));	
			String[] nextLine;		
			while ((nextLine = reader.readNext()) != null){		    	  
			     if ((nextLine != null)){		        	 
			    	 if (!nextLine[0].equals("")) { 
			        	VerifCalculator(nextLine,threshold,b);		        	     
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
			dimFilesProbe=cont;
			float GA=b.get(0);
			float FA=b.get(2);
			float FR=b.get(1);
			float GR=b.get(3);
			float GC=b.get(4); //genuine
			float GI=b.get(5);//impostor
			float TG=dimFilesProbe;
			float TI=dimFilesProbe*(dimId-1);
			float GAR=GA/TG; 
			float FAR=FA/TI;
			float FRR=FR/TG; 
			float GRR=GR/TI;
			float GestoP=(GI+GC)/(GA+FA);
			
			res[0]=GAR;
			res[2]=FAR;
			res[1]=FRR;
			res[3]=GRR;
			res[4]=GestoP;
			return res;
	}
	
	
	
	
	
	public List<String> trovaListaGallery(boolean inter,String tipo) {
		String s;
		if (inter) {
			s="WithInter";
		}
		else {
			s="WithoutInter";
		}
		String[] header = null;
		try {
			reader = new CSVReader(new FileReader("MatrixFuzzy"+"_"+s+"_"+tipo+"_"+".csv"));	
			 header=reader.readNext();
			     reader.close();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CsvValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		List<String> listaGallery=Arrays.asList(header).subList(1,header.length );
		
		return listaGallery;
	}
	
	public IdentificationResults identification(boolean inter,String tipo) {
		String s;
		if (inter) {
			s="WithInter";
		}
		else {
			s="WithoutInter";
		}
		ArrayList<Integer> cmsId=new ArrayList<Integer>();
		for (int i=0;i<listaTemplate.size();i++) {
			cmsId.add(0);
		}
		float contGesture=0;
		int cont=0;		
		try {
			reader = new CSVReader(new FileReader("MatrixFuzzy"+"_"+s+"_"+tipo+"_"+".csv"));	
			String[] nextLine;		
			while ((nextLine = reader.readNext()) != null){		    	  
			     if ((nextLine != null)){		        	 
			    	 if (!nextLine[0].equals("")) { 
			        	contGesture+=CmsCalculator(nextLine,cmsId);		        	     
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
		
			dimFilesProbe=cont;
			ArrayList<Float> cmc=PrintCmc(inter,tipo,cmsId);
			float rate=contGesture/dimFilesProbe;
			IdentificationResults res=new IdentificationResults(cmc,rate);
			return res;
	}

	private void VerifCalculator(String[] nextLine, double threshold, List<Float> b) {
		// TODO Auto-generated method stub
		int dimId=nextLine.length-1;
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
		
		
		for(int id=0;id<i.length;id++) { //identità dichiarata;
			for(int x=1;x<nextLine.length;x++) {
				String idProbe=nextLine[0].split("_")[0];
				String idGesture=nextLine[0].split("_")[2];
				String temp=listaTemplate.get(x-1);
				String idTemp=temp.split("_")[0];
				String GesTemp=temp.split("_")[2];
				if(String.valueOf(i[id]).equals(idTemp)) {// template corrispondente
					double valore=Double.valueOf(nextLine[x]);
					if(valore<=threshold) {						
						if(idProbe.equals(idTemp)) {
							b.set(0, b.get(0)+1);//GA
							if(idGesture.equals(GesTemp)) {
								b.set(4, b.get(4)+1); //gesto uguale G
							}
						}
						else {
							b.set(2, b.get(2)+1); //FA
							if(idGesture.equals(GesTemp)) {
								b.set(5, b.get(5)+1); //gesto uguale I
							}
						}
				
					}
					else {
					
						if(idProbe.equals(idTemp)) {
							b.set(1, b.get(1)+1); //FR
						}
						else {
							b.set(3, b.get(3)+1); //GR
						}
				
		
					}
	
				}
			}
		}
	}
	private int CmsCalculator(String[] nextLine,ArrayList<Integer> cmsId) {
		// TODO Auto-generated method stub	
		int contGesture=0;
		double[] lista=new double[nextLine.length-1];
		for(int x=1;x<nextLine.length;x++) {
			lista[x-1]=Double.valueOf(nextLine[x]);
		}
		
		Arrays.sort(lista);
		for(int x=0;x<lista.length;x++) {
		int j=Arrays.asList(nextLine).indexOf(String.valueOf(lista[x]));
			j=j-1;
			String template=listaTemplate.get(j);
			String id=template.split("_")[0];
			String gesture=template.split("_")[2];
			String probe=nextLine[0];
			String idProbe=probe.split("_")[0];
			String gestureProbe=probe.split("_")[2];
			
			if(idProbe.equals(id)) {
				cmsId.set(x, cmsId.get(x)+1);
			}
			if(x==0) {	
				if(gestureProbe.equals(gesture)) {
					contGesture++;
				}
			}
		}
		//inserisco in pos 1,2,3 i match;
	return contGesture;
	}

	
	
	private ArrayList<Float> PrintCmc(boolean inter,String tipo,ArrayList<Integer> cmsId) {
		// TODO Auto-generated method stub
		ArrayList<Float> l=new ArrayList<Float>();
		ArrayList<Integer> index=new ArrayList<Integer>();
		String s;
		if (inter) {
			s="WithInter";
		}
		else {
			s="WithoutInter";
		}
		float tot=0;
		for(int x=1;x<cmsId.size()+1;x++) {
			tot+=(float)(cmsId.get(x-1)/(float)dimFilesProbe);	
			l.add(tot);
			index.add(x);
		}
		return l;
	}
	
	
}
