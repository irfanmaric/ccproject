package com.madev.virtualwaitingroom.ui.dashboard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.madev.virtualwaitingroom.Adapter.MyUserAppointAdapter;
import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Common.SpacesItemDecoration;
import com.madev.virtualwaitingroom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    Unbinder unbinder;

    @BindView(R.id.recycler_myAppoint)
    RecyclerView recycler_myAppoint;
    MyUserAppointAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this,root);
        initViews();
        dashboardViewModel.getMessageError().observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getContext(), "" + s, Toast.LENGTH_SHORT).show();
            });
            dashboardViewModel.getMutableLiveDataUserAppointList().observe(getViewLifecycleOwner(), userAppointments -> {

                adapter = new MyUserAppointAdapter(getContext(), userAppointments);
                recycler_myAppoint.setAdapter(adapter);

            });

       return root;
    }

    private void initViews() {
        recycler_myAppoint.setHasFixedSize(true);
        recycler_myAppoint.setLayoutManager(new LinearLayoutManager(getContext()));
}}