package ateneatech.room4u.Request;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ateneatech.room4u.R;

public class AdaptadorAnuncios extends RecyclerView.Adapter<AdaptadorAnuncios.ViewHolderAnuncios> {

    ArrayList<Anuncios>  listDatos;

    public AdaptadorAnuncios(ArrayList<Anuncios> listDatos) {
        this.listDatos = listDatos;

    }

    @NonNull
    @Override
    public ViewHolderAnuncios onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.anuncios_list,null,false);
        return new ViewHolderAnuncios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAnuncios viewHolderAnuncios, int i) {
        viewHolderAnuncios.titulo.setText(listDatos.get(i).getTitulo());
        viewHolderAnuncios.descripcion.setText(listDatos.get(i).getDescripcion());
        viewHolderAnuncios.correo.setText(listDatos.get(i).getCorreo());
        viewHolderAnuncios.telefono.setText(listDatos.get(i).getTelefono());
        viewHolderAnuncios.precio.setText(listDatos.get(i).getPrecio());

    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderAnuncios extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView descripcion;
        TextView correo;
        TextView telefono;
        TextView precio;

        public ViewHolderAnuncios(@NonNull View itemView) {
            super(itemView);
            titulo =itemView.findViewById(R.id.idTitulo);
            descripcion=itemView.findViewById(R.id.idDescripcion);
            correo =itemView.findViewById(R.id.idCorreo);
            telefono=itemView.findViewById(R.id.idTelefono);
            precio =itemView.findViewById(R.id.idPrecio);
        }


    }
}
