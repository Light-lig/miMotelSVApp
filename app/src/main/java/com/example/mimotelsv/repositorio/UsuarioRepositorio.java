package com.example.mimotelsv.repositorio;

import android.os.AsyncTask;

import com.example.mimotelsv.modelos.Usuario;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class UsuarioRepositorio {
    String URL = "http://192.168.1.10:8080/users/login";
    public UsuarioRepositorio(){

    }


    public void login(Usuario us){




    }
}
