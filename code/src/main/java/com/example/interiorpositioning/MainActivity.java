package com.example.interiorpositioning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.example.interiorpositioning.utilidades.utilidades;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.Date;
import java.util.HashMap;

import java.util.Map;
import com.example.interiorpositioning.entidades.data;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private TextView mFbTextView;
    DatabaseReference rDaRe = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rRoCl = rDaRe.child("texto");

    DatabaseReference mRootReference;

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    public int time;
    public final Handler handler = new Handler();

    int whip = 0;

    TextView tv_steps;
    boolean running = false;
    Button siguiente;

    private String Acelerometro;
    private String Grioscopio;
    private String Magnetometro;
    private String wifi;
    private String pasos;
    final int TYPE_STEP_COUNTER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println(TYPE_STEP_COUNTER);
        pasos = "0";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        siguiente = (Button) findViewById(R.id.btnvis);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent siguiente= new Intent(MainActivity.this, Visualizar.class);
                startActivity(siguiente);
            }
        });


        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_data",null,1);
        acele();
        giros();
        magne();
        wifi();
        tv_steps = (TextView) findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //texto
        mFbTextView = (TextView) findViewById(R.id.text);

    }

    @Override
    protected void onStart() {
        super.onStart();

        rRoCl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String texto =  dataSnapshot.getValue().toString();
                mFbTextView.setText(texto);
                System.out.println(texto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // Aqui se Encuentran todas las funciones relacionadas con el Acelerometro!!

    private String acele(){
        sensorManager =  (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensor.TYPE_ACCELEROMETER);

        if (sensor == null){
            Toast.makeText(this, "Lo lamento mucho sensor de acelorometro no encontrado:(", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Perfecto, tu sensor de acelorometro, está listo!!", Toast.LENGTH_SHORT).show();
            //System.out.println("Correcto C");
        }

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                Acelerometro = "Cordenada en x: "+x+"\n                           Cordenada en y: "+y+"\n                           Cordenada en z: "+z;
                //System.out.println(Acelerometro);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //System.out.println("Acelerometro no se mueve");
            }
        };
        start();
        return Acelerometro;


    }

    // Aqui se Encuentran todas las funciones relacionadas con el Giroscopio!!
    private String giros(){

        sensorManager =  (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensor.TYPE_GYROSCOPE);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                Grioscopio=" Cordenada en x: "+x+"\n                       Cordenada en y: "+y+"\n                       Cordenada en z: "+z;
                System.out.println(Grioscopio);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
               System.out.println("Giroscopio no se mueve");
            }
        };
        start();
        return Grioscopio;
    }
    // Aqui se Encuentran todas las funciones relacionadas con el Magnetómetro!!

    private String magne(){

        sensorManager =  (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensor.TYPE_MAGNETIC_FIELD);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                Magnetometro="Cordenada en x: "+x+"\n                             Cordenada en y: "+y+"\n                             Cordenada en z: "+z;
               // System.out.println(Magnetometro);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //System.out.println("Magnetometro no se mueve");
            }
        };
        start();
        return Magnetometro;
    }

    private String wifi(){
        WifiManager info=(WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiinfo=info.getConnectionInfo();
        wifi=String.valueOf(wifiinfo.getRssi());
        wifi=wifi+" dB";
        System.out.println(wifi);
        return wifi;
    }

    public void insert(View v){
        insert();
    }




    // Aqui se encuentran los metodos del ciclo de vida de la actividad!!


    @Override
    protected void onPause() {

        stop();
        stopPasos();
        super.onPause();
    }
    @Override
    protected void onResume() {
        start();
        startPasos();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tv_steps.setText(String.valueOf(0));
    }

    //Aqui se encuentran los metodos para ejecutar los eventos!!

    private void start(){
        running = true;
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stop(){
        running = false;
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running){
            tv_steps.setText(String.valueOf(event.values[0]));
            pasos = (String.valueOf(event.values[0]));

            System.out.println(pasos);
            insert();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * startPasos()
    **/
    private void startPasos() {
        running = true;
        Sensor count = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (count != null) {
            sensorManager.registerListener((SensorEventListener) this, count, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor de pasos no disponible :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPasos() {
        running = false;
    }

    /*Time*/

    public void onTime(View view){

        EditText t = (EditText) findViewById(R.id.number);
        time = Integer.parseInt(t.getText().toString())*1000;
        Button b = (Button) findViewById(R.id.number2);
        b.setEnabled(false);
        runtime();


    }

    public void runtime(){
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
                insert();
                handler.postDelayed(this, time);
           }
       },time);
    }

    public void insert(){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_data",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();

        data mdata = new data(new Date().toString(), wifi(), magne(),acele(),giros(),pasos);

        ContentValues values=new ContentValues();
        values.put(utilidades.campo_acele,mdata.getAcelerometro());
        values.put(utilidades.campo_fecha,mdata.getFecha());
        values.put(utilidades.campo_giro,mdata.getGiroscopio());
        values.put(utilidades.campo_magneto,mdata.getMagneto());
        values.put(utilidades.campo_mac,mdata.getMac());
        values.put(utilidades.campo_pasos,mdata.getPasos());



        Long idResultado=db.insert(utilidades.tabla_data,utilidades.campo_fecha,values);
        Toast.makeText(getApplicationContext(),"Id Registro "+idResultado,Toast.LENGTH_SHORT).show();
        db.close();



        mRootReference = FirebaseDatabase.getInstance().getReference();

        String s ="Sera que ahora yes?";

        Map<String, String> datosSensores = new HashMap<>();
        datosSensores.put(utilidades.campo_acele,mdata.getAcelerometro());
        datosSensores.put(utilidades.campo_fecha,mdata.getFecha());
        datosSensores.put(utilidades.campo_giro,mdata.getGiroscopio());
        datosSensores.put(utilidades.campo_magneto,mdata.getMagneto());
        datosSensores.put(utilidades.campo_mac,mdata.getMac());
        datosSensores.put(utilidades.campo_pasos,mdata.getPasos());

        mRootReference.child("Sensores").push().setValue(datosSensores);
    }

}

