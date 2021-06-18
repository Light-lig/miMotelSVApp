package com.example.mimotelsv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mimotelsv.util.Constantes;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Activity_detalle_motel extends AppCompatActivity {
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private Constantes con  = new Constantes();
    public static int currentPosition;
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    private static final String KEY_CURRENT_POSITION = "com.google.samples.gridtopager.key.currentPosition";
    private static final int NUM_PAGES = 2;
    private String idMotel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_motel);
        viewPager = findViewById(R.id.pagerMotel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        idMotel = intent.getStringExtra("idMotel");
        String nombreMotel = intent.getStringExtra("nombreMotel");
        toolbar.setTitle(nombreMotel);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);


        pagerAdapter = new Activity_detalle_motel.ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tlMotelDetalle);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText((position == 0)?"MOTEL":"HABITACIONES")
        ).attach();
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
    }
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            if(position == 0){
                detalleMotel detallef = detalleMotel.newInstance(idMotel);
                return  detallef;
            }else{
                Habitaciones habitacionesf = Habitaciones.newInstance(idMotel);
                return habitacionesf;
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
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