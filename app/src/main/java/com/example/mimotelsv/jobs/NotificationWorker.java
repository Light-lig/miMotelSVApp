package com.example.mimotelsv.jobs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.mimotelsv.Activity_detalle_habitacion;
import com.example.mimotelsv.R;
import com.example.mimotelsv.util.Constantes;
import com.example.mimotelsv.util.Session;
import com.example.mimotelsv.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NotificationWorker extends Worker{
    private Constantes con  = new Constantes();
    private static final String TAG = NotificationWorker.class.getSimpleName();
    public static final int NOTIFICATION_ID = 888;
    public static final String CHANNEL_ID = "Habitaciones";
    private Context contexto;
    private Session preferencias;
    public NotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        preferencias = new Session(context);
        contexto = context;
    }

    @NonNull
    @Override
    public Result doWork() {
       return ConsultarHabitacion();
    }

    public Result ConsultarHabitacion(){
    int idHabitacion = preferencias.getAppSettings().getInt("habitacionRes",-1);
    if(idHabitacion != -1) {
        String URL = "http://" + con.IP + ":8080/moteles/habitacionIndividual/" + String.valueOf(idHabitacion);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, future, future);
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(request);

        try {

            JSONObject response = future.get(60, TimeUnit.SECONDS); // this will block
            Log.d(TAG, response.toString());
            JSONObject haEstado = response.getJSONObject("esId");
            int es = Integer.parseInt(haEstado.getString("estId"));
            if (es == 1) {
                Intent intent = new Intent(contexto, Activity_detalle_habitacion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("idHabitacion", String.valueOf(idHabitacion));
                PendingIntent pendingIntent = PendingIntent.getActivity(contexto, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(contexto, CHANNEL_ID)
                        .setSmallIcon(R.drawable.verde)
                        .setContentTitle("Informacion.")
                        .setContentText("Habitacion: " + response.getString("haNombreHabitacion") + ", ya se encuentra disponible.")
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(contexto);
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(NOTIFICATION_ID, builder.build());
                preferencias.borrarDatos("habitacionRes");
            }

            return Result.success();
        } catch (InterruptedException e) {
            e.printStackTrace();
            // exception handling
            return Result.failure();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return Result.failure();
            // exception handling
        } catch (TimeoutException e) {
            e.printStackTrace();
            return Result.failure();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
        return Result.success();
     }

}
