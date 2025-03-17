package igz.tfg.bookmarker.ui.ediciones;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.databinding.FragmentEdicionesBinding;
import igz.tfg.bookmarker.modelos.adaptadores.ediciones.EdicionesRecyclerAdapter;
import igz.tfg.bookmarker.modelos.openlibrary.EdicionOL;
import igz.tfg.bookmarker.modelos.openlibrary.ObraOL;

public class EdicionesFragment extends Fragment {
    private NavController navController;
    private RecyclerView recyclerLibros;
    private TextView txtBuscar;
    private EdicionesRecyclerAdapter recyclerAdapter;
    private FragmentEdicionesBinding binding;
    private EdicionesViewModel edicionesViewModel;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        edicionesViewModel = new ViewModelProvider(this).get(EdicionesViewModel.class);
        if (getArguments() != null) {
            igz.tfg.bookmarker.ui.ediciones.EdicionesFragmentArgs args = EdicionesFragmentArgs.fromBundle(getArguments());
            edicionesViewModel.setObra(args.getObra());
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEdicionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);
        TextView obraTitulo = binding.idTitulo;
        TextView obraAutor = binding.idAutor;
        TextView obraDescripcion = binding.idDescripcion;
        ImageView obraPortada = binding.idImagen;
        if (edicionesViewModel.getObra() != null) {
            ObraOL obra = edicionesViewModel.getObra();
            obraTitulo.setText(obra.getTitulo());
            obraAutor.setText(obra.getAutor());
            if (obra.getDescripcion().isEmpty()) {
                obraDescripcion.setVisibility(View.GONE);
            } else {
                obraDescripcion.setText(obra.getDescripcion());
            }
            Picasso.get().load(Uri.parse(obra.getPortadaUrl())).placeholder(R.drawable.ic_book_24).into(obraPortada);
        }
        recyclerLibros = binding.recycleView;
        recyclerLibros.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new EdicionesRecyclerAdapter();
        recyclerLibros.setAdapter(recyclerAdapter);
        progressBar = binding.progressBar;
        txtBuscar = binding.textoBuscar;
        edicionesViewModel.getTextoBuscar().observe(getViewLifecycleOwner(), txtBuscar::setText);
        edicionesViewModel.getListaEdiciones().observe(getViewLifecycleOwner(), new Observer<List<EdicionOL>>() {
            @Override
            public void onChanged(List<EdicionOL> ediciones) {
                if (ediciones == null || ediciones.isEmpty()) {
                    recyclerAdapter.clear();
                    progressBar.setVisibility(View.GONE);
                    recyclerLibros.setVisibility(View.GONE);
                    edicionesViewModel.getTextoBuscar().setValue(getResources().getString(R.string.busqueda_ediciones_vacia));
                    txtBuscar.setVisibility(View.VISIBLE);
                } else {
                    txtBuscar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    recyclerAdapter.setEdiciones(ediciones);
                    recyclerLibros.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerAdapter.setOnItemClickListener(new EdicionesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(EdicionOL edicion) {
                igz.tfg.bookmarker.ui.ediciones.EdicionesFragmentDirections.ActionNavigationEdicionesFragmentToNavigationEditarLibroFragment action = EdicionesFragmentDirections.actionNavigationEdicionesFragmentToNavigationEditarLibroFragment();
                action.setEdicion(edicion);
                navController.navigate(action);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        if (edicionesViewModel.getObra() != null) {
            ObraOL obra = edicionesViewModel.getObra();
            buscarLibros(obra);
        }
    }

    private void buscarLibros(ObraOL obra) {
        txtBuscar.setVisibility(View.GONE);
        recyclerLibros.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        edicionesViewModel.busquedaEdiciones(obra.getIdWorks(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray docs;
                    if (response != null) {
                        docs = response.getJSONArray("entries");
                        List<EdicionOL> ediciones = EdicionOL.desdeJson(docs, obra);
                        edicionesViewModel.getListaEdiciones().setValue(ediciones);
                    } else {
                        edicionesViewModel.getTextoBuscar().setValue(getResources().getString(R.string.busqueda_ediciones_vacia));
                        edicionesViewModel.getListaEdiciones().setValue(new ArrayList<>());
                    }
                } catch (JSONException e) {
                    edicionesViewModel.getTextoBuscar().setValue(getResources().getString(R.string.busqueda_error, "Error crear datos de libros."));
                    edicionesViewModel.getListaEdiciones().setValue(new ArrayList<>());
                    Log.e("BUSQUEDA", e.getMessage());
                    e.printStackTrace();
                }
            }

            //TODO corregir cuando da un error (sin internet) y no lo captura UnknwonHostException
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                String error = statusCode + ": " + responseString;
                edicionesViewModel.getTextoBuscar().setValue(getResources().getString(R.string.busqueda_error, error));
                edicionesViewModel.getListaEdiciones().setValue(new ArrayList<>());
                Log.w("EDICIONES", error + " - " + throwable.getMessage());
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