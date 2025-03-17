package igz.tfg.bookmarker.modelos.adaptadores.biblioteca;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import igz.tfg.bookmarker.modelos.room.tablas.SeccionConLibros;
import igz.tfg.bookmarker.ui.biblioteca.SeccionFragment;

public class SeccionesAdapter extends FragmentStateAdapter {
    private List<SeccionConLibros> seccionConLibros = new ArrayList<>();

    public SeccionesAdapter(Fragment fragment) {
        super(fragment);
    }

    public void setSeccionConLibros(List<SeccionConLibros> seccionConLibros) {
        this.seccionConLibros = seccionConLibros;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return SeccionFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return seccionConLibros.size();
    }
}
