package igz.tfg.bookmarker.ui.biblioteca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.databinding.FragmentBibliotecaBinding;
import igz.tfg.bookmarker.modelos.adaptadores.biblioteca.SeccionesAdapter;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionConLibros;

public class BibliotecaFragment extends Fragment {

    private FragmentBibliotecaBinding binding;
    private NavController navController;
    private ViewPager2 pager;
    private TabLayout tabLayout;
    private TextView textoBilioteca;
    private SeccionesAdapter seccionesAdapter;

    private BibliotecaViewModel bibliotecaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBibliotecaBinding.inflate(inflater, container, false);
        bibliotecaViewModel = new ViewModelProvider(this).get(BibliotecaViewModel.class);
        View root = binding.getRoot();
        textoBilioteca = binding.textoBiblioteca;
        pager = binding.pager;
        tabLayout = binding.tabLayout;
        establecerOpcionesToolbar();
        bibliotecaViewModel.getAllSeccionesConLibros().observe(getViewLifecycleOwner(), new Observer<List<SeccionConLibros>>() {
            @Override
            public void onChanged(List<SeccionConLibros> seccionConLibros) {
                seccionesAdapter = new SeccionesAdapter(getParentFragment());
                seccionesAdapter.setSeccionConLibros(seccionConLibros);
                pager.setAdapter(seccionesAdapter);
                new TabLayoutMediator(tabLayout, pager, (tab, position) -> {
                    if (!seccionConLibros.isEmpty()) {
                        tab.setText(seccionConLibros.get(position).seccion.getNombre());
                    }
                }).attach();
                if (seccionConLibros.isEmpty()) {
                    textoBilioteca.setVisibility(View.VISIBLE);
                    pager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                } else {
                    textoBilioteca.setVisibility(View.GONE);
                    pager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void establecerOpcionesToolbar() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.biblioteca_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int idSeleccionado = menuItem.getItemId();
                if (idSeleccionado == R.id.opc_nueva_seccion) {
                    crearNuevaSeccion();
                } else if (idSeleccionado == R.id.opc_nuevo_libro) {
                    crearNuevoLibro();
                }
                return false;
            }
        }, getViewLifecycleOwner());
    }

    private void crearNuevoLibro() {
        igz.tfg.bookmarker.ui.biblioteca.BibliotecaFragmentDirections.ActionNavigationBibliotecaToNavigationEditarLibroFragment action = BibliotecaFragmentDirections.actionNavigationBibliotecaToNavigationEditarLibroFragment();
        action.setTitulo("Nuevo libro");
        navController.navigate(action);
    }

    private void crearNuevaSeccion() {
        NavDirections action = BibliotecaFragmentDirections.actionNavigationBibliotecaToNuevaSeccionDialogFragment();
        navController.navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}


