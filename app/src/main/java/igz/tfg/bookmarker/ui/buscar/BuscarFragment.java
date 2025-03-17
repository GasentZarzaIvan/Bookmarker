package igz.tfg.bookmarker.ui.buscar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.databinding.FragmentBuscarBinding;
import igz.tfg.bookmarker.modelos.adaptadores.buscar.ObrasRecyclerAdapter;
import igz.tfg.bookmarker.modelos.openlibrary.ObraOL;

public class BuscarFragment extends Fragment {
    private final List<ObraOL> listaLibros = new ArrayList<>();
    private NavController navController;
    private RecyclerView recyclerLibros;
    private TextView txtBuscar;
    private ObrasRecyclerAdapter recyclerAdapter;
    private FragmentBuscarBinding binding;
    private BuscarViewModel buscarViewModel;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        buscarViewModel = new ViewModelProvider(this).get(BuscarViewModel.class);
        binding = FragmentBuscarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        establecerOpcionesToolbar();

        recyclerLibros = binding.recycleView;
        recyclerLibros.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new ObrasRecyclerAdapter();
        recyclerLibros.setAdapter(recyclerAdapter);
        progressBar = binding.progressBar;
        txtBuscar = binding.textoBuscar;
        buscarViewModel.getTextoBuscar().observe(getViewLifecycleOwner(), txtBuscar::setText);

        buscarViewModel.getListaLibro().observe(getViewLifecycleOwner(), new Observer<List<ObraOL>>() {
            @Override
            public void onChanged(List<ObraOL> libros) {
                if (libros == null || libros.isEmpty()) {
                    recyclerAdapter.clear();
                    progressBar.setVisibility(View.GONE);
                    recyclerLibros.setVisibility(View.GONE);
                    buscarViewModel.getTextoBuscar().setValue(getResources().getString(R.string.busqueda_obras_vacia));
                    txtBuscar.setVisibility(View.VISIBLE);
                } else {
                    txtBuscar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    recyclerAdapter.setObras(libros);
                    recyclerLibros.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerAdapter.setOnItemClickListener(new ObrasRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ObraOL obra) {
                igz.tfg.bookmarker.ui.buscar.BuscarFragmentDirections.ActionNavigationBuscarToNavigationEdicionesFragment action = igz.tfg.bookmarker.ui.buscar.BuscarFragmentDirections.actionNavigationBuscarToNavigationEdicionesFragment();
                action.setObra(obra);
                navController.navigate(action);
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
                menuInflater.inflate(R.menu.buscar_menu, menu);
                SearchView searchView = (SearchView) menu.findItem(R.id.opc_buscador).getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.d("buscador", query);
                        buscarLibros(query);
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return true;
                    }
                });

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                // Handle option Menu Here
                return false;
            }
        }, getViewLifecycleOwner());
    }

    private void buscarLibros(String query) {
        txtBuscar.setVisibility(View.GONE);
        recyclerLibros.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        buscarViewModel.busquedaGeneral(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray docs;
                    if (response != null) {
                        docs = response.getJSONArray("docs");
                        ArrayList<ObraOL> libros = ObraOL.desdeJson(docs);
                        buscarViewModel.getListaLibro().setValue(libros);
                    } else {
                        buscarViewModel.getTextoBuscar().setValue(getResources().getString(R.string.busqueda_obras_vacia));
                        buscarViewModel.getListaLibro().setValue(new ArrayList<>());
                    }
                } catch (JSONException e) {
                    buscarViewModel.getTextoBuscar().setValue(getResources().getString(R.string.busqueda_error, "Error crear datos de libros."));
                    buscarViewModel.getListaLibro().setValue(new ArrayList<>());
                    Log.e("BUSQUEDA", e.getMessage());
                    e.printStackTrace();
                }
            }

            //TODO corregir cuando da un error (sin internet) y no lo captura UnknwonHostException
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                String error = statusCode + ": " + responseString;
                buscarViewModel.getTextoBuscar().setValue(getResources().getString(R.string.busqueda_error, error));
                buscarViewModel.getListaLibro().setValue(new ArrayList<>());
                Log.w("BUSQUEDA", error + " - " + throwable.getMessage());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}