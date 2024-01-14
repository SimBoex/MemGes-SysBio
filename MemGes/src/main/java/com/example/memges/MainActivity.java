package com.example.memges;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private CSVWriter csvWriter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String fileName = "database.csv";
        File filePath = new File(getApplicationContext().getExternalFilesDir(null), fileName);
        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }


    public void RegistraUtente(View v) {
        Intent intent = new Intent(getApplicationContext(), RegistraUtente.class);
        startActivity(intent);

    }


    public void InserisciNuovaSessione(View v){
        Intent intent=new Intent(getApplicationContext(),UtenteRegistrato.class);
        startActivity(intent);

    }
}