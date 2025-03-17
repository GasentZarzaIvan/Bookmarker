package igz.tfg.bookmarker.ui.biblioteca;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import igz.tfg.bookmarker.modelos.room.BibliotecaRepository;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.LibroConSecciones;
import igz.tfg.bookmarker.modelos.room.tablas.Seccion;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionConLibros;

public class BibliotecaViewModel extends AndroidViewModel {

    private final BibliotecaRepository repository;
    private final LiveData<List<SeccionConLibros>> allSeccionesConLibros;

    public BibliotecaViewModel(@NonNull Application application) {
        super(application);
        repository = new BibliotecaRepository(application);
        allSeccionesConLibros = repository.getAllSeccionesConLibros();
    }

    public void insert(Libro libro) {
        repository.insert(libro);
    }

    public void update(Libro libro) {
        repository.update(libro);
    }

    public void insert(Seccion seccion) {
        repository.insert(seccion);
    }

    public LiveData<List<SeccionConLibros>> getAllSeccionesConLibros() {
        return allSeccionesConLibros;
    }
}