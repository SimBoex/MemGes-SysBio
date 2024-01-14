package com.example.memges;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.example.memges.database.DatabaseUtenti;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class UtenteRegistrato  extends AppCompatActivity{
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utenteregistrato);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void inserisciSessione(View v) {
        float istante = System.currentTimeMillis();
        EditText editText=findViewById(R.id.id);
        String temp=editText.getText().toString();
        int value=0;
        if (!"".equals(temp)){
            value=Integer.parseInt(temp);
        }
        id=String.valueOf(value);
        String[] array = DatabaseUtenti.getUtente(getApplicationContext(), Integer.parseInt(id));

        if ((Integer.valueOf(array[1]) < 3) && Float.valueOf(istante) - Float.valueOf(array[2]) > TimeUnit.HOURS.toMillis(24)) {
            int sessione=Integer.valueOf(array[1]);
            ++sessione;
            Intent intent = new Intent(this, RegistraGesto.class);
            intent.putExtra("id",Integer.valueOf(id));
            intent.putExtra("sessione",sessione);
            intent.putExtra("ripetizione",0);
            intent.putExtra("gesto","cerchio");
            intent.putExtra("old","si");
            startActivity(intent);
        }
        else{
            if (Integer.valueOf(array[1]) > 3){
                Context context = getApplicationContext();
                CharSequence text = "Hai raggiunto il limite max di sessioni";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else {
                Context context = getApplicationContext();
                CharSequence text = "non è trascorso un giorno dall'ultima sessione";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
