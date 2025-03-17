package igz.tfg.bookmarker.ui.crearSecccion;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import igz.tfg.bookmarker.modelos.room.BibliotecaRepository;
import igz.tfg.bookmarker.modelos.room.tablas.Seccion;

public class CrearSeccionViewModel extends AndroidViewModel {

    private final BibliotecaRepository repository;
    private final LiveData<List<Seccion>> allSecciones;


    public CrearSeccionViewModel(@NonNull Application application) {
        super(application);
        repository = new BibliotecaRepository(application);
        allSecciones = repository.getAllSecciones();
    }

    public void insert(Seccion seccion) {
        repository.insert(seccion);
    }

    public LiveData<List<Seccion>> getAllSecciones() {
        return allSecciones;
    }

}