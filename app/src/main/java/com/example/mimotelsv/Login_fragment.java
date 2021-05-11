package com.example.mimotelsv;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mimotelsv.modelos.Usuario;
import com.example.mimotelsv.repositorio.UsuarioRepositorio;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btnIniciarSesion;
    TextInputLayout txtCorreo, txtContra;
    String URL = "http://192.168.1.10:8080/users/login";
    public Login_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment login_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Login_fragment newInstance(String param1, String param2) {
        Login_fragment fragment = new Login_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_fragment, container, false);
        btnIniciarSesion = view.findViewById(R.id.btnIniciarSession);
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario us = new Usuario();
                UsuarioRepositorio repo = new UsuarioRepositorio();
                txtCorreo = view.findViewById(R.id.txtEmail);
                txtContra = view.findViewById(R.id.txtpass);
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
                                    Toast.makeText(getActivity(),"Usuario o contrasenia no valido",Toast.LENGTH_LONG).show();
                                }else{
                                    Intent moteles = new Intent(getActivity(), Activity_moteles.class);
                                    startActivity(moteles);
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                            }
                        });

                request.setRetryPolicy(new

                        DefaultRetryPolicy(60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                Volley.newRequestQueue(getActivity()).add(request);
            }
        });
        return view;
    }

}