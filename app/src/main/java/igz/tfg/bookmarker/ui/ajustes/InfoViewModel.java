package igz.tfg.bookmarker.ui.ajustes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import igz.tfg.bookmarker.modelos.room.BibliotecaRepository;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.Marcador;
import igz.tfg.bookmarker.modelos.room.tablas.Seccion;

public class InfoViewModel extends AndroidViewModel {
    private final BibliotecaRepository repository;

    private final MutableLiveData<String> txtDatosSecciones;
    private final MutableLiveData<String> txtDatosLibros;
    private final MutableLiveData<String> txtDatosMarcadores;
    private final LiveData<List<Seccion>> seccion;
    private final LiveData<List<Libro>> libros;
    private final LiveData<List<Marcador>> marcadores;


    public InfoViewModel(@NonNull Application application) {
        super(application);
        repository = new BibliotecaRepository(application);
        txtDatosSecciones = new MutableLiveData<>();
        txtDatosSecciones.setValue("Número de secciones: 0");
        txtDatosLibros = new MutableLiveData<>();
        txtDatosLibros.setValue("Número de secciones: 0");
        txtDatosMarcadores = new MutableLiveData<>();
        txtDatosMarcadores.setValue("Número de secciones: 0");
        seccion = repository.getAllSecciones();
        libros = repository.getAllLibros();
        marcadores = repository.getAllMarcadores();
    }

    public MutableLiveData<String> getTxtDatosSecciones() {
        return txtDatosSecciones;
    }

    public MutableLiveData<String> getTxtDatosLibros() {
        return txtDatosLibros;
    }

    public MutableLiveData<String> getTxtDatosMarcadores() {
        return txtDatosMarcadores;
    }

    public LiveData<List<Seccion>> getSeccion() {
        return seccion;
    }

    public LiveData<List<Libro>> getLibros() {
        return libros;
    }

    public LiveData<List<Marcador>> getMarcadores() {
        return marcadores;
    }

}