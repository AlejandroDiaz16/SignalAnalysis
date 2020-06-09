package com.example.interiorpositioning;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.interiorpositioning.entidades.data;
import com.example.interiorpositioning.utilidades.utilidades;


import java.util.ArrayList;


public class Visualizar extends AppCompatActivity {
    ListView lista;
    ArrayList<String> listaInfo;
    ArrayList<data> listaData;
    ConexionSQLiteHelper conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);
        conn=new ConexionSQLiteHelper(this,"bd_data",null,1);
        lista=(ListView) findViewById(R.id.lista);
        consultarData();
        ArrayAdapter adap=new ArrayAdapter(this,android.R.layout.simple_list_item_1,listaInfo);
        lista.setAdapter(adap);


    }

    private void consultarData() {
        SQLiteDatabase db=conn.getReadableDatabase();

        data d=null;
        listaData=new ArrayList<data>();
        Cursor cur=db.rawQuery("SELECT * FROM "+utilidades.tabla_data,null);
        while (cur.moveToNext()){
            d=new data(cur.getString(0),cur.getString(1),cur.getString(2),cur.getString(3),cur.getString(4),cur.getString(5));
            listaData.add(d);

        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInfo=new ArrayList<String>();
        for (int i=0; i<listaData.size(); i++){
            listaInfo.add("\n Sensor Table: \n\n"
                    +"\n Acelerómetro: "+listaData.get(i).getAcelerometro()+"\n"
                    +"\n Giroscopio: " +listaData.get(i).getGiroscopio()+"\n"
                    +"\n Magnetómetro: "+listaData.get(i).getMagneto()+"\n"
                    +"\nNo. de pasos dados : "+listaData.get(i).getPasos()+"\n"
                    +"\nV MAC: "+listaData.get(i).getMac()+"\n"
                    +"\n"+"Estampa de tiempo: \n"+listaData.get(i).getFecha()+"\n");
        }
    }
}
