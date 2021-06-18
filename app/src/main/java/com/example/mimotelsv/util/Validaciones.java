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
        if(txt.getEditText().getText().toString().isEmpty()) {
            txt.setErrorEnabled(true);
            txt.setError("El campo es requerido.");
            return false;
        }
        if(txt.getEditText().getText().toString().contains(" ")){
            txt.setErrorEnabled(true);
            txt.setError("El valor no debe contener espacios en blanco");
            return false;
        }
        txt.setErrorEnabled(false);
        return true;
    }

    public boolean equalPasswords(TextInputLayout txt1, TextInputLayout txt2){
        if(!txt1.getEditText().getText().toString().equals(txt2.getEditText().getText().toString())){
            txt2.setErrorEnabled(true);
            txt2.setError("Las contraseñas son diferentes.");
            return false;
        }
        txt2.setErrorEnabled(false);
        return true;
    }

    public boolean caracteresMinimos(TextInputLayout txt, int cantidad){
        if(txt.getEditText().getText().toString().length() < cantidad){
            txt.setErrorEnabled(true);
            txt.setError("El campo debe contener al menos " + String.valueOf(cantidad) + " caracteres.");
            return false;
        }
        txt.setErrorEnabled(false);
        return true;
    }

}
