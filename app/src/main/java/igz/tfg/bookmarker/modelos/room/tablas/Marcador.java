package igz.tfg.bookmarker.modelos.room.tablas;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "marcador", indices = {@Index(value = {"marcadorId"}, unique = true)}, foreignKeys = {@ForeignKey(entity = Libro.class, parentColumns = "libroId", childColumns = "libroId", onUpdate = 5, onDelete = 5)})
public class Marcador {
    @PrimaryKey(autoGenerate = true)
    private long marcadorId;
    private long libroId;
    private String nombre;
    private int pagina = 0;

    public Marcador() {
    }

    public Marcador(long libroId) {
        this.libroId = libroId;
        this.nombre = "Marcador";
    }

    public Marcador(long libroId, String nombre) {
        this.libroId = libroId;
        this.nombre = nombre;
    }

    public long getMarcadorId() {
        return marcadorId;
    }

    public void setMarcadorId(long marcadorId) {
        this.marcadorId = marcadorId;
    }

    public long getLibroId() {
        return libroId;
    }

    public void setLibroId(long libroId) {
        this.libroId = libroId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }
}
