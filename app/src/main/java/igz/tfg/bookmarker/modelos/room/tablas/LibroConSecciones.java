package igz.tfg.bookmarker.modelos.room.tablas;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class LibroConSecciones {
    @Embedded
    public Libro libro;
    @Relation(parentColumn = "libroId", entityColumn = "seccionId", associateBy = @Junction(SeccionLibroRelacion.class))
    public List<Seccion> secciones;
}
