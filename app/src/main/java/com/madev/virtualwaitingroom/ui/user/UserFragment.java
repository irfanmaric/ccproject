package com.madev.virtualwaitingroom.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.madev.virtualwaitingroom.Model.User;
import com.madev.virtualwaitingroom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {

    private UserViewModel usersViewModel;
    private Unbinder unbinder;

    @BindView(R.id.ci_userProfilePhoto)
    CircleImageView ci_userProfilePhoto;
    @BindView(R.id.tv_username)
    TextView tv_username;
    @BindView(R.id.tv_phoneNumber)
    TextView tv_phoneNumber;
    @BindView(R.id.tv_points)
    TextView tv_points;
    @BindView(R.id.tv_app_counter)
    TextView tv_app_counter;
    @BindView(R.id.tv_app_done_counter)
    TextView tv_app_done_counter;
    @BindView(R.id.tv_app_error_counter)
    TextView tv_app_error_counter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        usersViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this,root);

       usersViewModel.getMutableLiveDataUsers().observe(getViewLifecycleOwner(),user -> {
           displayInfo(user);
       });
        return root;
    }

    private void displayInfo(User user) {
        Glide.with(getContext()).load(user.getImage()).into(ci_userProfilePhoto);
        tv_username.setText(new StringBuilder(user.getFirstName()+ " " + user.getLastName()));
        tv_phoneNumber.setText(new StringBuilder(user.getPhone()));
        tv_points.setText(new StringBuilder(user.getPoints()));
    }
}