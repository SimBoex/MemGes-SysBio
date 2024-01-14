package matrix;

import java.util.ArrayList;

public class IdentificationResults {

	ArrayList<Float> cmc;
	float GestureRate;
	
	IdentificationResults(ArrayList<Float> cmc,float rate){
		this.cmc=cmc;
		GestureRate=rate;
	}
}
