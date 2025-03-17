package igz.tfg.bookmarker.ui.libro;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.databinding.FragmentLibroBinding;
import igz.tfg.bookmarker.modelos.adaptadores.libro.MarcadoresRecyclerAdapter;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.LibroConMarcadores;
import igz.tfg.bookmarker.modelos.room.tablas.Marcador;

public class LibroFragment extends Fragment {

    private NavController navController;
    private RecyclerView recyclerLibros;
    private TextView txtSinMarcadores;
    private MarcadoresRecyclerAdapter recyclerAdapter;
    private FragmentLibroBinding binding;
    private LibroViewModel libroViewModel;
    private Libro libro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        libroViewModel = new ViewModelProvider(this).get(LibroViewModel.class);
        if (getArguments() != null) {
            LibroFragmentArgs args = LibroFragmentArgs.fromBundle(getArguments());
            if (args.getIdLibro() > -1) libroViewModel.setLibroId(args.getIdLibro());
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLibroBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setDisplayShowHomeEnabled(true);
        }
        establecerOpcionesToolbar();
        TextView txtTitulo = binding.idTitulo;
        TextView txtAutor = binding.idAutor;
        TextView txtEditorial = binding.idEditorial;
        TextView txtDescripcion = binding.idDescripcion;
        ImageView imvPortada = binding.idImagen;
        TextView txtIsbn10 = binding.idIsbn10;
        TextView txtIsbn13 = binding.idIsbn13;
        TextView txtFechaPublicacion = binding.idFechaPublicacion;
        TextView txtNumPag = binding.idNumPag;

        recyclerLibros = binding.recycleView;
        recyclerLibros.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new MarcadoresRecyclerAdapter();
        recyclerLibros.setAdapter(recyclerAdapter);
        txtSinMarcadores = binding.textoBuscar;
        libroViewModel.getTextoBuscar().observe(getViewLifecycleOwner(), txtSinMarcadores::setText);
        libroViewModel.getLibroConMarcadores().observe(getViewLifecycleOwner(), new Observer<LibroConMarcadores>() {
            @Override
            public void onChanged(LibroConMarcadores libroConMarcadores) {
                libro = libroConMarcadores.libro;
                if (libro != null) {
                    ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                    if (toolbar != null) {
                        toolbar.setTitle(libro.getTitulo());
                    }
                    txtTitulo.setText(libro.getTitulo().isEmpty() ? "Sin titulo" : libro.getTitulo());
                    txtAutor.setText(libro.getAutor().isEmpty() ? "" : libro.getAutor());
                    txtEditorial.setText(libro.getEditorial().isEmpty() ? "" : libro.getEditorial());
                    txtDescripcion.setText(libro.getDescripcion().isEmpty() ? "N/A" : libro.getDescripcion());
                    txtIsbn10.setText(libro.getIsbn10().isEmpty() ? "N/A" : libro.getIsbn10());
                    txtIsbn13.setText(libro.getIsbn13().isEmpty() ? "N/A" : libro.getIsbn13());
                    txtFechaPublicacion.setText(libro.getFechaPublicacion().isEmpty() ? "N/A" : libro.getFechaPublicacion());
                    txtNumPag.setText(String.valueOf(libro.getNumPaginas()).isEmpty() ? "N/A" : String.valueOf(libro.getNumPaginas()));
                    Picasso.get().load(Uri.parse(libro.getRutaPortada())).placeholder(R.drawable.ic_book_24).into(imvPortada);
                }
                List<Marcador> marcadores = libroConMarcadores.marcadores;
                if (marcadores == null || marcadores.isEmpty()) {
                    recyclerAdapter.clear();
                    recyclerLibros.setVisibility(View.GONE);
                    libroViewModel.getTextoBuscar().setValue(getResources().getString(R.string.libro_marcadores_vacio));
                    txtSinMarcadores.setVisibility(View.VISIBLE);
                } else {
                    txtSinMarcadores.setVisibility(View.GONE);
                    recyclerAdapter.setMarcadores(marcadores);
                    recyclerLibros.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerAdapter.setOnItemClickListener(new MarcadoresRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Marcador marcador) {
                libroViewModel.updateMarcador(marcador);
            }
        });
        return root;
    }

    private void establecerOpcionesToolbar() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.libro_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int idSeleccionado = menuItem.getItemId();
                if (idSeleccionado == R.id.opc_modificar_libro) {
                    modificarLibro();
                } else if (idSeleccionado == R.id.opc_nuevo_marcador) {
                    crearNuevoMarcador();
                }
                return false;
            }
        }, getViewLifecycleOwner());
    }


    private void modificarLibro() {
        if (libro != null) {
            igz.tfg.bookmarker.ui.libro.LibroFragmentDirections.ActionLibroFragmentToNavigationEditarLibroFragment action = LibroFragmentDirections.actionLibroFragmentToNavigationEditarLibroFragment();
            action.setTitulo("Editar libro");
            action.setIdLibro(libro.getLibroId());
            navController.navigate(action);
        }
    }

    private void crearNuevoMarcador() {
        if (libro != null) {
            libroViewModel.addMarcador(libro);
        } else {
            Toast.makeText(getContext(), "No se ha podido crear un nuevo marcador", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}