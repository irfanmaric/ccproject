package com.madev.virtualwaitingroom.ui.branchdetails;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Model.BranchesModel;
import com.madev.virtualwaitingroom.Model.CommentModel;

public class BranchDetailsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<BranchesModel> mutableLiveDataBranches;
    private MutableLiveData<CommentModel> mutableLiveDataComment;


    public void setCommentModel(CommentModel commentModel){
        if(mutableLiveDataComment != null)
            mutableLiveDataComment.setValue(commentModel);
    }

    public MutableLiveData<CommentModel> getMutableLiveDataComment() {
        return mutableLiveDataComment;
    }

    public BranchDetailsViewModel (){
        mutableLiveDataComment = new MutableLiveData<>();
    }

    public MutableLiveData<BranchesModel> getMutableLiveDataBranches() {
        if(mutableLiveDataBranches == null)
            mutableLiveDataBranches = new MutableLiveData<>();
        mutableLiveDataBranches.setValue(Common.selectedFood);
        return mutableLiveDataBranches;
    }

    public void setBranchModel(BranchesModel branchesModel) {
        if(branchesModel != null)
        mutableLiveDataBranches.setValue(branchesModel);
    }
}