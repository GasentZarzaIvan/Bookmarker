package igz.tfg.bookmarker.ui.editarLibro;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import igz.tfg.bookmarker.modelos.openlibrary.EdicionOL;
import igz.tfg.bookmarker.modelos.room.BibliotecaRepository;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.LibroConMarcadores;
import igz.tfg.bookmarker.modelos.room.tablas.LibroConSecciones;
import igz.tfg.bookmarker.modelos.room.tablas.Seccion;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionConLibros;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionLibroRelacion;

public class EditarViewModel extends AndroidViewModel {

    private final BibliotecaRepository repository;
    private LiveData<LibroConSecciones> libroConSecciones = null;
    private final LiveData<List<Seccion>> allSecciones;
    private EdicionOL edicion;

    private String uriImagen;
    private String titulo;

    public EditarViewModel(@NonNull Application application) {
        super(application);
        repository = new BibliotecaRepository(application);
        allSecciones = repository.getAllSecciones();
        uriImagen = "";
        titulo = "";
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public long insert(Libro libro) {
        return repository.insert(libro);
    }

    public void update(Libro libro) {
        repository.update(libro);
    }

    public void insert(Seccion seccion) {
        repository.insert(seccion);
    }

    public void insert(SeccionLibroRelacion relacion) {
        repository.insert(relacion);
    }

    public void delete(SeccionLibroRelacion relacion) {
        repository.delete(relacion);
    }

    public void deleteSeccionLibroRelacionById(long seccionId, long libroId) {
        repository.deleteSeccionLibroRelacionById(seccionId, libroId);
    }

    public LiveData<LibroConSecciones> getLibroConSecciones() {
        return libroConSecciones;
    }

    public Libro getLibroByRowId(Long rowId) {
        return repository.getLibroByRowId(rowId);
    }

    public LiveData<List<Seccion>> getAllSecciones() {
        return allSecciones;
    }

    public EdicionOL getEdicion() {
        return edicion;
    }

    public void setEdicion(EdicionOL edicion) {
        this.edicion = edicion;
    }

    public String getUriImagen() {
        return uriImagen;
    }

    public void setUriImagen(String uriImagen) {
        this.uriImagen = uriImagen;
    }

    public void setLibroId(long libroId) {
        libroConSecciones = repository.getLibroConSeccionesById(libroId);
    }
}