package igz.tfg.bookmarker.modelos.openlibrary;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.net.URLEncoder;

public class OpenLibraryManager {
    private static final String URL_BASE = "http://openlibrary.org/";
    private static final String SEARCH_API = "search.json?";
    private static final String BUSQUEDA_GENERAL = "q=";
    private static final String EDICIONES = "/editions.json";
    private static final String JSON = ".json";

    private static final AsyncHttpClient cliente = new AsyncHttpClient();

    public void busquedaGeneral(String busqueda, JsonHttpResponseHandler handler) {
        try {
            String direccion = URL_BASE + SEARCH_API + BUSQUEDA_GENERAL + URLEncoder.encode(busqueda, "utf-8");
            cliente.get(direccion, handler);
        } catch (Exception e) {
            Log.e("API", e.getMessage());
        }
    }

    public void busquedaEdiciones(String busqueda, JsonHttpResponseHandler handler) {
        try {
            String direccion = URL_BASE + URLEncoder.encode(busqueda, "utf-8") + EDICIONES;
            cliente.get(direccion, handler);
        } catch (Exception e) {
            Log.e("API", e.getMessage());
        }
    }

    public void busquedaObra(String busqueda, JsonHttpResponseHandler handler) {
        try {
            String direccion = URL_BASE + URLEncoder.encode(busqueda, "utf-8") + JSON;
            cliente.get(direccion, handler);
        } catch (Exception e) {
            Log.e("API", e.getMessage());
        }
    }
}
