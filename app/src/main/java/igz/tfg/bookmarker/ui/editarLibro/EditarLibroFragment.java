package igz.tfg.bookmarker.ui.editarLibro;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.databinding.FragmentEditarLibroBinding;
import igz.tfg.bookmarker.modelos.openlibrary.EdicionOL;
import igz.tfg.bookmarker.modelos.room.tablas.Libro;
import igz.tfg.bookmarker.modelos.room.tablas.LibroConSecciones;
import igz.tfg.bookmarker.modelos.room.tablas.Seccion;
import igz.tfg.bookmarker.modelos.room.tablas.SeccionLibroRelacion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarLibroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarLibroFragment extends Fragment {
    private long libroId = -1;

    private FragmentEditarLibroBinding binding;
    private Libro libro;
    private NavController navController;
    private EditText inpTitulo;
    private EditText inpAutor;
    private EditText inpNumPaginas;
    private EditText inpIsbn10;
    private EditText inpIsbn13;
    private EditText inpFechaPublicacion;
    private EditText inpEditorial;
    private EditText inpDescripcion;
    private TextView txtSeccionesVacias;
    private LinearLayout lnlSecciones;
    private ImageView imvPortada;
    private EditarViewModel editarViewModel;
    private ActivityResultLauncher<Intent> launcherElegirImagen;

    public EditarLibroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditarLibroFragment.
     */
    public static EditarLibroFragment newInstance() {
        EditarLibroFragment fragment = new EditarLibroFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editarViewModel = new ViewModelProvider(this).get(EditarViewModel.class);
        if (getArguments() != null) {
            EditarLibroFragmentArgs args = EditarLibroFragmentArgs.fromBundle(getArguments());
            if (editarViewModel.getTitulo().isEmpty()) {
                editarViewModel.setTitulo(args.getTitulo());
            }
            if (args.getIdLibro() >= 0) {
                libroId = args.getIdLibro();
                editarViewModel.setLibroId(libroId);
            }
            if (args.getEdicion() != null && libroId < 0) {
                    editarViewModel.setEdicion(args.getEdicion());
            }
        }
        launcherElegirImagen = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    editarViewModel.setUriImagen(String.valueOf(selectedImageUri));
                    Picasso.get().load(selectedImageUri).placeholder(R.drawable.ic_book_24).into(imvPortada);
                } else {
                    Toast.makeText(getContext(), "No se ha seleccionado ninguna portada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditarLibroBinding.inflate(inflater, container, false);
        inflater.inflate(R.layout.fragment_editar_libro, container, false);
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle(editarViewModel.getTitulo());
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);
        establecerOpcionesToolbar();
        View root = binding.getRoot();
        inpTitulo = binding.inpTitulo;
        inpAutor = binding.inpAutor;
        inpNumPaginas = binding.inpNumPaginas;
        inpIsbn10 = binding.inpIsbn10;
        inpIsbn13 = binding.inpIsbn13;
        inpFechaPublicacion = binding.inpFechaPublicacion;
        inpEditorial = binding.inpEditorial;
        inpDescripcion = binding.inpDescripcion;
        imvPortada = binding.imvPortada;
        txtSeccionesVacias = binding.txtSeccionesVacias;
        lnlSecciones = binding.lnlSecciones;
        dibujarSecciones();
        if (editarViewModel.getEdicion() != null) {
            establecerDatosEdicion();
        }
        if (editarViewModel.getLibroConSecciones() != null) {
            establecerDatosLibro();
        }

        imvPortada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elegirImagen();
            }
        });
        return root;
    }



    private void dibujarSecciones() {
        editarViewModel.getAllSecciones().observe(getViewLifecycleOwner(), new Observer<List<Seccion>>() {
            @Override
            public void onChanged(List<Seccion> secciones) {
                if (secciones.isEmpty()) {
                    txtSeccionesVacias.setVisibility(View.VISIBLE);
                    borrarCheckBoxes();
                } else {
                    txtSeccionesVacias.setVisibility(View.GONE);
                    List<String> cbxNombres = new ArrayList<>();
                    for (int i = 0; i < lnlSecciones.getChildCount(); i++) {
                        if (lnlSecciones.getChildAt(i) instanceof CheckBox) {
                            CheckBox cbxI = ((CheckBox) lnlSecciones.getChildAt(i));
                            if (cbxI.isChecked()) {
                                cbxNombres.add((String) cbxI.getText());
                            }
                        }
                    }
                    borrarCheckBoxes();
                    for (Seccion seccion : secciones) {
                        CheckBox cbx = new CheckBox(getContext());
                        cbx.setText(seccion.getNombre());
                        cbx.setChecked(cbxNombres.contains(seccion.getNombre()));
                        lnlSecciones.addView(cbx);
                    }
                }
            }
        });
    }

    private void borrarCheckBoxes() {
        List<View> botones = new ArrayList<>();
        for (int i = 0; i < lnlSecciones.getChildCount(); i++) {
            View view = lnlSecciones.getChildAt(i);
            if (view instanceof CheckBox) {
                botones.add(view);
            }
        }
        for (View boton : botones) {
            lnlSecciones.removeView(boton);
        }
    }

    private void establecerDatosEdicion() {
        EdicionOL edicion = editarViewModel.getEdicion();
        inpTitulo.setText(edicion.getTitulo().trim());
        inpAutor.setText(edicion.getAutor().trim());
        String numPaginas = edicion.getNumPaginas() > 0 ? String.valueOf(edicion.getNumPaginas()) : "";
        inpNumPaginas.setText(numPaginas.trim());
        inpIsbn10.setText(edicion.getIsbn10().trim());
        inpIsbn13.setText(edicion.getIsbn13().trim());
        inpFechaPublicacion.setText(edicion.getFechaPublicacion().trim());
        inpEditorial.setText(edicion.getEditorial().trim());
        inpDescripcion.setText(edicion.getDescripcion().trim());
        editarViewModel.setUriImagen(edicion.getPortadaUrl());
    }
    private void establecerDatosLibro() {
        editarViewModel.getLibroConSecciones().observe(getViewLifecycleOwner(), new Observer<LibroConSecciones>() {
            @Override
            public void onChanged(LibroConSecciones libroConSecciones) {
                if (libroConSecciones != null) {
                    libro = libroConSecciones.libro;
                    List<Seccion> secciones = libroConSecciones.secciones;
                    if (libro != null) {
                        inpTitulo.setText(libro.getTitulo().trim());
                        inpAutor.setText(libro.getAutor().trim());
                        String numPaginas = libro.getNumPaginas() > 0 ? String.valueOf(libro.getNumPaginas()) : "";
                        inpNumPaginas.setText(numPaginas.trim());
                        inpIsbn10.setText(libro.getIsbn10().trim());
                        inpIsbn13.setText(libro.getIsbn13().trim());
                        inpFechaPublicacion.setText(libro.getFechaPublicacion().trim());
                        inpEditorial.setText(libro.getEditorial().trim());
                        inpDescripcion.setText(libro.getDescripcion().trim());
                        editarViewModel.setUriImagen(libro.getRutaPortada());
                        if (!editarViewModel.getUriImagen().isEmpty()) {
                            Picasso.get().load(editarViewModel.getUriImagen()).placeholder(R.drawable.ic_book_24).into(imvPortada);
                        }
                        if (!secciones.isEmpty()) {
                            seleccionarSecciones(secciones);
                        }
                    }
                }
            }
        });

    }

    private void seleccionarSecciones(List<Seccion> secciones) {
        List<String> seccionesNombres = secciones.stream().map(Seccion::getNombre).collect(Collectors.toList());
        for (int i = 0; i < lnlSecciones.getChildCount(); i++) {
            if (lnlSecciones.getChildAt(i) instanceof CheckBox) {
                CheckBox cbxI = ((CheckBox) lnlSecciones.getChildAt(i));
                cbxI.setChecked(seccionesNombres.contains(cbxI.getText().toString()));
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        if (!editarViewModel.getUriImagen().isEmpty()) {
            Picasso.get().load(editarViewModel.getUriImagen()).placeholder(R.drawable.ic_book_24).into(imvPortada);
        }
    }

    private void establecerOpcionesToolbar() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.editar_libro_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int idSeleccionado = menuItem.getItemId();
                if (idSeleccionado == R.id.opc_guardar_libro) {
                    guardarLibro();
                } else if (idSeleccionado == R.id.opc_nueva_seccion) {
                    crearNuevaSeccion();
                }
                return false;
            }
        }, getViewLifecycleOwner());
    }

    //TODO guardar portada en memoria local https://developer.android.com/training/data-storage
    private void guardarLibro() {
        if (inpTitulo.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "No se ha introducido un titulo", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (libro == null) {
                    libro = new Libro();
                }
                libro.setTitulo(inpTitulo.getText().toString().trim());
                libro.setAutor(inpAutor.getText().toString().trim());
                String numPagCadena = inpNumPaginas.getText().toString().trim();
                int numPaginas = numPagCadena.isEmpty() ? 0 : Integer.parseInt(numPagCadena);
                libro.setNumPaginas(numPaginas);
                libro.setIsbn10(inpIsbn10.getText().toString().trim());
                libro.setIsbn13(inpIsbn13.getText().toString().trim());
                libro.setFechaPublicacion(inpFechaPublicacion.getText().toString().trim());
                libro.setEditorial(inpEditorial.getText().toString().trim());
                libro.setDescripcion(inpDescripcion.getText().toString().trim());
                libro.setRutaPortada(editarViewModel.getUriImagen());
                if (libroId <= 0) {
                    long rowId = editarViewModel.insert(libro);
                    libroId = obtenerLibroId(rowId);
                    libro .setLibroId(libroId);
                    editarViewModel.setTitulo("Editar libro");
                    ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                    toolbar.setTitle(editarViewModel.getTitulo());
                    editarViewModel.setEdicion(null);
                } else {
                    editarViewModel.update(libro);
                    libroId = libro.getLibroId();
                }
                crearYBorrarRelaciones(libroId);
                Toast.makeText(getContext(), "Datos registrados", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void crearYBorrarRelaciones(long libroId) {
        List<Seccion> seccionesARelacionar = obtenerSeccionesARelacionar();
        for (Seccion seccion : seccionesARelacionar) {
            editarViewModel.insert(new SeccionLibroRelacion(seccion.getSeccionId(), libroId));
        }
        List<Seccion> seccionesNoRelacionadas = editarViewModel.getAllSecciones().getValue().stream().filter((seccion -> !seccionesARelacionar.contains(seccion))).collect(Collectors.toList());
        for (Seccion seccion : seccionesNoRelacionadas) {
            editarViewModel.deleteSeccionLibroRelacionById(seccion.getSeccionId(), libroId);
        }
    }

    private List<Seccion> obtenerSeccionesARelacionar() {
        List<String> botonesNombres = new ArrayList<>();
        for (int i = 0; i < lnlSecciones.getChildCount(); i++) {
            View view = lnlSecciones.getChildAt(i);
            if (view instanceof CheckBox && ((CheckBox) view).isChecked()) {
                botonesNombres.add((String) ((CheckBox) view).getText());
            }
        }
        List<Seccion> seccionesIds = editarViewModel.getAllSecciones().getValue().stream().filter((seccion -> botonesNombres.contains(seccion.getNombre()))).collect(Collectors.toList());
        return seccionesIds;
    }

    private long obtenerLibroId(long rowId) {
        Libro libro = editarViewModel.getLibroByRowId(rowId);
        return libro.getLibroId();
    }

    private void crearNuevaSeccion() {
        NavDirections action = EditarLibroFragmentDirections.actionNavigationEditarLibroFragmentToNuevaSeccionDialogFragment();
        navController.navigate(action);
    }

    private void elegirImagen() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launcherElegirImagen.launch(i);
    }
}