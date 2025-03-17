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

public class ObraOL implements Parcelable {

    public static final Creator<ObraOL> CREATOR = new Creator<ObraOL>() {
        @Override
        public ObraOL createFromParcel(Parcel in) {
            return new ObraOL(in);
        }

        @Override
        public ObraOL[] newArray(int size) {
            return new ObraOL[size];
        }
    };
    private String idOpenLibrary;
    private String idWorks;
    private String titulo;
    private String autor;
    private String descripcion; //datos de descripcion en /work/.json

    public ObraOL() {
    }

    protected ObraOL(Parcel in) {
        idOpenLibrary = in.readString();
        idWorks = in.readString();
        titulo = in.readString();
        autor = in.readString();
        descripcion = in.readString();
    }

    public static ObraOL desdeJson(JSONObject jsonObject) {
        ObraOL obraOL = new ObraOL();
        try {
            obraOL.idWorks = jsonObject.getString("key");
            if (jsonObject.has("cover_edition_key")) {
                obraOL.idOpenLibrary = jsonObject.getString("cover_edition_key");
            } else if (jsonObject.has("edition_key")) {
                JSONArray ids = jsonObject.getJSONArray("edition_key");
                obraOL.idOpenLibrary = ids.getString(0);
            }
            obraOL.titulo = jsonObject.has("title_suggest") ? jsonObject.getString("title_suggest") : "";
            obraOL.autor = obtenerAutor(jsonObject);
            obraOL.descripcion = jsonObject.has("description") ? jsonObject.getString("description") : "";
        } catch (JSONException e) {
            Log.e("ObraDesdeJSON", e.getMessage());
            e.printStackTrace();
            return null;
        }
        return obraOL;
    }

    private static String obtenerAutor(final JSONObject jsonObject) {
        try {
            JSONArray autores = jsonObject.getJSONArray("author_name");
            int numAutores = autores.length();
            String[] autoresCadenas = new String[numAutores];
            for (int i = 0; i < numAutores; i++) {
                autoresCadenas[i] = autores.getString(i);
            }
            return TextUtils.join(", ", autoresCadenas);
        } catch (JSONException e) {
            return "";
        }
    }

    public static ArrayList<ObraOL> desdeJson(JSONArray jsonArray) {
        ArrayList<ObraOL> obraOLS = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject libroJson;
            try {
                libroJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            ObraOL obraOL = ObraOL.desdeJson(libroJson);
            if (obraOL != null) {
                obraOLS.add(obraOL);
            }
        }
        return obraOLS;
    }


    public String getIdOpenLibrary() {
        return idOpenLibrary;
    }

    public String getIdWorks() {
        return idWorks;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getPortadaUrl() {
        return "http://covers.openlibrary.org/b/olid/" + idOpenLibrary + "-L.jpg?default=false";
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(idOpenLibrary);
        dest.writeString(idWorks);
        dest.writeString(titulo);
        dest.writeString(autor);
        dest.writeString(descripcion);
    }
}