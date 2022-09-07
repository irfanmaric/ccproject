package com.madev.virtualwaitingroom.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.madev.virtualwaitingroom.Adapter.MyBranchAdapter;
import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Model.BranchesModel;
import com.madev.virtualwaitingroom.R;
import com.madev.virtualwaitingroom.ui.home.HomeViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Branches extends Fragment {

    private BranchesViewModel branchesViewModel;

    Unbinder unbinder;
    @BindView(R.id.recycler_branches)
    RecyclerView recycler_branches;
    LayoutAnimationController layoutAnimationController;
    MyBranchAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        branchesViewModel =
                new ViewModelProvider(this).get(BranchesViewModel.class);
        View root = inflater.inflate(R.layout.branches_fragment, container, false);
        unbinder = ButterKnife.bind(this,root);
        initViews();
        branchesViewModel.getMutableLiveDataBranchList().observe(getViewLifecycleOwner(), new Observer<List<BranchesModel>>() {
            @Override
            public void onChanged(List<BranchesModel> branchesModels) {
                adapter = new MyBranchAdapter(getContext(),branchesModels);
                recycler_branches.setAdapter(adapter);
                recycler_branches.setLayoutAnimation(layoutAnimationController);

            }
        });
        return root;
    }


    private void initViews(){
       // ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Common.categorySelected.getName());

        recycler_branches.setHasFixedSize(true);
        recycler_branches.setLayoutManager(new LinearLayoutManager(getContext()));
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
    }
}