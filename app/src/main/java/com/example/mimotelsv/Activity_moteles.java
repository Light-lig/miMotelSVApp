package com.example.mimotelsv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
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
import com.example.mimotelsv.util.Constantes;
import com.example.mimotelsv.util.RecyclerItemClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Activity_moteles extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener {
    private MotelesAdapter motelAdaptador;
    private RecyclerView rcvMoteles;
    private Constantes con  = new Constantes();
    SwipeRefreshLayout myRefresh;
    private List<Motel> lista = new ArrayList<>();
    private String municipio = "0";
    private String categoria = "0";
    private String nombre = "null";
    private LinearProgressIndicator barraMoteles;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moteles);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);

        drawerLayout.addDrawerListener(this);


        handleIntent(getIntent());
        myRefresh = findViewById(R.id.swipeContainer);
        rcvMoteles = findViewById(R.id.rcvMoteles);
        motelAdaptador = new MotelesAdapter(lista);
        rcvMoteles.setAdapter(motelAdaptador);
        barraMoteles = findViewById(R.id.progresBarMoteles);
        myRefresh.setRefreshing(false);
        downloadData();
        myRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Intent i = new Intent(Activity_moteles.this, Activity_moteles.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                }
        );
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

    }

        protected void downloadData() {
        barraMoteles.show();
            String URL = "http://"+con.IP+":8080/moteles/lista/"+municipio+"/"+categoria+"/"+nombre;
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
                                barraMoteles.hide();

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


    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            nombre  = query;

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filtros:
                Intent filtros = new Intent(Activity_moteles.this,Filtrar.class);
                startActivity(filtros);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int title;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                title = R.string.menu_home;
                break;
            case R.id.nav_perfil:
                title = R.string.menu_perfil;
                break;
            case R.id.nav_reservaciones:
                title = R.string.menu_reservaciones;
                break;
            case R.id.nav_logout:
                title = R.string.menu_logout;
                break;

            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}