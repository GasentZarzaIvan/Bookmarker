package igz.tfg.bookmarker.modelos.adaptadores.biblioteca;

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
import igz.tfg.bookmarker.modelos.room.tablas.Libro;

public class LibrosRecyclerAdapter extends RecyclerView.Adapter<LibrosRecyclerAdapter.LibroHolder> {
    private OnItemClickListener listener;
    private List<Libro> libros = new ArrayList<>();

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LibroHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.obra_item, parent, false);
        return new LibroHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroHolder holder, int position) {
        Libro libroActual = libros.get(position);
        holder.txtTitulo.setText(libroActual.getTitulo());
        holder.txtSubtitulo.setText(libroActual.getAutor());
        Picasso.get().load(Uri.parse(libroActual.getRutaPortada())).placeholder(R.drawable.ic_book_24).into(holder.imvImagen);
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public void clear() {
        this.libros.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(Libro libro);
    }

    class LibroHolder extends RecyclerView.ViewHolder {
        final TextView txtTitulo;
        final TextView txtSubtitulo;
        final ImageView imvImagen;

        public LibroHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.idTitulo);
            txtSubtitulo = itemView.findViewById(R.id.idAutor);
            imvImagen = itemView.findViewById(R.id.idImagen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(libros.get(position));
                    }
                }
            });
        }
    }
}
