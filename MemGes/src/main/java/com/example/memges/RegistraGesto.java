package com.example.memges;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.memges.database.Aquisizione;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;

import static android.os.Environment.getExternalStorageDirectory;
import android.view.View.OnTouchListener;

public class RegistraGesto extends Activity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private int ripetizione;
    private int sessione;
    private int id;
    private int Numevento = 1;
    private String figura;
    double x1, y1, z1;
    long lasttime=0;
    String file;
    boolean stop, start;
    Context c;
    CSVWriter csvWriter;
    File filePath;
    String old;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registra_gesto);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        c = getApplicationContext();
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        Intent i = getIntent();
        Bundle v = i.getExtras();
        id = v.getInt("id");
        sessione = v.getInt("sessione");
        figura = v.getString("gesto");
        ripetizione = v.getInt("ripetizione");
        old=v.getString("old");
        stop = false;
        start = false;
        long t = System.currentTimeMillis();
        CharSequence text = "clicca sul pulsante + del volume per registrare un "+figura;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getBaseContext(), text, duration);
        toast.show();
        file = String.valueOf(id) + "_" + String.valueOf(sessione) + "_" + figura + "_" + String.valueOf(ripetizione) + ".csv";
        filePath = new File(c.getExternalFilesDir(null), file);
        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            csvWriter = new CSVWriter(new FileWriter(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;
        if (((mySensor.getType() == Sensor.TYPE_ACCELEROMETER) && (Numevento <= 300)) && start) {

            long startTime = System.currentTimeMillis();
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            long diff = (long) (startTime - lasttime);



            if (lasttime == 0) {
                lasttime = startTime;
                try {

                    Aquisizione.setNuovaRiga(csvWriter, String.valueOf(startTime), String.valueOf(x), String.valueOf(y), String.valueOf(z));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Numevento++;
            } else if (diff == 20) {

                lasttime = startTime;
                try {

                    Aquisizione.setNuovaRiga(csvWriter, String.valueOf(startTime), String.valueOf(x), String.valueOf(y), String.valueOf(z));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Numevento++;
            } else if (diff > 20) {
                while ((diff >= 20) && (Numevento <= 300)) {
                    lasttime = lasttime + 20;
                    if(diff==20){
                        try {
                              Aquisizione.setNuovaRiga(csvWriter, String.valueOf(startTime), String.valueOf(x), String.valueOf(y), String.valueOf(z));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            Aquisizione.setNuovaRiga(csvWriter, String.valueOf(lasttime), String.valueOf(x1), String.valueOf(y1), String.valueOf(z1));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Numevento++;
                    diff = diff - 20;
                }
            }
            x1 = x;
            y1 = y;
            z1 = z;




        } else if ((Numevento > 300) || stop) {
            senSensorManager.unregisterListener(this);
            try {
                csvWriter.flush();
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, GestoRegistrato.class);
            intent.putExtra("ripetizione", ripetizione);
            intent.putExtra("gesto", figura);
            intent.putExtra("sessione", sessione);
            intent.putExtra("id", id);
            intent.putExtra("old",old);
            intent.putExtra("lasttime",lasttime);
            startActivity(intent);

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    start = true;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN && start) {
                    Intent i = new Intent(getApplicationContext(), GestoRegistrato.class);
                    i.putExtra("ripetizione", ripetizione);
                    i.putExtra("gesto", figura);
                    i.putExtra("sessione", sessione);
                    i.putExtra("id", id);
                    i.putExtra("old",old);
                    i.putExtra("lasttime",lasttime);

                    startActivity(i);
                }
                return true;
            default:
                return true;
        }
    }

}

