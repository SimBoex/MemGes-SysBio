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
import java.util.List;

public class DatabaseUtenti {

    private String id,sessioni,last;
    private String nomefile;


    public void DatabaseUtenti(){
        //crei file;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getNewid(Context c) {
        int id=0;
        int cont=0;
        try {
            File f=new File(c.getExternalFilesDir(null), "database.csv");
            Reader reader = Files.newBufferedReader(Paths.get(f.toString()));
            CSVReader csvReader = new CSVReader(reader);
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                if (record != null ) {
                    cont++;
                }
            }
            id=cont;
            csvReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return  id;
    }


    public static void setNuovoUtente(Context c,String id,int sessioni,long t){
        CSVWriter writer;
        String[] buffer=new String[]{id,String.valueOf(sessioni),String.valueOf(t)};

        try {
            File filePath = new File(c.getExternalFilesDir(null), "database.csv");
            if (!filePath.exists()) {
                try {
                    filePath.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            writer=new CSVWriter(new FileWriter(filePath,true));
            writer.writeNext(buffer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String[] getUtente(Context c,int id){
        String [] recordf= new String[] {"non sono invocato"};
        String[] record;
        try {
            File filePath = new File(c.getExternalFilesDir(null), "database.csv");
            Reader reader = Files.newBufferedReader(Paths.get(filePath.toString()));
            CSVReader csvReader = new CSVReader(reader);

            while ((record = csvReader.readNext()) != null) {
                if (record[0].equals(String.valueOf(id))) {
                    recordf=record;
                }
            }

            csvReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recordf;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void SetOldUtente(Context c, int id, long t, int sessioni) {
        CSVWriter writer;
        CSVReader csvReader;
        try {
            File filePath = new File(c.getExternalFilesDir(null), "database.csv");
            Reader reader = Files.newBufferedReader(Paths.get(filePath.toString()));
             csvReader = new CSVReader(reader);
            List<String[]> csvBody = csvReader.readAll();
            csvBody.get(id)[2]=String.valueOf(t);
            csvBody.get(id)[1]=String.valueOf(sessioni);
            reader.close();
            writer=new CSVWriter(new FileWriter(filePath,false));
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
