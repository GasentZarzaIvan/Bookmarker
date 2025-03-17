package igz.tfg.bookmarker.modelos.room.tablas;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class SeccionConLibros {
    @Embedded
    public Seccion seccion;
    @Relation(parentColumn = "seccionId", entityColumn = "libroId", associateBy = @Junction(SeccionLibroRelacion.class))
    public List<Libro> libros;

}
