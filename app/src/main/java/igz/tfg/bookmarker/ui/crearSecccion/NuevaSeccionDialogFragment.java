package igz.tfg.bookmarker.ui.crearSecccion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import igz.tfg.bookmarker.R;
import igz.tfg.bookmarker.modelos.room.tablas.Seccion;


public class NuevaSeccionDialogFragment extends DialogFragment {
    private CrearSeccionViewModel crearSeccionViewModel;
    private EditText inpNombreSeccion;
    private Button btnCancelar;
    private Button btnAceptar;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        crearSeccionViewModel = new ViewModelProvider(this).get(CrearSeccionViewModel.class);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_nueva_seccion_dialog, null);
        builder.setView(view);

        inpNombreSeccion = view.findViewById(R.id.inpNombreSeccion);
        btnCancelar = view.findViewById(R.id.btnCancelar);
        btnAceptar = view.findViewById(R.id.btnAceptar);
        btnAceptar.setEnabled(false);

        inpNombreSeccion.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnAceptar.setEnabled(!s.toString().trim().isEmpty());
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getParentFragment().getView()).popBackStack();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreSeccion = inpNombreSeccion.getText().toString().trim();
                crearSeccionViewModel.insert(new Seccion(nombreSeccion));
                Navigation.findNavController(getParentFragment().getView()).popBackStack();
            }
        });
        return builder.create();
    }
}