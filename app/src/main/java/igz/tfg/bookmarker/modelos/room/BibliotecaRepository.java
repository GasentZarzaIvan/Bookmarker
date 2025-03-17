package igz.tfg.bookmarker.modelos.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import igz.tfg.bookmarker.modelos.openlibrary.OpenLibraryManager;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.LibroConMarcadores;
import igz.tfg.bookmarker.modelos.room.tablas.LibroConSecciones;
import igz.tfg.bookmarker.modelos.room.tablas.Marcador;
import igz.tfg.bookmarker.modelos.room.tablas.Seccion;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionConLibros;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionLibroRelacion;

public class BibliotecaRepository {
    private final BibliotecaDao bibliotecaDao;
    private final OpenLibraryManager openLibraryManager;
    private final LiveData<List<Libro>> allLibros;
    private final LiveData<List<Seccion>> allSecciones;
    private final LiveData<List<SeccionConLibros>> allSeccionesConLibros;
    private final LiveData<List<LibroConSecciones>> allLibrosConSecciones;
    private final LiveData<List<Marcador>> allMarcadores;

    private final Executor executor = Executors.newSingleThreadExecutor();

    public BibliotecaRepository(Application application) {
        BibliotecaDatabase bibliotecaDatabase = BibliotecaDatabase.getInstance(application);
        openLibraryManager = new OpenLibraryManager();
        bibliotecaDao = bibliotecaDatabase.libroDao();
        allLibros = bibliotecaDao.getAllLibros();
        allSecciones = bibliotecaDao.getAllSecciones();
        allSeccionesConLibros = bibliotecaDao.getAllSeccionesConLibros();
        allLibrosConSecciones = bibliotecaDao.getAllLibrosConSecciones();
        allMarcadores = bibliotecaDao.getAllMarcadores();
    }

    public void busquedaGeneral(String busqueda, JsonHttpResponseHandler handler) {
        openLibraryManager.busquedaGeneral(busqueda, handler);
    }

    public void busquedaEdiciones(String busqueda, JsonHttpResponseHandler handler) {
        openLibraryManager.busquedaEdiciones(busqueda, handler);
    }

    public Libro getLibroByRowId(Long rowId) {
        Libro libro = null;
        try {
            Callable<Libro> callable = new Callable<Libro>() {
                @Override
                public Libro call() throws Exception {
                    return bibliotecaDao.getLibroByRowId(rowId);
                }
            };
            Future<Libro> future = Executors.newSingleThreadExecutor().submit(callable);
            libro = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return libro;
    }

    public long insert(Libro libro) {
        Long rowId = (long) -1;
        try {
            Callable<Long> callable = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return bibliotecaDao.insert(libro);
                }
            };
            Future<Long> future = Executors.newSingleThreadExecutor().submit(callable);
            rowId = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowId;
    }

    public void update(Libro libro) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.update(libro);
            }
        });
    }

    public void delete(Libro libro) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.delete(libro);
            }
        });
    }

    public void deleteAllLibros() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.deleteAllLibros();
            }
        });
    }

    public void insert(Seccion seccion) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.insert(seccion);
            }
        });
    }

    public void update(Seccion seccion) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.update(seccion);
            }
        });
    }

    public void delete(Seccion seccion) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.delete(seccion);
            }
        });
    }

    public void deleteAllSecciones() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.deleteAllSecciones();
            }
        });
    }

    public void insert(SeccionLibroRelacion relacion) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.insert(relacion);
            }
        });
    }

    public void update(SeccionLibroRelacion relacion) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.update(relacion);
            }
        });
    }

    public void delete(SeccionLibroRelacion relacion) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.delete(relacion);
            }
        });
    }

    public void deleteSeccionLibroRelacionById(long seccionId, long libroId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.deleteSeccionLibroRelacionById(seccionId, libroId);
            }
        });
    }

    public void deleteAllSeccionLibroRelacion() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.deleteAllSeccionLibroRelacion();
            }
        });
    }

    public LiveData<List<Libro>> getAllLibros() {
        return allLibros;
    }

    public LiveData<List<SeccionConLibros>> getAllSeccionesConLibros() {
        return allSeccionesConLibros;
    }

    public LiveData<List<LibroConSecciones>> getAllLibrosConSecciones() {
        return allLibrosConSecciones;
    }

    public LiveData<List<Seccion>> getAllSecciones() {
        return allSecciones;
    }

    public Libro getLibroById(long id) {
        Libro libro = null;
        try {
            Callable<Libro> callable = new Callable<Libro>() {
                @Override
                public Libro call() throws Exception {
                    return bibliotecaDao.getLibroById(id);
                }
            };
            Future<Libro> future = Executors.newSingleThreadExecutor().submit(callable);
            libro = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return libro;
    }

    public LiveData<List<Marcador>> getAllMarcadores() {
        return allMarcadores;
    }

    public LiveData<LibroConMarcadores> getLibroConMarcadoresById(long id) {
        LiveData<LibroConMarcadores> libroConMarcadores = null;
        try {
            Callable<LiveData<LibroConMarcadores>> callable = new Callable<LiveData<LibroConMarcadores>>() {
                @Override
                public LiveData<LibroConMarcadores> call() throws Exception {
                    return bibliotecaDao.getLibroConMarcadoresById(id);
                }
            };
            Future<LiveData<LibroConMarcadores>> future = Executors.newSingleThreadExecutor().submit(callable);
            libroConMarcadores = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return libroConMarcadores;
    }

    public void insert(Marcador marcador) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.insert(marcador);
            }
        });
    }

    public void update(Marcador marcador) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                bibliotecaDao.update(marcador);
            }
        });
    }

    public LiveData<LibroConSecciones> getLibroConSeccionesById(long id) {
        LiveData<LibroConSecciones> libroConSecciones = null;
        try {
            Callable<LiveData<LibroConSecciones>> callable = new Callable<LiveData<LibroConSecciones>>() {
                @Override
                public LiveData<LibroConSecciones> call() throws Exception {
                    return bibliotecaDao.getLibroConSeccionesById(id);
                }
            };
            Future<LiveData<LibroConSecciones>> future = Executors.newSingleThreadExecutor().submit(callable);
            libroConSecciones = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return libroConSecciones;
    }
}
