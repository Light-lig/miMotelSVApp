package com.example.mimotelsv.adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mimotelsv.Habitaciones;
import com.example.mimotelsv.R;
import com.example.mimotelsv.modelos.Habitacion;
import com.example.mimotelsv.modelos.Motel;

import java.util.List;

public class HabitacionesAdapter extends RecyclerView.Adapter<HabitacionesAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo, descripcion, txtPrecio, txtTiempo;
        private ImageView fotoPortada, ivEstado;
        public ViewHolder(View itemView){
            super(itemView);
            titulo = itemView.findViewById(R.id.tvTitulo);
            descripcion = itemView.findViewById(R.id.tvDescripcion);
            fotoPortada = itemView.findViewById(R.id.ivPortada);
            txtPrecio = itemView.findViewById(R.id.tvPrecio);
            txtTiempo = itemView.findViewById(R.id.tvTiempo);
            ivEstado = itemView.findViewById(R.id.ivEstado);
        }
    }
    public List<Habitacion> listaHabitaciones;
    //Generar el constructor de la lista creada para que pueda recibir los valores
    public HabitacionesAdapter(List<Habitacion> listaHabitaciones) {
        this.listaHabitaciones = listaHabitaciones;
    }
    @NonNull
    @Override
    public HabitacionesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.lista_habitaciones,parent,false); HabitacionesAdapter.ViewHolder viewHolder = new HabitacionesAdapter.ViewHolder(view);
        return viewHolder;

    }



    @Override
    public void onBindViewHolder(@NonNull HabitacionesAdapter.ViewHolder holder, int position) {
        holder.titulo.setText(listaHabitaciones.get(position).getNombre());
        holder.descripcion.setText(listaHabitaciones.get(position).getDescripcion());
        Bitmap bmp = BitmapFactory.decodeByteArray(listaHabitaciones.get(position).getFotos().get(0).getFoto(), 0, listaHabitaciones.get(position).getFotos().get(0).getFoto().length);
        holder.fotoPortada.setImageBitmap(bmp);
        holder.txtPrecio.setText("$"+String.valueOf(listaHabitaciones.get(position).getPrecio()));
        holder.txtTiempo.setText(listaHabitaciones.get(position).getTiempo());
       switch (listaHabitaciones.get(position).getEstado()){
           case "Disponible":
                holder.ivEstado.setImageResource(R.drawable.verde);
               break;
           case "Reservado":
               holder.ivEstado.setImageResource(R.drawable.rojo);

               break;
           default:
               holder.ivEstado.setImageResource(R.drawable.amarillo);
               break;
       }
    }

    @Override
    public int getItemCount() {
        return listaHabitaciones.size();
    }
}
