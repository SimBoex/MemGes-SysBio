package com.example.memges.database;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Aquisizione {


    private String timestamp,x,y,z;
    private String nomefile;




    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String[] getRiga(Context c,String file,String t) {
        String[] r=null;
        try {
            File filePath = new File(c.getExternalFilesDir(null), file);
            Reader reader = Files.newBufferedReader(Paths.get(filePath.toString()));
            CSVReader csvReader = new CSVReader(reader);
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                if (record[0].equals(t)) {
                    r=record;
                }
            }
            if (r==null){
                r=new String[]{"riga non trovata"};
            }
            csvReader.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  r;
    }


    public static void setNuovaRiga(CSVWriter csvWriter,String t,String x,String y,String z) throws IOException {

            csvWriter.writeNext(new String[]{t,x,y,z});
            csvWriter.flush();


    }


}
