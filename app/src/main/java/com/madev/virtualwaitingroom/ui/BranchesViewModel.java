package com.madev.virtualwaitingroom.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Model.BranchesModel;

import java.util.List;

public class BranchesViewModel extends ViewModel {

    private MutableLiveData<List<BranchesModel>> mutableLiveDataBranchList;
    // TODO: Implement the ViewModel

    public BranchesViewModel (){

    }

    public MutableLiveData<List<BranchesModel>> getMutableLiveDataBranchList() {
        if(mutableLiveDataBranchList == null)
            mutableLiveDataBranchList = new MutableLiveData<>();
        mutableLiveDataBranchList.setValue(Common.categorySelected.getBranches());
        return mutableLiveDataBranchList;
    }
}