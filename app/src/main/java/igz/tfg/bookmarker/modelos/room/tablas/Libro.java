package igz.tfg.bookmarker.modelos.room.tablas;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import igz.tfg.bookmarker.modelos.openlibrary.EdicionOL;

@Entity(tableName = "libro", indices = {@Index(value = {"libroId"}, unique = true)})
public class Libro {
    @PrimaryKey(autoGenerate = true)
    private long libroId;
    private String titulo = "";
    private String autor = "";
    private int numPaginas;
    private String isbn10 = "";
    private String isbn13 = "";
    private String fechaPublicacion = "";
    private String editorial = "";
    private String descripcion = "";

    private String rutaPortada = "";

    public Libro() {
    }

    public Libro(EdicionOL edicion) {
        this.titulo = edicion.getTitulo();
        this.autor = edicion.getAutor();
        this.numPaginas = edicion.getNumPaginas();
        this.isbn10 = edicion.getIsbn10();
        this.isbn13 = edicion.getIsbn13();
        this.fechaPublicacion = edicion.getFechaPublicacion();
        this.editorial = edicion.getEditorial();
        this.descripcion = edicion.getDescripcion();
        this.rutaPortada = edicion.getPortadaUrl();
    }

    public long getLibroId() {
        return libroId;
    }

    public void setLibroId(long libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaPortada() {
        return rutaPortada;
    }

    public void setRutaPortada(String rutaPortada) {
        this.rutaPortada = rutaPortada;
    }
}
