package com.madev.virtualwaitingroom.ui.comments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.madev.virtualwaitingroom.Model.BranchesModel;
import com.madev.virtualwaitingroom.Model.CommentModel;

import java.util.List;

public class CommentViewModel extends ViewModel {
    private MutableLiveData<List<CommentModel>> mutableLiveDataCommentList;


    public CommentViewModel() {
        mutableLiveDataCommentList = new MutableLiveData<>();
    }


    public MutableLiveData<List<CommentModel>> getMutableLiveDataBranchList() {
        return mutableLiveDataCommentList;
    }

    public void setCommentList(List<CommentModel> commentList){
        mutableLiveDataCommentList.setValue(commentList);
    }
}
