package com.example.mimotelsv;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.mimotelsv.adaptadores.HabitacionesAdapter;
import com.example.mimotelsv.modelos.Fotos;
import com.example.mimotelsv.modelos.Habitacion;
import com.example.mimotelsv.modelos.Motel;
import com.example.mimotelsv.util.Constantes;
import com.example.mimotelsv.util.RecyclerItemClickListener;
import com.example.mimotelsv.util.Util;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Habitaciones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Habitaciones extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ID_MOTEL = "idMotel";
    private Constantes con  = new Constantes();
    private RecyclerView rvHabitaciones;
    private List<Habitacion> lista = new ArrayList<>();
    private HabitacionesAdapter adaptador;
    private LinearProgressIndicator barraDetalleMotel;
    private Util util = new Util();
    // TODO: Rename and change types of parameters
    private String idMotel;

    public Habitaciones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Habitaciones.
     */
    // TODO: Rename and change types and number of parameters
    public static Habitaciones newInstance(String idMotel) {
        Habitaciones fragment = new Habitaciones();
        Bundle args = new Bundle();
        args.putString(ID_MOTEL, idMotel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idMotel = getArguments().getString(ID_MOTEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_habitaciones, container, false);
        rvHabitaciones = v.findViewById(R.id.rvHabitaciones);
        barraDetalleMotel = v.findViewById(R.id.progressBarHabitaciones);
        barraDetalleMotel.show();
        DownloadTask(v);
        adaptador = new HabitacionesAdapter(lista);
        rvHabitaciones.setAdapter(adaptador);
        rvHabitaciones.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getBaseContext(), rvHabitaciones, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent detalleHabitacion = new Intent(getActivity(), Activity_detalle_habitacion.class);
                detalleHabitacion.putExtra("idHabitacion",String.valueOf(lista.get(position).getId()));

                startActivity(detalleHabitacion);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        return v;
    }
    private void DownloadTask(View v) {
            String URL = "http://"+con.IP+":8080/moteles/habitacion/" + idMotel;
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if(response != null){
                                try {



                                    for(int i=0;i<response.length();i++){
                                        JSONObject habitacion = response.getJSONObject(i);
                                        Habitacion ha = new Habitacion();
                                        ha.setId(habitacion.getInt("haId"));
                                        ha.setNombre(habitacion.getString("haNombreHabitacion") + " - " + habitacion.getInt("haNumeroHabitacion"));
                                        ha.setDescripcion(habitacion.getString("haDescripcion") + " - Tipo: " + habitacion.getString("haTipoDeHabitacion"));
                                        ha.setPrecio(habitacion.getDouble("haPrecio"));
                                        ha.setTiempo(habitacion.getString("haTiempo"));
                                        JSONObject estado = habitacion.getJSONObject("esId");
                                        ha.setEstado(estado.getString("estEstado"));
                                        JSONArray fotos = habitacion.getJSONArray("smFotosList");

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        byte[] imageBytes = baos.toByteArray();
                                        imageBytes = Base64.decode(fotos.getJSONObject(0).getString("fhFoto"),Base64.DEFAULT | Base64.NO_WRAP);
                                        List<Fotos> fotosList = new ArrayList<>();
                                        Fotos fo = new Fotos();
                                        fo.setFoto(imageBytes);
                                        fotosList.add(fo);
                                        ha.setFotos(fotosList);

                                        lista.add(ha);
                                        adaptador.notifyDataSetChanged();
                                    }

                                    barraDetalleMotel.hide();
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
                                util.mostrarSnack(v, barraDetalleMotel);
                            } else if (error instanceof AuthFailureError) {
                                util.mostrarSnack(v,barraDetalleMotel);
                            } else if (error instanceof ServerError) {
                                util.mostrarSnack(v,barraDetalleMotel);
                            } else if (error instanceof NetworkError) {
                                util.mostrarSnack(v,barraDetalleMotel);
                            } else if (error instanceof ParseError) {
                                util.mostrarSnack(v,barraDetalleMotel);
                            }
                        }
                    });

            request.setRetryPolicy(new
                    DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            Volley.newRequestQueue(getActivity()).add(request);

    }
}