package com.example.mimotelsv;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.example.mimotelsv.modelos.Motel;
import com.example.mimotelsv.util.Constantes;
import com.example.mimotelsv.util.Util;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detalleMotel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detalleMotel extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ID_MOTEL = "idMotel";
    private Constantes con  = new Constantes();
    // TODO: Rename and change types of parameters
    private String idMotel;
    private ImageView imagenPortada;
    private TextView txtNombre, txtDireccion, txtHoraApertura, txtHoraCierre,txtRating;
    private RatingBar rating;
    private LinearProgressIndicator barraDetalleMotel;
    private Util util = new Util();
    MapView mapView;
    GoogleMap map;
    private  Motel mo = new Motel();
    public detalleMotel() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment detalleMotel.
     */
    // TODO: Rename and change types and number of parameters
    public static detalleMotel newInstance(String idMotel) {
        detalleMotel fragment = new detalleMotel();
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
        View v = inflater.inflate(R.layout.fragment_detalle_motel, container, false);
        mapView = (MapView) v.findViewById(R.id.mapa);
        imagenPortada = v.findViewById(R.id.ivPortada);
        txtNombre = v.findViewById(R.id.txtNombre);
        txtDireccion = v.findViewById(R.id.txtDireccion);
        txtHoraApertura = v.findViewById(R.id.txtHoraApertura);
        txtHoraCierre = v.findViewById(R.id.txtHoraCierre);
        txtRating = v.findViewById(R.id.txtRating);
        rating = v.findViewById(R.id.rbRatingMotel);
        barraDetalleMotel = v.findViewById(R.id.progressBarDetalleMotel);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
        barraDetalleMotel.show();
        String URL = "http://"+con.IP+":8080/moteles/oneMotel/" + idMotel;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null){
                            Toast.makeText(getActivity(),"No retorno nada",Toast.LENGTH_LONG).show();
                        }else{
                            try {
                                //llenando el objeto

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte[] imageBytes = baos.toByteArray();
                                imageBytes = Base64.decode(response.getString("moFotoPortada"),Base64.DEFAULT | Base64.NO_WRAP);
                                mo.setImagenProtada(imageBytes);
                                mo.setNombre(response.getString("moNombre"));
                                mo.setDireccion(response.getString("moDireccion"));
                                mo.setHoraApertura(response.getString("moHoraApertura"));
                                mo.setHoraCierre(response.getString("moHoraCierre"));
                                mo.setRating(response.getDouble("rating"));
                                mo.setLatitud(response.getDouble("moLatitud"));
                                mo.setLongitud(response.getDouble("moLongitud"));

                                //llenando view
                                Bitmap bmp = BitmapFactory.decodeByteArray(mo.getImagenProtada(), 0, mo.getImagenProtada().length);
                                imagenPortada.setImageBitmap(bmp);
                                txtNombre.setText(mo.getNombre());
                                txtDireccion.setText(mo.getDireccion());
                                txtHoraApertura.setText("Hora apertura: " +mo.getHoraApertura());
                                txtHoraCierre.setText("Hora cierre: "+mo.getHoraCierre());
                                txtRating.setText("Calificacion: "+String.valueOf(mo.getRating()));
                                rating.setRating((float)mo.getRating());

                                LatLng motelHubicacion =  new LatLng(mo.getLatitud(), mo.getLongitud());
                                map.addMarker(new MarkerOptions().position(motelHubicacion).title(mo.getNombre()));
                                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                        motelHubicacion, 15);
                                map.animateCamera(location);
                                barraDetalleMotel.hide();
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
                            util.mostrarSnack(v, barraDetalleMotel, error.getMessage());
                        } else if (error instanceof AuthFailureError) {
                            util.mostrarSnack(v,barraDetalleMotel, error.getMessage());
                        } else if (error instanceof ServerError) {
                            util.mostrarSnack(v,barraDetalleMotel, error.getMessage());
                        } else if (error instanceof NetworkError) {
                            util.mostrarSnack(v,barraDetalleMotel, error.getMessage());
                        } else if (error instanceof ParseError) {
                            util.mostrarSnack(v,barraDetalleMotel, error.getMessage());
                        }
                    }
                });

        request.setRetryPolicy(new

                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(getActivity()).add(request);
        return v;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled(true);


    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}