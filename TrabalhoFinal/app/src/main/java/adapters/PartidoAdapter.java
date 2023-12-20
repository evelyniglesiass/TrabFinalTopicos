package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalhofinal.R;

import java.util.List;

import models.Partido;

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.PartidoViewHolder> {

    private List<Partido> listPartido;
    private OnItemClickListener listener;

    public PartidoAdapter(List<Partido> listPartido, OnItemClickListener listener) {
        this.listPartido = listPartido;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PartidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partido, parent, false);
        return new PartidoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PartidoViewHolder holder, int position) {
        Partido partido = listPartido.get(position);

        holder.txtNome.setText(partido.getNome());
        holder.txtSigla.setText(partido.getSigla());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(partido);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPartido.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Partido partido);
    }

    public class PartidoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome;
        TextView txtSigla;

        public PartidoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomePartido);
            txtSigla = itemView.findViewById(R.id.txtPartidoSigla);
        }
    }
}