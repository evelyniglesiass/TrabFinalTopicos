package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalhofinal.R;

import java.util.List;

import models.Deputado;

public class DeputadoAdapter extends RecyclerView.Adapter<DeputadoAdapter.DeputadoViewHolder> {

    private List<Deputado> listDeputados;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Deputado deputado);
    }

    public DeputadoAdapter(List<Deputado> listDeputados, OnItemClickListener listener) {
        this.listDeputados = listDeputados;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeputadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(c);
        View depView = inflater.inflate(com.example.trabalhofinal.R.layout.item_deputado, parent, false);
        return new DeputadoViewHolder(depView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeputadoViewHolder holder, int position) {
        Deputado dep = listDeputados.get(position);
        holder.bind(dep, listener);
    }

    @Override
    public int getItemCount() {
        return listDeputados.size();
    }

    static class DeputadoViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtNome;
        private final TextView txtPartidoSigla;

        DeputadoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeDeputado);
            txtPartidoSigla = itemView.findViewById(R.id.txtDeputadoSigla);
        }

        void bind(final Deputado dep, final OnItemClickListener listener) {
            txtNome.setText(dep.getNome());
            txtPartidoSigla.setText(dep.getSiglaPartido());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(dep);
                }
            });
        }
    }
}
