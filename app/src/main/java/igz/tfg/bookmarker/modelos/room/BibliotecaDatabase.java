package igz.tfg.bookmarker.modelos.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.Marcador;
import igz.tfg.bookmarker.modelos.room.tablas.Seccion;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionLibroRelacion;

@Database(entities = {Libro.class, Seccion.class, SeccionLibroRelacion.class, Marcador.class}, version = 1)
public abstract class BibliotecaDatabase extends RoomDatabase {

    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
    private static BibliotecaDatabase instance;

    public static synchronized BibliotecaDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), BibliotecaDatabase.class, "biblioteca_database").addCallback(roomCallback).fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract BibliotecaDao libroDao();

}
