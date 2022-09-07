package com.madev.virtualwaitingroom.Adapter;

import android.content.Context;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.madev.virtualwaitingroom.Callback.IRecyclerClickListener;
import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.EventBus.BranchItemClick;
import com.madev.virtualwaitingroom.Model.BranchesModel;
import com.madev.virtualwaitingroom.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyBranchAdapter extends RecyclerView.Adapter<MyBranchAdapter.MyViewHolder> {

    private Context context;
    private List<BranchesModel> branchesModelList;

    public MyBranchAdapter(Context context, List<BranchesModel> branchesModelList) {
        this.context = context;
        this.branchesModelList = branchesModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_branch_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(branchesModelList.get(position).getImage()).into(holder.img_branch_image);
        holder.txt_branch_time.setText(new StringBuilder("Radno vrijeme ").append(branchesModelList.get(position).getWorkTime()));
        holder.txt_branch_name.setText(new StringBuilder("").append(branchesModelList.get(position).getName()));
        holder.ratingStar.setText(String.valueOf(branchesModelList.get(position).getRatingValue() / branchesModelList.get(position).getRatingCount()));

        holder.setListener((view, pos) -> {
            Common.selectedFood = branchesModelList.get(pos);
            Common.selectedFood.setKey(String.valueOf(pos));
            EventBus.getDefault().postSticky(new BranchItemClick(true,branchesModelList.get(pos))) ;
        });
    }

    @Override
    public int getItemCount() {
        return branchesModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;
        @BindView(R.id.txt_branch_name)
        TextView txt_branch_name;
        @BindView(R.id.txt_branch_time)
        TextView txt_branch_time;
        @BindView(R.id.img_branch_image)
        ImageView img_branch_image;
        @BindView(R.id.img_fav)
        ImageView img_fav;
        @BindView(R.id.ratingStar)
        TextView ratingStar;
        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener){
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
}
