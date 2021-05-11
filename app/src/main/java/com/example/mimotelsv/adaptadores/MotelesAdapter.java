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

import com.example.mimotelsv.R;
import com.example.mimotelsv.modelos.Motel;

import java.util.List;

public class MotelesAdapter extends RecyclerView.Adapter<MotelesAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo, descripcion;
        private RatingBar rating;
        private ImageView fotoPortada;
        public ViewHolder(View itemView){
            super(itemView);
            titulo = itemView.findViewById(R.id.tvTitulo);
            descripcion = itemView.findViewById(R.id.tvDescripcion);
            fotoPortada = itemView.findViewById(R.id.ivPortada);
            rating = itemView.findViewById(R.id.rbRating);
        }
    }
    public List<Motel> listaMoteles;
    //Generar el constructor de la lista creada para que pueda recibir los valores
    public MotelesAdapter(List<Motel> listaMoteles) {
        this.listaMoteles = listaMoteles;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.lista_moteles,parent,false); ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titulo.setText(listaMoteles.get(position).getNombre());
        holder.descripcion.setText(listaMoteles.get(position).getDireccion());
        Bitmap bmp = BitmapFactory.decodeByteArray(listaMoteles.get(position).getImagenProtada(), 0, listaMoteles.get(position).getImagenProtada().length);
        holder.fotoPortada.setImageBitmap(bmp);
        holder.rating.setRating((float) listaMoteles.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return listaMoteles.size();
    }
}
