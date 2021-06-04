package com.example.mimotelsv.util;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

public class Validaciones {

    public Validaciones() {
    }

    public  boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public boolean validar(TextInputLayout txt){
        if(txt.getEditText().getText().toString().isEmpty()){
            txt.setErrorEnabled(true);
            txt.setError("El campo es requerido");
            return false;
        }
        txt.setErrorEnabled(false);
        return true;
    }
}
