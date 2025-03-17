package igz.tfg.bookmarker.modelos.room.tablas;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "seccion", indices = {@Index(value = {"seccionId"}, unique = true)})
public class Seccion {
    @PrimaryKey(autoGenerate = true)
    private long seccionId;

    private String nombre;

    public Seccion(String nombre) {
        this.nombre = nombre;
    }

    public long getSeccionId() {
        return seccionId;
    }

    public void setSeccionId(long seccionId) {
        this.seccionId = seccionId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
