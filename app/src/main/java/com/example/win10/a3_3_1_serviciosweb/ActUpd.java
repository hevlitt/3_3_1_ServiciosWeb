package com.example.win10.a3_3_1_serviciosweb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ActUpd extends AppCompatActivity implements ServerResponse{
    EditText txtnombre,txtdireccion;
    String idAlumno,nombre,direccion;
    Button actualizar;
    PostAlumn conexionservidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        txtnombre=findViewById(R.id.updateNombre);
        txtdireccion=findViewById(R.id.updateDireccion);
        actualizar=findViewById(R.id.btnActualizar);

        if(getIntent().hasExtra("idAlumno")&&getIntent().hasExtra("nombre")&&getIntent().hasExtra("direccion")){
            idAlumno=getIntent().getStringExtra("idAlumno");
            nombre=getIntent().getStringExtra("nombre");
            direccion=getIntent().getStringExtra("direccion");
        }
        txtnombre.setText(nombre);
        txtdireccion.setText(direccion);

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject datosAlumnos = new JSONObject();
                try {
                    conexionservidor = new PostAlumn(ActUpd.this);
                    datosAlumnos.put("idalumno",idAlumno);
                    datosAlumnos.put("nombre", URLEncoder.encode(txtnombre.getText().toString(), "utf-8"));
                    datosAlumnos.put("direccion", URLEncoder.encode(txtdireccion.getText().toString(), "utf-8"));
                    try {
                        conexionservidor.getJSON(String.valueOf(datosAlumnos));
                        URL url = new URL("http://10.58.1.215:8080/datos1/actualizar_alumno.php");
                        conexionservidor.execute(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                GetAlumn process = new GetAlumn(ActUpd.this);
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
                    if (respuesta.getString("mensaje").equals("Actualizacion correcta")) {
                        Toast.makeText(ActUpd.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
