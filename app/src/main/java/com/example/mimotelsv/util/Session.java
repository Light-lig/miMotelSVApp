package com.example.mimotelsv.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private Constantes AppConstants = new Constantes();
    private SharedPreferences appSettings;
    private Context mContext;
    public Session(Context context){
        appSettings = context.getSharedPreferences(AppConstants.SHARED_SETTINGS_NAME, context.MODE_PRIVATE);
    }
    public SharedPreferences getAppSettings(){
        return this.appSettings;
    }
    public void salvarDatos(String clave, String tipo, String valor){
        SharedPreferences.Editor editor = appSettings.edit();
        switch(tipo){
            case "string" :
                    editor.putString(clave,valor);
            break;
            default:
                    editor.putInt(clave,Integer.parseInt(valor));
                break;
        }
        editor.commit();
    }

    public void borrarDatos(String clave){
        SharedPreferences.Editor editor = appSettings.edit();
        editor.remove(clave);
        editor.commit();
    }
}
