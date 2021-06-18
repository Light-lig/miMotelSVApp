package com.example.mimotelsv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mimotelsv.modelos.Departamento;
import com.example.mimotelsv.modelos.Municipio;
import com.example.mimotelsv.modelos.Usuario;
import com.example.mimotelsv.util.Constantes;
import com.example.mimotelsv.util.Session;
import com.example.mimotelsv.util.Util;
import com.example.mimotelsv.util.Validaciones;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Activity_singUp extends AppCompatActivity {
    private TextInputLayout txtCorreo, txtPass1, txtPass2, cmbDepartamentos, cmbMunicipios;
    private Constantes con  = new Constantes();
    //listas que contentran todos los datos
    private List<Municipio> AllMunicipios = new ArrayList<>();
    //lista para filtrar los municipios
    private List<String> listaMunicipios = new ArrayList<>();
    //lista que contentran los nombres
    private List<String> listaDepartamentos = new ArrayList<>();
    private AutoCompleteTextView actDepartamentos, actMunicipios;
    private LinearProgressIndicator progresBarRegistrarme;
    private Session preferencias;
    private Button btnRegistrarme;
    private ArrayAdapter<String> adapterDepatamentos, adapterMunicipios;
    private Validaciones val = new Validaciones();
    private Util util = new Util();
    private String URL = "http://"+con.IP+":8080/users/newUser";
    private int departamento;
    private int Municipio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        preferencias = new Session(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actDepartamentos = (AutoCompleteTextView) findViewById(R.id.actDepartamentos);
        actMunicipios = (AutoCompleteTextView) findViewById(R.id.actMunicipios);
        cmbDepartamentos = findViewById(R.id.cmbDepartamentosSing);
        btnRegistrarme = findViewById(R.id.btnRegistrarme);
        progresBarRegistrarme = findViewById(R.id.progresBarRegistrarme);
        txtPass1 = findViewById(R.id.txtClaveSing);
        txtCorreo = findViewById(R.id.txtCorreoSing);
        txtPass2 = findViewById(R.id.txtPass2);
        cmbMunicipios = findViewById(R.id.cmbMunicipiosSing);
        adapterDepatamentos = new ArrayAdapter(
                this,
                R.layout.list_item,
                listaDepartamentos
        );
        adapterMunicipios = new ArrayAdapter(
                this,
                R.layout.list_item,
                listaMunicipios
        );

        actDepartamentos.setAdapter(adapterDepatamentos);
        actMunicipios.setAdapter(adapterMunicipios);
        progresBarRegistrarme.hide();
        ((AutoCompleteTextView)cmbDepartamentos.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterDepatamentos.getItem(position);
                final String departamento = ((AutoCompleteTextView)cmbDepartamentos.getEditText()).getText().toString();
                listaMunicipios.clear();
                List<Municipio> listaMunicipiosFiltrada = AllMunicipios.stream().filter(mun -> mun.getDepa().getNombre().equals(departamento)).collect(Collectors.toList());
                for (Municipio mu : listaMunicipiosFiltrada){
                    listaMunicipios.add(mu.getNombre());
                    adapterMunicipios.notifyDataSetChanged();
                }

            }
        });
        View view = findViewById(android.R.id.content).getRootView();
        downloadData(view);

        btnRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario us = new Usuario();
                progresBarRegistrarme.show();

                if (val.validar(txtCorreo)
                        && val.validar(txtPass1)
                        && val.caracteresMinimos(txtCorreo,8)
                        && val.caracteresMinimos(txtPass1,8)
                        && val.caracteresMinimos(txtPass2,8)
                        && val.validar(txtPass2)
                        && val.validar(cmbMunicipios)
                        && val.equalPasswords(txtPass1,txtPass2)
                      ){

                    us.setCorreo(txtCorreo.getEditText().getText().toString());
                    us.setPass(txtPass2.getEditText().getText().toString());
                    String municipio =  cmbMunicipios.getEditText().getText().toString();
                    Municipio municipioSelected = AllMunicipios.stream().filter(mun -> mun.getNombre().equals(municipio)).collect(Collectors.toList()).get(0);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("correo", us.getCorreo());
                    params.put("password", us.getPass());
                    params.put("idMunicipio", String.valueOf(municipioSelected.getId()));
                    params.put("tipoUser", String.valueOf(3));

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if (response == null) {
                                        progresBarRegistrarme.hide();
                                        Toast.makeText(getBaseContext(), "Usuario ya existe.", Toast.LENGTH_LONG).show();
                                    } else {
                                        try {
                                            preferencias.salvarDatos("idUsuario", "int", String.valueOf(response.getInt("usrId")));
                                            preferencias.salvarDatos("nombre", "string", response.getString("usrCorreo"));
                                            progresBarRegistrarme.hide();
                                            Intent moteles = new Intent(getBaseContext(), Activity_moteles.class);
                                            startActivity(moteles);
                                            finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        util.mostrarSnack(v, progresBarRegistrarme, error.getMessage());
                                    } else if (error instanceof AuthFailureError) {
                                        try {
                                            JSONObject mensaje = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                                            util.mostrarSnack(v, progresBarRegistrarme , mensaje.getString("mensaje"));
                                        } catch (UnsupportedEncodingException | JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (error instanceof ServerError) {
                                        util.mostrarSnack(v, progresBarRegistrarme, error.getMessage());
                                    } else if (error instanceof NetworkError) {
                                        util.mostrarSnack(v, progresBarRegistrarme, error.getMessage());
                                    } else if (error instanceof ParseError) {
                                        util.mostrarSnack(v, progresBarRegistrarme, error.getMessage());
                                    }
                                }
                            });

                    request.setRetryPolicy(new

                            DefaultRetryPolicy(60000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                    Volley.newRequestQueue(getBaseContext()).add(request);
                    progresBarRegistrarme.hide();
                }else{
                    progresBarRegistrarme.hide();
                }
            }
        });

    }
    protected void downloadData(View v) {
        String URL = "http://"+con.IP+":8080/moteles/departamentos";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null){
                            try {



                                for(int i=0;i<response.length();i++){
                                    // Get current json object
                                    JSONObject departamento = response.getJSONObject(i);
                                    Departamento de = new Departamento();
                                    // Get the current student (json object) data
                                    de.setId(departamento.getInt("depId"));
                                    de.setNombre(departamento.getString("depNombre"));

                                    JSONArray municipios = departamento.getJSONArray("smMunicipioList");
                                    for(int j= 0; j<municipios.length();j++){
                                        JSONObject muni = municipios.getJSONObject(j);
                                        Municipio mu = new Municipio();
                                        mu.setId(muni.getInt("munId"));
                                        mu.setNombre(muni.getString("munNombre"));
                                        mu.setDepa(de);
                                        AllMunicipios.add(mu);
                                    }
                                    listaDepartamentos.add(de.getNombre());
                                    adapterDepatamentos.notifyDataSetChanged();
                                }


                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                        progresBarRegistrarme.hide();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            util.mostrarSnack(v, progresBarRegistrarme, error.getMessage());
                        } else if (error instanceof AuthFailureError) {
                            util.mostrarSnack(v, progresBarRegistrarme, error.getMessage());
                        } else if (error instanceof ServerError) {
                            util.mostrarSnack(v, progresBarRegistrarme, error.getMessage());
                        } else if (error instanceof NetworkError) {
                            util.mostrarSnack(v, progresBarRegistrarme, error.getMessage());
                        } else if (error instanceof ParseError) {
                            util.mostrarSnack(v, progresBarRegistrarme, error.getMessage());
                        }
                    }
                });

        request.setRetryPolicy(new

                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(this).add(request);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}