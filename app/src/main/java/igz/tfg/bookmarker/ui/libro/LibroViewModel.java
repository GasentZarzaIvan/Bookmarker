package igz.tfg.bookmarker.ui.libro;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.modelos.room.BibliotecaRepository;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.LibroConMarcadores;
import igz.tfg.bookmarker.modelos.room.tablas.Marcador;

public class LibroViewModel extends AndroidViewModel {
    private final MutableLiveData<String> textoBuscar;
    private final BibliotecaRepository repository;
    private LiveData<LibroConMarcadores> libroConMarcadores = null;

    public LibroViewModel(@NonNull Application application) {
        super(application);
        repository = new BibliotecaRepository(application);
        textoBuscar = new MutableLiveData<>();
        textoBuscar.setValue(String.valueOf(R.string.libro_marcadores_vacio));
    }

    public MutableLiveData<String> getTextoBuscar() {
        return textoBuscar;
    }

    public void insert(Libro libro) {
        repository.insert(libro);
    }

    public LiveData<LibroConMarcadores> getLibroConMarcadores() {
        return libroConMarcadores;
    }

    public void setLibroId(long libroId) {
        libroConMarcadores = repository.getLibroConMarcadoresById(libroId);
    }

    public void updateMarcador(Marcador marcador) {
        repository.update(marcador);
    }

    public void addMarcador(Libro libro) {
        repository.insert(new Marcador(libro.getLibroId()));
    }
}