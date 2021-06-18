package com.example.mimotelsv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.example.mimotelsv.adaptadores.ViewPagerAdapter;

import com.example.mimotelsv.jobs.NotificationWorker;
import com.example.mimotelsv.modelos.Habitacion;

import com.example.mimotelsv.modelos.Reservacion;
import com.example.mimotelsv.util.Constantes;
import com.example.mimotelsv.util.Session;

import com.example.mimotelsv.util.Util;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Activity_detalle_habitacion extends AppCompatActivity {
    // creating object of ViewPager
    private Constantes con  = new Constantes();
    private ViewPager mViewPager;
    private TextView txtNombre, txtTipo, txtPrecio, txtTiempo, txtDescripcion, txtEstado;
    private ImageView ivEstado;
    private MaterialButton btnReservar;
    private String idHabitacion;
    private LinearProgressIndicator barraProgreso;
    private Session preferencias;
    private  Habitacion ha = new Habitacion();
    private Reservacion res = new Reservacion();
    private Util util = new Util();
    private Toolbar toolbar;
    // images array
    private List<String> images = new ArrayList<>();

    // Creating Object of ViewPagerAdapter
    private ViewPagerAdapter mViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_habitacion);
         toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        idHabitacion = intent.getStringExtra("idHabitacion");


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        // Initializing the ViewPager Object
        mViewPager = (ViewPager) findViewById(R.id.viewPagerMain);
        txtNombre = findViewById(R.id.txtNombre);
        txtTipo = findViewById(R.id.txtTipo);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtTiempo = findViewById(R.id.txtTiempo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtEstado = findViewById(R.id.txtEstado);
        ivEstado = findViewById(R.id.ivEstadoHabitacion);
        btnReservar = findViewById(R.id.btnReservar);
        barraProgreso = findViewById(R.id.progresBarHabitacion);
        preferencias = new Session(this);

        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(Activity_detalle_habitacion.this, images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);
        View view = findViewById(android.R.id.content).getRootView();
        DownloadTask(view);
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtEstado.getText().equals("Reservado")){

                     new MaterialAlertDialogBuilder(Activity_detalle_habitacion.this)
                            .setTitle("Informacion")
                            .setMessage("Esta seguro?")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reservar(v);
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                            preferencias.borrarDatos("habitacionRes");
                }else{
                    Toast.makeText(getBaseContext(),"La habitacion ya esta reservada intentelo mas tarde.",Toast.LENGTH_LONG).show();
                    preferencias.salvarDatos("habitacionRes","int",String.valueOf(ha.getId()));
                }
            }
        });

    }
    private void DownloadTask(View v) {
            String URL = "http://"+con.IP+":8080/moteles/habitacionIndividual/" + idHabitacion;
            barraProgreso.show();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if(response == null){
                                Toast.makeText(getBaseContext(),"No retorno nada",Toast.LENGTH_LONG).show();
                            }else{
                                try {
                                    //llenando el objeto

                                    ha.setId(response.getInt("haId"));
                                    ha.setNombre(response.getString("haNombreHabitacion"));
                                    toolbar.setTitle(ha.getNombre());
                                    ha.setTipo(response.getString("haTipoDeHabitacion"));
                                    ha.setNumero(response.getInt("haNumeroHabitacion"));
                                    ha.setPrecio(response.getDouble("haPrecio"));
                                    ha.setTiempo(response.getString("haTiempo"));
                                    ha.setDescripcion(response.getString("haDescripcion"));
                                    JSONObject estado = response.getJSONObject("esId");
                                    ha.setEstado(estado.getString("estEstado"));
                                    JSONArray fotos = response.getJSONArray("smFotosList");

                                    //llenando objeto reservacion
                                    res.setResCantidadPagar(ha.getPrecio());

                                    res.setFecha(new Date());
                                    Date currentTime = Calendar.getInstance().getTime();
                                    res.setHora(currentTime.toString());
                                    res.setHaId(ha.getId());
                                    Integer idUsuario = preferencias.getAppSettings().getInt("idUsuario",-1);
                                    res.setUsrId(idUsuario);

                                    txtNombre.setText(ha.getNombre() +" "+ String.valueOf(ha.getNumero()));
                                    txtTipo.setText(ha.getTipo());
                                    txtPrecio.setText("Precio: $" + String.valueOf(ha.getPrecio()));
                                    txtTiempo.setText("Tiempo: " + ha.getTiempo());
                                    txtDescripcion.setText(ha.getDescripcion());
                                    txtEstado.setText(ha.getEstado());
                                    switch (ha.getEstado()){
                                        case "Disponible":
                                            ivEstado.setImageResource(R.drawable.verde);
                                            break;
                                        case "Reservado":
                                            ivEstado.setImageResource(R.drawable.rojo);

                                            break;
                                        default:
                                            ivEstado.setImageResource(R.drawable.amarillo);
                                            break;
                                    }
                                    for(int i=0;i<fotos.length();i++){
                                        // Get current json object
                                        JSONObject foto = fotos.getJSONObject(i);

                                        images.add(foto.getString("fhFoto"));

                                    }
                                    mViewPagerAdapter.notifyDataSetChanged();
                                    barraProgreso.hide();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    barraProgreso.hide();
                                }
                            }
                            barraProgreso.hide();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                util.mostrarSnack(v, barraProgreso, error.getMessage());
                            } else if (error instanceof AuthFailureError) {
                                util.mostrarSnack(v,barraProgreso, error.getMessage());
                            } else if (error instanceof ServerError) {
                                util.mostrarSnack(v,barraProgreso, error.getMessage());
                            } else if (error instanceof NetworkError) {
                                util.mostrarSnack(v,barraProgreso, error.getMessage());
                            } else if (error instanceof ParseError) {
                                util.mostrarSnack(v,barraProgreso, error.getMessage());
                            }
                        }
                    });

            request.setRetryPolicy(new

                    DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            Volley.newRequestQueue(getBaseContext()).add(request);

    }
    public void reservar(View v){
        String URL = "http://"+con.IP+":8080/moteles/reservar";
        Map<String, String> params = new HashMap<String, String>();
        params.put("resCantidadPagar", String.valueOf(res.getResCantidadPagar()));
        String newstring = new SimpleDateFormat("yyyy-MM-dd").format(res.getFecha());
        params.put("fecha", newstring);
        params.put("hora", res.getHora());
        params.put("haId", String.valueOf(res.getHaId()));
        params.put("usrId", String.valueOf(res.getUsrId()));

        barraProgreso.show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null){
                            barraProgreso.hide();
                            Toast.makeText(getBaseContext(),"Ocurrio un error.",Toast.LENGTH_LONG).show();
                        }else{
                            try {
                                barraProgreso.hide();


                                    txtEstado.setText("Reservado");
                                    ivEstado.setImageResource(R.drawable.rojo);
                                Toast.makeText(getBaseContext(),"La habitacion se reservo correctamente.",Toast.LENGTH_LONG).show();
                                Intent reservacion = new Intent(Activity_detalle_habitacion.this, ActivityReservaciones.class);
                                startActivity(reservacion);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            try {
                                JSONObject mensaje = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                                util.mostrarSnack(v, barraProgreso , mensaje.getString("mensaje"));
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }

                        } else if (error instanceof AuthFailureError) {
                            try {
                                JSONObject mensaje = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                                util.mostrarSnack(v, barraProgreso , mensaje.getString("mensaje"));
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (error instanceof ServerError) {
                            try {
                                JSONObject mensaje = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                                util.mostrarSnack(v, barraProgreso , mensaje.getString("mensaje"));
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (error instanceof NetworkError) {
                            try {
                                JSONObject mensaje = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                                util.mostrarSnack(v, barraProgreso , mensaje.getString("mensaje"));
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (error instanceof ParseError) {
                            try {
                                JSONObject mensaje = new JSONObject(new String(error.networkResponse.data,"UTF-8"));
                                util.mostrarSnack(v, barraProgreso , mensaje.getString("mensaje"));
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        request.setRetryPolicy(new

                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(getBaseContext()).add(request);
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