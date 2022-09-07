package com.madev.virtualwaitingroom.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.madev.virtualwaitingroom.Interface.ItemClickListener;
import com.madev.virtualwaitingroom.R;

public class AppointViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    public CardView item_appoint;
    public TextView tv_appoint_time,tv_appoint_avaliability;
    private ItemClickListener itemClickListener;


    public AppointViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_appoint_avaliability = (TextView)itemView.findViewById(R.id.tv_appoint_avaliability);
        tv_appoint_time = (TextView)itemView.findViewById(R.id.tv_appoint_time);
        item_appoint = (CardView) itemView.findViewById(R.id.item_appoint);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

        }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Izaberite opciju");
        contextMenu.add(0,0,getAdapterPosition(), "Rezerviši termin");
    }
}
