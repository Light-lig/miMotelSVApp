package com.example.mimotelsv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.android.volley.toolbox.Volley;
import com.example.mimotelsv.modelos.Departamento;
import com.example.mimotelsv.modelos.Municipio;
import com.example.mimotelsv.util.Constantes;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
    private ArrayAdapter<String> adapterDepatamentos, adapterMunicipios;
    private int departamento;
    private int Municipio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actDepartamentos = (AutoCompleteTextView) findViewById(R.id.actDepartamentos);
        actMunicipios = (AutoCompleteTextView) findViewById(R.id.actMunicipios);
        cmbDepartamentos = findViewById(R.id.cmbDepartamentosSing);
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
        downloadData();
    }
    protected void downloadData() {
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

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getBaseContext(),
                                    getBaseContext().getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
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