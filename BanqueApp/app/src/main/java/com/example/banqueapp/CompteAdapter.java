package com.example.banqueapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {

    private List<Compte> compteList;
    private OnCompteActionListener actionListener;

    public interface OnCompteActionListener {
        void onUpdate(Compte compte);
        void onDelete(Compte compte);
    }

    public CompteAdapter(List<Compte> compteList, OnCompteActionListener actionListener) {
        this.compteList = compteList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.compte_item, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = compteList.get(position);
        holder.textId.setText("ID: " + compte.getIdLong());
        holder.textSolde.setText("Balance: " + compte.getSolde());
        holder.textType.setText("Type: " + compte.getType());
        holder.textDate.setText("Created: " + compte.getDateCreation());

        holder.btnUpdate.setOnClickListener(v -> actionListener.onUpdate(compte));
        holder.btnDelete.setOnClickListener(v -> actionListener.onDelete(compte));
    }

    @Override
    public int getItemCount() {
        return compteList.size();
    }

    static class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView textId, textSolde, textType, textDate;
        ImageButton btnUpdate, btnDelete;

        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.text_id);
            textSolde = itemView.findViewById(R.id.text_solde);
            textType = itemView.findViewById(R.id.text_type);
            textDate = itemView.findViewById(R.id.text_date);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
