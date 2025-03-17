package igz.tfg.bookmarker.modelos.adaptadores.buscar;

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
import igz.tfg.bookmarker.modelos.openlibrary.ObraOL;

public class ObrasRecyclerAdapter extends RecyclerView.Adapter<ObrasRecyclerAdapter.LibroHolder> {

    private OnItemClickListener listener;
    private List<ObraOL> obras = new ArrayList<>();

    public void setObras(List<ObraOL> obras) {
        this.obras = obras;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LibroHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obra_item, parent, false);
        return new LibroHolder(view);
    }

    @Override
    public void onBindViewHolder(LibroHolder holder, int position) {
        ObraOL obraOL = obras.get(position);
        holder.txtNombre.setText(obraOL.getTitulo());
        holder.txtInformacion.setText(obraOL.getAutor());
        Picasso.get().load(Uri.parse(obraOL.getPortadaUrl())).placeholder(R.drawable.imagen_no_disponible).into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return obras.size();
    }

    public void clear() {
        obras.clear();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(ObraOL obra);
    }

    class LibroHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtInformacion;
        ImageView foto;

        public LibroHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.idTitulo);
            txtInformacion = itemView.findViewById(R.id.idAutor);
            foto = itemView.findViewById(R.id.idImagen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(obras.get(position));
                    }
                }
            });
        }
    }
}