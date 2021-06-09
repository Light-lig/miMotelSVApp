package com.example.mimotelsv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mimotelsv.adaptadores.ViewPagerAdapter;
import com.example.mimotelsv.modelos.Fotos;
import com.example.mimotelsv.modelos.Habitacion;
import com.example.mimotelsv.modelos.Motel;
import com.example.mimotelsv.modelos.Reservacion;
import com.example.mimotelsv.util.Constantes;
import com.example.mimotelsv.util.Session;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_detalle_habitacion extends AppCompatActivity {
    // creating object of ViewPager
    private Constantes con  = new Constantes();
    ViewPager mViewPager;
    TextView txtNombre, txtTipo, txtPrecio, txtTiempo, txtDescripcion, txtEstado;
    ImageView ivEstado;
    MaterialButton btnReservar;
    private String idHabitacion;
    private LinearProgressIndicator barraProgreso;
    private Session preferencias;
    private  Habitacion ha = new Habitacion();
    private Reservacion res = new Reservacion();
    // images array
    List<String> images = new ArrayList<>();

    // Creating Object of ViewPagerAdapter
    ViewPagerAdapter mViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_habitacion);
        Toolbar toolbar = findViewById(R.id.toolbar);
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

        Intent intent = getIntent();
        idHabitacion = intent.getStringExtra("idHabitacion");
        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(Activity_detalle_habitacion.this, images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);
        DownloadTask tarea = new DownloadTask();
        tarea.execute();
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ha.getEstado() != "Reservado"){
                reservar();

                }else{
                    Toast.makeText(getBaseContext(),"La habitacion ya esta reservada intentelo mas tarde.",Toast.LENGTH_LONG).show();

                }
            }
        });

    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String URL = "http://"+con.IP+":8080/moteles/habitacionIndividual/" + idHabitacion;
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


            Volley.newRequestQueue(getBaseContext()).add(request);
            return null;
        }
    }
    public void reservar(){
        String URL = "http://"+con.IP+":8080/moteles/reservar";
        Map<String, String> params = new HashMap<String, String>();
        params.put("resCantidadPagar", String.valueOf(res.getResCantidadPagar()));
        params.put("fecha", res.getFecha().toString());
        params.put("hora", res.getHora());
        params.put("haId", String.valueOf(res.getHaId()));
        params.put("UsrId", String.valueOf(res.getUsrId()));

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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());

                                    builder.setMessage("La habitacion se reservo exito, dispone de media hora para llegar a su destino de lo contrario su tiempo de estancia se acortara." +
                                            "muchas gracias.")
                                            .setTitle("Informacion");
                                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();


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