package com.example.mimotelsv.util;

import android.view.View;

import com.example.mimotelsv.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

public class Util {
    public Util(){

    }

    public void mostrarSnack(View v, LinearProgressIndicator barraProgreso){

        Snackbar.make(v, R.string.text_error_connection, Snackbar.LENGTH_LONG).setAction(R.string.action_text, new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        }).show();
        if(barraProgreso != null){
            barraProgreso.hide();
        }
    }
}
