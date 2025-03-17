package igz.tfg.bookmarker.modelos.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.LibroConMarcadores;
import igz.tfg.bookmarker.modelos.room.tablas.LibroConSecciones;
import igz.tfg.bookmarker.modelos.room.tablas.Marcador;
import igz.tfg.bookmarker.modelos.room.tablas.Seccion;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionConLibros;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionLibroRelacion;

@Dao
public interface BibliotecaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Libro libro);

    @Update
    void update(Libro libro);

    @Delete
    void delete(Libro libro);

    @Query("DELETE FROM Libro")
    void deleteAllLibros();

    @Query("SELECT * FROM Libro ORDER BY titulo DESC")
    LiveData<List<Libro>> getAllLibros();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Seccion seccion);

    @Update
    void update(Seccion seccion);

    @Delete
    void delete(Seccion seccion);

    @Query("DELETE FROM seccion")
    void deleteAllSecciones();

    @Query("SELECT * FROM seccion ORDER BY nombre DESC")
    LiveData<List<Seccion>> getAllSecciones();

    @Query("SELECT * FROM libro WHERE rowid = :rowId")
    Libro getLibroByRowId(Long rowId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(SeccionLibroRelacion Secc);

    @Update
    void update(SeccionLibroRelacion seccion);

    @Delete
    void delete(SeccionLibroRelacion seccion);

    @Query("DELETE FROM seccion_libro_relacion where seccionId = :seccionId and libroId = :libroId")
    void deleteSeccionLibroRelacionById(long seccionId, long libroId);

    @Query("DELETE FROM seccion")
    void deleteAllSeccionLibroRelacion();

    @Query("SELECT * FROM seccion_libro_relacion")
    LiveData<List<SeccionLibroRelacion>> getAllSeccionLibroRelacion();

    @Transaction
    @Query("SELECT * FROM seccion")
    LiveData<List<SeccionConLibros>> getAllSeccionesConLibros();

    @Transaction
    @Query("SELECT * FROM libro")
    LiveData<List<LibroConSecciones>> getAllLibrosConSecciones();

    @Query("SELECT * FROM libro WHERE libroId = :id")
    Libro getLibroById(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Marcador libro);

    @Update
    void update(Marcador libro);

    @Delete
    void delete(Marcador libro);

    @Transaction
    @Query("SELECT * FROM libro where libroId = :libroId")
    LiveData<LibroConMarcadores> getLibroConMarcadoresById(Long libroId);

    @Transaction
    @Query("SELECT * FROM libro where libroId = :libroId")
    LiveData<LibroConSecciones> getLibroConSeccionesById(Long libroId);

    @Query("SELECT * FROM marcador")
    LiveData<List<Marcador>> getAllMarcadores();

    @Query("DELETE FROM marcador")
    void deleteAllMarcadores();

}
