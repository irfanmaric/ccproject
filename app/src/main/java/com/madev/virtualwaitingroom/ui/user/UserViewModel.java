package com.madev.virtualwaitingroom.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Model.BranchesModel;
import com.madev.virtualwaitingroom.Model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> mutableLiveDataUsers;

    public UserViewModel() {

    }


    public MutableLiveData<User> getMutableLiveDataUsers() {
        if(mutableLiveDataUsers == null)
            mutableLiveDataUsers = new MutableLiveData<>();
        mutableLiveDataUsers.setValue(Common.currentUser);
        return mutableLiveDataUsers;
    }
}