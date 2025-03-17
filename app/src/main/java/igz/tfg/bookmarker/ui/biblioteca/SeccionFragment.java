package igz.tfg.bookmarker.ui.biblioteca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.databinding.FragmentSeccionBinding;
import igz.tfg.bookmarker.modelos.adaptadores.biblioteca.LibrosRecyclerAdapter;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionConLibros;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeccionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeccionFragment extends Fragment {

    public static final String POSICION_SECCION = "posicionSeccion";
    private FragmentSeccionBinding binding;
    private NavController navController;

    private int posicionSeccion;
    private BibliotecaViewModel bibliotecaViewModel;

    public SeccionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param posicion Posicion de la seccion en la lista SeccionesConLibros en BibliotecaViewModel.
     * @return A new instance of fragment SeccionFragment.
     */
    public static SeccionFragment newInstance(int posicion) {
        SeccionFragment fragment = new SeccionFragment();
        Bundle args = new Bundle();
        args.putInt(POSICION_SECCION, posicion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            posicionSeccion = getArguments().getInt(POSICION_SECCION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSeccionBinding.inflate(inflater, container, false);
        bibliotecaViewModel = new ViewModelProvider(this).get(BibliotecaViewModel.class);
        View root = binding.getRoot();
        TextView txtSeccion = binding.textoSeccion;
        RecyclerView recyclerLibros = binding.recyclerLibros;
        recyclerLibros.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerLibros.setHasFixedSize(false);
        LibrosRecyclerAdapter recyclerAdapter = new LibrosRecyclerAdapter();
        recyclerLibros.setAdapter(recyclerAdapter);
        bibliotecaViewModel.getAllSeccionesConLibros().observe(getViewLifecycleOwner(), new Observer<List<SeccionConLibros>>() {
            @Override
            public void onChanged(List<SeccionConLibros> seccionConLibros) {
                List<Libro> libros = seccionConLibros.get(posicionSeccion).libros;
                if (libros == null || libros.isEmpty()) {
                    recyclerAdapter.clear();
                    recyclerLibros.setVisibility(View.GONE);
                    txtSeccion.setVisibility(View.VISIBLE);
                } else {
                    recyclerAdapter.setLibros(seccionConLibros.get(posicionSeccion).libros);
                    recyclerLibros.setVisibility(View.VISIBLE);
                    txtSeccion.setVisibility(View.GONE);
                }
            }
        });
        recyclerAdapter.setOnItemClickListener(new LibrosRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Libro libro) {
                igz.tfg.bookmarker.ui.biblioteca.BibliotecaFragmentDirections.ActionNavigationBibliotecaToLibroFragment action = BibliotecaFragmentDirections.actionNavigationBibliotecaToLibroFragment();
                action.setIdLibro(libro.getLibroId());
                navController.navigate(action);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
    }

}