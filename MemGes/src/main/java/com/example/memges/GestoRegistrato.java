package com.example.memges;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memges.database.DatabaseUtenti;


public class GestoRegistrato extends AppCompatActivity {
    int id;
    int sessione;
    int ripetizione;
    long lasttime;
    String figura;
    String old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestoregistrato);
        Intent i = getIntent();
        Bundle v = i.getExtras();
        id = v.getInt("id");
        sessione = v.getInt("sessione");
        ripetizione = v.getInt("ripetizione");
        figura = v.getString("gesto");
        old = v.getString("old");
        lasttime=v.getLong("lasttime");

        if (ripetizione + 1 < 3) {
            String x = "ripetizione\nregistrata\ncorrettamente";
            String y = "clicca qui per ripetere il gesto " + figura;
            setTesto(x, y);
        } else {
            if ((ripetizione + 1 == 3) && (figura.equals("cerchio"))) {
                String x = "hai completato\nil gesto cerchio";
                String y = "clicca qui per inserire il nuovo gesto rettangolo";
                setTesto(x, y);
            } else if ((ripetizione + 1 == 3) && (figura.equals("rettangolo"))) {
                String x = "hai terminato di inserire il gesto rettangolo";
                String y = "clicca qui per chiudere";
                setTesto(x, y);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void registraGesto(View v) {
        if (ripetizione + 1 < 3) {
            Intent intent = new Intent(this, RegistraGesto.class);
            intent.putExtra("id", id);
            intent.putExtra("sessione", sessione);
            intent.putExtra("gesto", figura);
            intent.putExtra("ripetizione", ++ripetizione);
            intent.putExtra("old", old);
            startActivity(intent);
        } else {
            if ((ripetizione + 1 == 3) && (figura.equals("cerchio"))) {
                Intent intent = new Intent(this, RegistraGesto.class);
                intent.putExtra("id", id);
                intent.putExtra("sessione", sessione);
                intent.putExtra("gesto", "rettangolo");
                intent.putExtra("ripetizione", 0);
                intent.putExtra("old", old);
                startActivity(intent);
            } else if ((ripetizione + 1 == 3) && (figura.equals("rettangolo"))) {

                if (old.equals("si")) {

                    DatabaseUtenti.SetOldUtente(getApplicationContext(), id,lasttime, sessione);

                } else if (old.equals("no")){
                    DatabaseUtenti.setNuovoUtente(getApplicationContext(), String.valueOf(id), sessione,(long) lasttime);
                }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RegistraGesto.class);
        intent.putExtra("ripetizione",ripetizione);
        intent.putExtra("gesto", figura);
        intent.putExtra("sessione", sessione);
        intent.putExtra("id", id);
        intent.putExtra("old",old);
        startActivity(intent);
    }

    public void setTesto(String a,String b){
        TextView bottone = (TextView) findViewById(R.id.Registra);
        TextView testo = (TextView) findViewById(R.id.testo);
        testo.setText(a);
        bottone.setText(b);
    }

}
