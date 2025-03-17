package igz.tfg.bookmarker.modelos.adaptadores.libro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.modelos.room.tablas.Marcador;

public class MarcadoresRecyclerAdapter extends RecyclerView.Adapter<MarcadoresRecyclerAdapter.MarcadoresHolder> {
    private OnItemClickListener listener;
    private List<Marcador> marcadores = new ArrayList<>();

    public void setMarcadores(List<Marcador> marcadores) {
        this.marcadores = marcadores;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MarcadoresHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marcador_item, parent, false);
        return new MarcadoresHolder(view);
    }

    @Override
    public void onBindViewHolder(MarcadoresHolder holder, int position) {
        Marcador marcador = marcadores.get(position);
        holder.txtNombre.setText(marcador.getNombre());
        holder.txtPagina.setText(String.valueOf(marcador.getPagina()));
        holder.inpNombre.setText(marcador.getNombre());
        holder.inpPagina.setText(String.valueOf(marcador.getPagina()));
        holder.inpNombre.setHint(marcador.getNombre());
        holder.inpPagina.setHint(String.valueOf(marcador.getPagina()));
        holder.btnEditar.setOnClickListener(v -> {
            holder.inpNombre.setText(marcador.getNombre());
            holder.inpPagina.setText(String.valueOf(marcador.getPagina()));
            holder.inpNombre.setHint(marcador.getNombre());
            holder.inpPagina.setHint(String.valueOf(marcador.getPagina()));
            holder.activarModoEditar();
        });
        holder.btnCancelar.setOnClickListener(v -> {
            holder.txtNombre.setText(marcador.getNombre());
            holder.txtPagina.setText(String.valueOf(marcador.getPagina()));
            holder.activarModoMostrar();
        });


    }

    @Override
    public int getItemCount() {
        return marcadores.size();
    }

    public void clear() {
        marcadores.clear();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(Marcador marcador);
    }

    class MarcadoresHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPagina;
        EditText inpNombre, inpPagina;
        ImageButton btnEditar, btnAceptar, btnCancelar;

        public MarcadoresHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.idNombre);
            txtPagina = itemView.findViewById(R.id.idPagina);
            inpNombre = itemView.findViewById(R.id.inpNombre);
            inpPagina = itemView.findViewById(R.id.inpPagina);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnAceptar = itemView.findViewById(R.id.btnAceptar);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
            activarModoMostrar();
            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        Marcador marcador = marcadores.get(position);
                        marcador.setNombre(inpNombre.getText().toString().trim());
                        String paginaCadena = inpPagina.getText().toString().trim();
                        int pagina = paginaCadena.isEmpty() ? 0 : Integer.parseInt(paginaCadena);
                        marcador.setPagina(pagina);
                        listener.OnItemClick(marcador);
                        txtNombre.setText(marcador.getNombre());
                        txtPagina.setText(String.valueOf(marcador.getPagina()));
                        activarModoMostrar();
                    }
                }
            });
        }

        private void activarModoMostrar() {
            txtNombre.setVisibility(View.VISIBLE);
            txtPagina.setVisibility(View.VISIBLE);
            btnEditar.setVisibility(View.VISIBLE);
            btnAceptar.setVisibility(View.GONE);
            btnCancelar.setVisibility(View.GONE);
            inpNombre.setVisibility(View.GONE);
            inpPagina.setVisibility(View.GONE);
        }

        private void activarModoEditar() {
            txtNombre.setVisibility(View.GONE);
            txtPagina.setVisibility(View.GONE);
            btnEditar.setVisibility(View.GONE);
            btnAceptar.setVisibility(View.VISIBLE);
            btnCancelar.setVisibility(View.VISIBLE);
            inpNombre.setVisibility(View.VISIBLE);
            inpPagina.setVisibility(View.VISIBLE);
        }
    }
}