package com.madev.virtualwaitingroom.Callback;

import com.madev.virtualwaitingroom.Model.CommentModel;

import java.util.List;

public interface ICommentCallbackListener {
    void onCommentLoadSuccess(List<CommentModel> commentModels);
    void onCommentLoadFailed(String message);
}
