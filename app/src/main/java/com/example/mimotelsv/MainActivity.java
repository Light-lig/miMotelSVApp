package com.example.mimotelsv;

import androidx.activity.ComponentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mimotelsv.modelos.Usuario;
import com.example.mimotelsv.util.Constantes;
import com.example.mimotelsv.util.Session;
import com.example.mimotelsv.util.Validaciones;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ComponentActivity {
    private Validaciones val = new Validaciones();
    private Constantes con  = new Constantes();
    Button btnIniciarSesion, btnRegistrarme;
    TextInputLayout txtCorreo, txtContra;
    private LinearProgressIndicator barraProgreso;
    private Session preferencias;
    String URL = "http://"+con.IP+":8080/users/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MiMotelSv);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferencias = new Session(this);
        Integer idUsuario = preferencias.getAppSettings().getInt("idUsuario",-1);
        if(idUsuario != -1){
            Intent moteles = new Intent(getBaseContext(), Activity_moteles.class);
            startActivity(moteles);
            finish();
        }
        barraProgreso = findViewById(R.id.progresBarLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSession);
        btnRegistrarme = findViewById(R.id.btnRegistrarme);
        btnRegistrarme.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent registrarme = new Intent(getBaseContext(),Activity_singUp.class);
                startActivity(registrarme);
            }
        });
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario us = new Usuario();
                txtCorreo = findViewById(R.id.txtEmail);
                txtContra = findViewById(R.id.txtpass);

                if(val.validar(txtCorreo) && val.validar(txtContra)){
                    barraProgreso.show();
                    us.setCorreo(txtCorreo.getEditText().getText().toString());
                    us.setPass(txtContra.getEditText().getText().toString());
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("usrCorreo",us.getCorreo());
                    params.put("usrPassword", us.getPass());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if(response == null){
                                        barraProgreso.hide();
                                        Toast.makeText(getBaseContext(),"Usuario o contrasenia no valido",Toast.LENGTH_LONG).show();
                                    }else{
                                        try {
                                            preferencias.salvarDatos("idUsuario","int",String.valueOf(response.getInt("usrId")));
                                            preferencias.salvarDatos("nombre","string",response.getString("usrCorreo"));
                                            barraProgreso.hide();
                                            Intent moteles = new Intent(getBaseContext(), Activity_moteles.class);
                                            startActivity(moteles);
                                            finish();
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        Toast.makeText(getBaseContext(),
                                                getBaseContext().getString(R.string.error_network_timeout),
                                                Toast.LENGTH_LONG).show();
                                        barraProgreso.hide();
                                    } else if (error instanceof AuthFailureError) {
                                        //TODO
                                    } else if (error instanceof ServerError) {
                                        //TODO
                                    } else if (error instanceof NetworkError) {
                                        //TODO
                                    } else if (error instanceof ParseError) {
                                        //TODO
                                    }
                                }
                            });

                    request.setRetryPolicy(new

                            DefaultRetryPolicy(60000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                    Volley.newRequestQueue(getBaseContext()).add(request);
                }

            }
        });

    }


}