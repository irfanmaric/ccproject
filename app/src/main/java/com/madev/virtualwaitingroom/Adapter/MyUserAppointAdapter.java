package com.madev.virtualwaitingroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.madev.virtualwaitingroom.Callback.IRecyclerClickListener;
import com.madev.virtualwaitingroom.Model.CategoryModel;
import com.madev.virtualwaitingroom.Model.UserAppointment;
import com.madev.virtualwaitingroom.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyUserAppointAdapter  extends RecyclerView.Adapter<MyUserAppointAdapter.MyViewHolder> {

    Context context;
    List<UserAppointment> userAppointments;

    public MyUserAppointAdapter(Context context, List<UserAppointment> userAppointments) {
        this.context = context;
        this.userAppointments = userAppointments;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Unbinder unbinder;
        @BindView(R.id.appoint_date)
        TextView appoint_date;
        @BindView(R.id.appoint_time)
        TextView appoint_time;
        @BindView(R.id.appoint_branch)
        TextView appoint_branch;
        @BindView(R.id.appoint_status)
        TextView appoint_status;

        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyUserAppointAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_myappoint_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyUserAppointAdapter.MyViewHolder holder, int position) {
        holder.appoint_date.setText(new StringBuilder(userAppointments.get(position).getDate()));
        holder.appoint_time.setText(new StringBuilder(userAppointments.get(position).getTime()));
        holder.appoint_branch.setText(new StringBuilder(userAppointments.get(position).getBranchName()));
        if(userAppointments.get(position).getStatus().equals(""))
            holder.appoint_status.setVisibility(View.GONE);
        else{
            holder.appoint_status.setText(new StringBuilder(userAppointments.get(position).getStatus()));
        }


    }

    @Override
    public int getItemCount() {
        return userAppointments.size();
    }
}
