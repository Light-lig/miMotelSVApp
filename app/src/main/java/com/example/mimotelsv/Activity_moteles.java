package com.example.mimotelsv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mimotelsv.adaptadores.MotelesAdapter;
import com.example.mimotelsv.modelos.Motel;
import com.example.mimotelsv.util.RecyclerItemClickListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Activity_moteles extends AppCompatActivity {
    private MotelesAdapter motelAdaptador;
    private RecyclerView rcvMoteles;
    String URL = "http://192.168.1.10:8080/moteles/lista";
    private List<Motel> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moteles);


        rcvMoteles = findViewById(R.id.rcvMoteles);
        rcvMoteles.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        motelAdaptador = new MotelesAdapter(lista);
        rcvMoteles.setAdapter(motelAdaptador);

        rcvMoteles.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rcvMoteles, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent detalleMotel = new Intent(Activity_moteles.this, Activity_detalle_motel.class);
                detalleMotel.putExtra("idMotel",String.valueOf(lista.get(position).getId()));
                startActivity(detalleMotel);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null){
                            try {



                                for(int i=0;i<response.length();i++){
                                    // Get current json object
                                    JSONObject motel = response.getJSONObject(i);
                                    Motel mo = new Motel();
                                    // Get the current student (json object) data
                                    mo.setId(motel.getInt("moId"));
                                    mo.setNombre(motel.getString("moNombre"));
                                    mo.setDireccion(motel.getString("moDireccion"));
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    byte[] imageBytes = baos.toByteArray();
                                    imageBytes = Base64.decode(motel.getString("moFotoPortada"),Base64.DEFAULT | Base64.NO_WRAP);
                                    mo.setImagenProtada(imageBytes);
                                    mo.setRating(motel.getDouble("rating"));

                                    lista.add(mo);
                                    motelAdaptador.notifyDataSetChanged();
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
                        Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                });

        request.setRetryPolicy(new

                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(this).add(request);
    }
}