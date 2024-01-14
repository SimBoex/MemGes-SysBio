package verification;

import java.io.IOException;

import matrix.DtwMatrix;
import matrix.FuzzyMatrix;

public class verification_test {

	public void DtwTest() {
		String[] nome=new String[4];
		nome[0]="asse singolaX";
		nome[1]="asse singolaY";
		nome[2]="asse singolaZ";
		nome[3]="asse combinata";
		float[] array=new float[4];
		DtwMatrix m=new DtwMatrix("cross");
		array=m.testDtwVerification();
		for (int x=0;x<array.length;x=x+2) {
			System.out.println("rate "+nome[x]+" è "+String.valueOf(array[x]));
			System.out.println("rate "+nome[x]+" è "+String.valueOf(array[x+1]));
		}
	}
	
	public void FuzzyTest() {
		String[] nome=new String[2];
		nome[0]="risultati alg without interpolation";
		nome[1]="risultati alg with interpolation";
		boolean inter=false;
		for(int x=0;x<2;x++) {
		float[] array=new float[2];
		FuzzyMatrix m;
		try {
			m = new FuzzyMatrix("cross",inter);
		
		array=m.testFuzzy();
		System.out.println(nome[x]);
		System.out.println(array[0]+" , "+array[1]);
		inter=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	}
