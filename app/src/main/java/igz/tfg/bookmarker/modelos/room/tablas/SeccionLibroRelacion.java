package igz.tfg.bookmarker.modelos.room.tablas;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "seccion_libro_relacion", primaryKeys = {"seccionId", "libroId"}, foreignKeys = {@ForeignKey(entity = Seccion.class, parentColumns = "seccionId", childColumns = "seccionId", onUpdate = 5, onDelete = 5), @ForeignKey(entity = Libro.class, parentColumns = "libroId", childColumns = "libroId", onUpdate = 5, onDelete = 5)}, indices = {@Index(value = {"libroId", "seccionId"})})
public class SeccionLibroRelacion {
    private long seccionId;
    private long libroId;

    public SeccionLibroRelacion(long seccionId, long libroId) {
        this.seccionId = seccionId;
        this.libroId = libroId;
    }

    public long getSeccionId() {
        return seccionId;
    }

    public void setSeccionId(long seccionId) {
        this.seccionId = seccionId;
    }

    public long getLibroId() {
        return libroId;
    }

    public void setLibroId(long libroId) {
        this.libroId = libroId;
    }
}
