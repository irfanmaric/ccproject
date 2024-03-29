package com.madev.virtualwaitingroom.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.madev.virtualwaitingroom.Adapter.MyCategoriesAdapter;
import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Common.SpacesItemDecoration;
import com.madev.virtualwaitingroom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Unbinder unbinder;
    @BindView(R.id.recycler_home)
    RecyclerView recycler_home;
    AlertDialog dialog;
    LayoutAnimationController layoutAnimationController;
    MyCategoriesAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this,root);
        initViews();
        homeViewModel.getMessageError().observe(getViewLifecycleOwner(), s ->  {
            Toast.makeText(getContext(),""+s,Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        homeViewModel.getCategoryListMutable().observe(getViewLifecycleOwner(),categoryModels -> {
            dialog.dismiss();
            adapter = new MyCategoriesAdapter(getContext(),categoryModels);
            recycler_home.setAdapter(adapter);
            recycler_home.setLayoutAnimation(layoutAnimationController);
        });
        return root;
    }

    private void initViews(){
        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        dialog.show();
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(adapter != null){
                    switch (adapter.getItemViewType(position)){
                        case Common
                                .DEFAULT_COLUMN_COUNT: return 1;
                        case Common.FULL_WIDTH_COLUMN: return 2;
                        default:return -1;
                    }
                }
                return -1;
            }
        });
        recycler_home.setLayoutManager(layoutManager);
        recycler_home.addItemDecoration(new SpacesItemDecoration(8));
    }
}