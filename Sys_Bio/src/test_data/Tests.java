package test_data;


import matrix.CreaMatrixDtw;
import matrix.CreaMatrixFuzzy;
import matrix.ReadMatrixDtw;
import matrix.ReadMatrixFuzzy;

public class Tests {
	
	public  static void StartFuzzyVerification(boolean inter) {
		CreaMatrixFuzzy m1=new CreaMatrixFuzzy(inter,"1a");
		CreaMatrixFuzzy m2=new CreaMatrixFuzzy(inter,"2a");
		ReadMatrixFuzzy m3=new ReadMatrixFuzzy(inter,"verification");
	}
	
	public static void StartFuzzyIdentification(boolean inter) {
		CreaMatrixFuzzy m1=new CreaMatrixFuzzy(inter,"1a");
		CreaMatrixFuzzy m2=new CreaMatrixFuzzy(inter,"2a");
		ReadMatrixFuzzy m3=new ReadMatrixFuzzy(inter,"identification");
	}
	
	public  static void StartDtwVerification(String asse) {
		CreaMatrixDtw m=new CreaMatrixDtw("1a",asse);
		CreaMatrixDtw m2=new CreaMatrixDtw("2a",asse);
		ReadMatrixDtw m1=new ReadMatrixDtw("verification");
	}
	
	public static void StartDtwIdentification(String asse) {
		CreaMatrixDtw m=new CreaMatrixDtw("1a",asse);
		CreaMatrixDtw m2=new CreaMatrixDtw("2a",asse);
		ReadMatrixDtw m1=new ReadMatrixDtw("identification");
	}
	
	public static void main(String[] args) {
		
		StartDtwVerification("X");
			
		
		
	
	}
}

	
	

	

	
