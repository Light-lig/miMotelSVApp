package com.example.mimotelsv.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mimotelsv.R;
import com.example.mimotelsv.modelos.Reservacion;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReservacionesAdapter extends RecyclerView.Adapter<ReservacionesAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNumeroReservacion, tvFecha, tvCantidadPagar, tvNumeroHabitacion, tvNombre, tvTipo;
        public ViewHolder(View itemView){
            super(itemView);
            tvNumeroReservacion = itemView.findViewById(R.id.txtNOrden);
            tvFecha = itemView.findViewById(R.id.txtFecha);
            tvCantidadPagar = itemView.findViewById(R.id.txtCantidadPagar);
            tvNumeroHabitacion = itemView.findViewById(R.id.txtNumeroHabitacion);
            tvNombre = itemView.findViewById(R.id.txtNombreHabitacion);
            tvTipo = itemView.findViewById(R.id.tvTipoHabitacion);
        }
    }
    public List<Reservacion> lista;
    public ReservacionesAdapter(List<Reservacion> listaRes) {
                this.lista = listaRes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.lista_ticket,parent,false); ReservacionesAdapter.ViewHolder viewHolder = new ReservacionesAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNumeroReservacion.setText(String.valueOf(lista.get(position).getResId()));
        String newstring = new SimpleDateFormat("yyyy-MM-dd").format(lista.get(position).getFecha());
        holder.tvFecha.setText(newstring);
        holder.tvCantidadPagar.setText("$"+String.valueOf(lista.get(position).getResCantidadPagar()));
        holder.tvNumeroHabitacion.setText(String.valueOf(lista.get(position).getNumeroHabitacion()));
        holder.tvNombre.setText(lista.get(position).getNombreHabitacion());
        holder.tvTipo.setText(lista.get(position).getTipoHabitacion());
    }

    @Override
    public int getItemCount() {
       return lista.size();
    }


}
