package com.example.mimotelsv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
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
import com.example.mimotelsv.adaptadores.ReservacionesAdapter;
import com.example.mimotelsv.modelos.Fotos;
import com.example.mimotelsv.modelos.Habitacion;
import com.example.mimotelsv.modelos.Motel;
import com.example.mimotelsv.modelos.Reservacion;
import com.example.mimotelsv.util.Constantes;
import com.example.mimotelsv.util.Session;
import com.example.mimotelsv.util.Util;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityReservaciones extends AppCompatActivity {
    private Session sesion;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ReservacionesAdapter adaptador;
    private RecyclerView rvReservaciones;
    private SwipeRefreshLayout myRefresh;
    private Constantes con = new Constantes();
    private LinearProgressIndicator barraReservaciones;
    private Util util = new Util();
    private List<Reservacion> lista = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservaciones);
        //obtenemos la sesion
        sesion = new Session(this);
        //agregamos la toolbar y le enlazamos el cajon
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mis reservaciones");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        //obtenemos el refresh
        myRefresh = findViewById(R.id.swipeContainer);
        myRefresh.setRefreshing(false);
        barraReservaciones = findViewById(R.id.progresBarReservaciones);
        myRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Intent i = new Intent(ActivityReservaciones.this, ActivityReservaciones.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                }
        );

        //adaptador
        rvReservaciones = findViewById(R.id.rcvRecervaciones);
        adaptador = new ReservacionesAdapter(lista);
        rvReservaciones.setAdapter(adaptador);
        View view = findViewById(android.R.id.content).getRootView();
        DownloadTask(view);
    }
    private void DownloadTask(View v) {
        Integer idUsuario = sesion.getAppSettings().getInt("idUsuario", -1);
        String URL = "http://"+con.IP+":8080/moteles/reservaciones/" + String.valueOf(idUsuario);
        barraReservaciones.show();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null){
                            try {
                                for(int i=0;i<response.length();i++){
                                    JSONObject reservacion = response.getJSONObject(i);
                                    Reservacion res = new Reservacion();
                                    res.setResId(reservacion.getInt("resId"));
                                    res.setResCantidadPagar(reservacion.getDouble("resCantidadApagar"));
                                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(reservacion.getString("fecha"));
                                    res.setFecha(date1);
                                    JSONObject habitacion = reservacion.getJSONObject("haId");
                                    res.setNombreHabitacion(habitacion.getString("haNombreHabitacion"));
                                    res.setNumeroHabitacion(habitacion.getInt("haNumeroHabitacion"));
                                    res.setTipoHabitacion(habitacion.getString("haTipoDeHabitacion"));
                                    lista.add(res);
                                    adaptador.notifyDataSetChanged();
                                }

                                barraReservaciones.hide();
                            }catch (JSONException | ParseException e){
                                e.printStackTrace();
                            }

                        }else{
                            barraReservaciones.hide();
                            Toast.makeText(ActivityReservaciones.this,"No se encontraron resultado.",Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            util.mostrarSnack(v, barraReservaciones, error.getMessage());
                        } else if (error instanceof AuthFailureError) {
                            util.mostrarSnack(v,barraReservaciones,error.getMessage());
                        } else if (error instanceof ServerError) {
                            util.mostrarSnack(v,barraReservaciones,error.getMessage());
                        } else if (error instanceof NetworkError) {
                            util.mostrarSnack(v,barraReservaciones,error.getMessage());
                        } else if (error instanceof ParseError) {
                            util.mostrarSnack(v,barraReservaciones,error.getMessage());
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