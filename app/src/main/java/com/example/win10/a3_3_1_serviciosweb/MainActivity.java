package com.example.win10.a3_3_1_serviciosweb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements ServerResponse{
    static RecyclerView recycler;
    PostAlumn conexionservidor;
    EditText nombre, direccion, id;
    Button insertar, listar, buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = (EditText) findViewById(R.id.txtId);
        nombre = (EditText)findViewById(R.id.txtNombre);
        direccion = (EditText)findViewById(R.id.txtDireccion);
        insertar =(Button) findViewById(R.id.btnInsertar);
        listar = (Button)findViewById(R.id.btnListar);
        buscar = (Button)findViewById(R.id.btnBuscar);
        recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setHasFixedSize(true);

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject datosAlumnos = new JSONObject();
                try {
                    conexionservidor = new PostAlumn(MainActivity.this);
                    datosAlumnos.put("nombre", URLEncoder.encode(nombre.getText().toString(), "utf-8"));
                    datosAlumnos.put("direccion", URLEncoder.encode(direccion.getText().toString(), "utf-8"));
                    try {
                        conexionservidor.getJSON(String.valueOf(datosAlumnos));
                        URL url = new URL("http://10.58.1.215:8080/datos1/insertar_alumno.php");
                        conexionservidor.execute(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                GetAlumn process = new GetAlumn(MainActivity.this);
                process.execute();
            }
        });

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetAlumn process = new GetAlumn(MainActivity.this);
                process.execute();
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(id.getText().toString());
                GetAlumnId process = new GetAlumnId(n, MainActivity.this);
                process.execute();
            }
        });
    }

    @Override
    public void procesarRespuesta(String r) {
        if (r == null) {

        } else {
            try {
                JSONObject respuesta = new JSONObject(r);
                if (respuesta.getInt("estado")==1) {
                    if (respuesta.getString("mensaje").equals("Creacion correcta")) {
                        Toast.makeText(MainActivity.this, "Datos insertados con éxito", Toast.LENGTH_SHORT).show();
                        nombre.setText("");
                        direccion.setText("");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

