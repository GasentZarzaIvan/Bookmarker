package igz.tfg.bookmarker.modelos.room.tablas;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class LibroConMarcadores {
    @Embedded
    public Libro libro;
    @Relation(parentColumn = "libroId", entityColumn = "libroId")
    public List<Marcador> marcadores;
}
