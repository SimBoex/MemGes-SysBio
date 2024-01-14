package com.example.memges;

import androidx.annotation.RequiresApi;
import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.memges.database.DatabaseUtenti;

public class RegistraUtente extends AppCompatActivity {  //implements SensorEventListener {
    private int id;
    private int sessioni;
    private float last;
    private TextView t;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         id=DatabaseUtenti.getNewid(this);
        setContentView(R.layout.registrautente);
        setTesto(id);
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



public void setTesto(int id){
    t=findViewById(R.id.testo2);
    String testo= t.getText().toString();
    t.setText("il tuo ID  e'"+String.valueOf(id));

}


public void registraGesto(View v){
    Intent intent = new Intent(getApplicationContext(), RegistraGesto.class);
    intent.putExtra("id",id);
    intent.putExtra("ripetizione",0);
    intent.putExtra("gesto","cerchio");
    intent.putExtra("sessione",1);
    intent.putExtra("old","no");
    startActivity(intent);
}
}
