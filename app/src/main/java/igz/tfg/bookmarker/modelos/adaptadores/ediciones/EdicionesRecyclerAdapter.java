package igz.tfg.bookmarker.modelos.adaptadores.ediciones;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.modelos.openlibrary.EdicionOL;

public class EdicionesRecyclerAdapter extends RecyclerView.Adapter<EdicionesRecyclerAdapter.EdicionesHolder> {

    private OnItemClickListener listener;
    private List<EdicionOL> ediciones = new ArrayList<>();

    public void setEdiciones(List<EdicionOL> ediciones) {
        this.ediciones = ediciones;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EdicionesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edicion_item, parent, false);
        return new EdicionesHolder(view);
    }

    @Override
    public void onBindViewHolder(EdicionesHolder holder, int position) {
        EdicionOL edicion = ediciones.get(position);
        holder.txtNombre.setText(edicion.getTitulo());
        holder.txtInformacion.setText(edicion.getAutor());
        Picasso.get().load(Uri.parse(edicion.getPortadaUrl())).placeholder(R.drawable.imagen_no_disponible).into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return ediciones.size();
    }

    public void clear() {
        ediciones.clear();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(EdicionOL edicion);
    }

    class EdicionesHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtInformacion;
        ImageView foto;

        public EdicionesHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.idTitulo);
            txtInformacion = itemView.findViewById(R.id.idAutor);
            foto = itemView.findViewById(R.id.idImagen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(ediciones.get(position));
                    }
                }
            });
        }
    }
}