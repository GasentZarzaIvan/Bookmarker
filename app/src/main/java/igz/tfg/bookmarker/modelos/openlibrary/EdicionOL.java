package igz.tfg.bookmarker.modelos.openlibrary;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EdicionOL implements Parcelable {
    public static final Creator<EdicionOL> CREATOR = new Creator<EdicionOL>() {
        @Override
        public EdicionOL createFromParcel(Parcel in) {
            return new EdicionOL(in);
        }

        @Override
        public EdicionOL[] newArray(int size) {
            return new EdicionOL[size];
        }
    };
    private String titulo;
    private String autor;
    private int numPaginas;
    private String isbn10;
    private String isbn13;
    private String fechaPublicacion;
    private String editorial;
    private String descripcion;
    private String portadaId;

    public EdicionOL() {
    }

    protected EdicionOL(Parcel in) {
        titulo = in.readString();
        autor = in.readString();
        numPaginas = in.readInt();
        isbn10 = in.readString();
        isbn13 = in.readString();
        fechaPublicacion = in.readString();
        editorial = in.readString();
        descripcion = in.readString();
        portadaId = in.readString();
    }

    public static EdicionOL desdeJson(JSONObject jsonObject, ObraOL obra) {
        EdicionOL edicionOL = desdeJson(jsonObject);
        if (edicionOL != null) {
            edicionOL.autor = obra.getAutor();
            if (edicionOL.titulo.isEmpty()) {
                edicionOL.titulo = obra.getTitulo();
            }
            if (edicionOL.descripcion.isEmpty()) {
                edicionOL.descripcion = obra.getDescripcion();
            }
        }
        return edicionOL;
    }

    public static EdicionOL desdeJson(JSONObject jsonObject) {
        EdicionOL edicionOL = new EdicionOL();
        try {
            edicionOL.titulo = obtenerTitulo(jsonObject);
            edicionOL.autor = "";
            edicionOL.numPaginas = jsonObject.has("number_of_pages") ? jsonObject.getInt("number_of_pages") : 0;
            edicionOL.isbn10 = jsonObject.has("isbn_10") ? jsonObject.getJSONArray("isbn_10").getString(0) : "";
            edicionOL.isbn13 = jsonObject.has("isbn_13") ? jsonObject.getJSONArray("isbn_13").getString(0) : "";
            edicionOL.fechaPublicacion = jsonObject.has("publish_date") ? jsonObject.getString("publish_date") : "";
            edicionOL.editorial = obtenerEditorial(jsonObject);
            edicionOL.descripcion = "";
            edicionOL.portadaId = jsonObject.has("covers") ? jsonObject.getJSONArray("covers").getString(0) : "";
        } catch (JSONException e) {
            Log.e("desdeJSON", e.getMessage());
            e.printStackTrace();
            return null;
        }
        return edicionOL;
    }

    private static String obtenerTitulo(final JSONObject jsonObject) {
        try {
            String titulo = "";
            if (jsonObject.has("full_title")) {
                titulo = jsonObject.getString("full_title");
            } else if (jsonObject.has("title")) {
                titulo = jsonObject.getString("title");
            }
            return titulo;
        } catch (JSONException e) {
            return "";
        }
    }

    private static String obtenerEditorial(final JSONObject jsonObject) {
        try {
            JSONArray editoriales = jsonObject.getJSONArray("publishers");
            int numEditoriales = editoriales.length();
            String[] editorialesCadenas = new String[numEditoriales];
            for (int i = 0; i < numEditoriales; i++) {
                editorialesCadenas[i] = editoriales.getString(i);
            }
            return TextUtils.join(", ", editorialesCadenas);
        } catch (JSONException e) {
            return "";
        }
    }

    public static List<EdicionOL> desdeJson(JSONArray jsonArray, ObraOL obra) {
        List<EdicionOL> edicionOLS = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject edicionJson;
            try {
                edicionJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            EdicionOL edicionOL = EdicionOL.desdeJson(edicionJson, obra);
            if (edicionOL != null) {
                edicionOLS.add(edicionOL);
            }
        }
        return edicionOLS;
    }

    public static ArrayList<EdicionOL> desdeJson(JSONArray jsonArray) {
        ArrayList<EdicionOL> edicionOLS = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject edicionJson;
            try {
                edicionJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            EdicionOL edicionOL = EdicionOL.desdeJson(edicionJson);
            if (edicionOL != null) {
                edicionOLS.add(edicionOL);
            }
        }
        return edicionOLS;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPortadaId() {
        return portadaId;
    }

    public void setPortadaId(String portadaId) {
        this.portadaId = portadaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getPortadaUrl() {
        return "http://covers.openlibrary.org/b/id/" + portadaId + "-L.jpg?default=false";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(autor);
        dest.writeInt(numPaginas);
        dest.writeString(isbn10);
        dest.writeString(isbn13);
        dest.writeString(fechaPublicacion);
        dest.writeString(editorial);
        dest.writeString(descripcion);
        dest.writeString(portadaId);
    }
}
