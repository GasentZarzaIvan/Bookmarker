package igz.tfg.bookmarker.ui.buscar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.modelos.openlibrary.ObraOL;
import igz.tfg.bookmarker.modelos.room.BibliotecaRepository;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;

public class BuscarViewModel extends AndroidViewModel {

    private final MutableLiveData<List<ObraOL>> listaLibro;
    private final MutableLiveData<String> textoBuscar;
    private final BibliotecaRepository repository;

    public BuscarViewModel(@NonNull Application application) {
        super(application);
        repository = new BibliotecaRepository(application);
        listaLibro = new MutableLiveData<>();
        listaLibro.setValue(new ArrayList<>());
        textoBuscar = new MutableLiveData<>();
        textoBuscar.setValue(String.valueOf(R.string.busqueda_obras_vacia));
    }

    public MutableLiveData<String> getTextoBuscar() {
        return textoBuscar;
    }

    public void insert(Libro libro) {
        repository.insert(libro);
    }

    public void busquedaGeneral(String busqueda, JsonHttpResponseHandler handler) {
        repository.busquedaGeneral(busqueda, handler);
    }

    public MutableLiveData<List<ObraOL>> getListaLibro() {
        return listaLibro;
    }

}