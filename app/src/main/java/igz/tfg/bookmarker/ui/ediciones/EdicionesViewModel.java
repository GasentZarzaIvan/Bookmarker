package igz.tfg.bookmarker.ui.ediciones;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.modelos.openlibrary.EdicionOL;
import igz.tfg.bookmarker.modelos.openlibrary.ObraOL;
import igz.tfg.bookmarker.modelos.room.BibliotecaRepository;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;

public class EdicionesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<EdicionOL>> listaEdiciones;
    private final MutableLiveData<String> textoBuscar;
    private final BibliotecaRepository repository;
    private ObraOL obra;

    public EdicionesViewModel(@NonNull Application application) {
        super(application);
        repository = new BibliotecaRepository(application);
        listaEdiciones = new MutableLiveData<>();
        listaEdiciones.setValue(new ArrayList<>());
        textoBuscar = new MutableLiveData<>();
        textoBuscar.setValue(String.valueOf(R.string.busqueda_ediciones_vacia));
        obra = null;
    }

    public MutableLiveData<String> getTextoBuscar() {
        return textoBuscar;
    }

    public void insert(Libro libro) {
        repository.insert(libro);
    }

    public void busquedaEdiciones(String busqueda, JsonHttpResponseHandler handler) {
        repository.busquedaEdiciones(busqueda, handler);
    }

    public MutableLiveData<List<EdicionOL>> getListaEdiciones() {
        return listaEdiciones;
    }

    public ObraOL getObra() {
        return obra;
    }

    public void setObra(ObraOL obra) {
        this.obra = obra;
    }
}