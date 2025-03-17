package igz.tfg.bookmarker.ui.ajustes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.databinding.FragmentInfoBinding;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.Marcador;
import igz.tfg.bookmarker.modelos.room.tablas.Seccion;


public class InfoFragment extends Fragment {

    private FragmentInfoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        InfoViewModel infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        binding = FragmentInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView txtDatosSecciones = binding.txtDatosSecciones;
        TextView txtDatosLibros = binding.txtDatosLibros;
        TextView txtDatosMarcadores = binding.txtDatosMarcadores;
        infoViewModel.getTxtDatosSecciones().observe(getViewLifecycleOwner(), txtDatosSecciones::setText);
        infoViewModel.getTxtDatosLibros().observe(getViewLifecycleOwner(), txtDatosLibros::setText);
        infoViewModel.getTxtDatosMarcadores().observe(getViewLifecycleOwner(), txtDatosMarcadores::setText);
        infoViewModel.getSeccion().observe(getViewLifecycleOwner(), new Observer<List<Seccion>>() {
            @Override
            public void onChanged(List<Seccion> secciones) {
                infoViewModel.getTxtDatosSecciones().setValue(getResources().getString(R.string.info_numero_secciones, secciones.size()));
            }
        });
        infoViewModel.getLibros().observe(getViewLifecycleOwner(), new Observer<List<Libro>>() {
            @Override
            public void onChanged(List<Libro> libros) {
                infoViewModel.getTxtDatosLibros().setValue(getResources().getString(R.string.info_numero_libros, libros.size()));
            }
        });
        infoViewModel.getMarcadores().observe(getViewLifecycleOwner(), new Observer<List<Marcador>>() {
            @Override
            public void onChanged(List<Marcador> marcadores) {
                infoViewModel.getTxtDatosMarcadores().setValue(getResources().getString(R.string.info_numero_marcadores, marcadores.size()));
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}