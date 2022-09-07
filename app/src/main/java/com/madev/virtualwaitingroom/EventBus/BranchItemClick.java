package com.madev.virtualwaitingroom.EventBus;

import com.madev.virtualwaitingroom.Model.BranchesModel;

public class BranchItemClick {
    private boolean success;
    private BranchesModel branchesModel;

    public BranchItemClick(boolean success, BranchesModel branchesModel) {
        this.success = success;
        this.branchesModel = branchesModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BranchesModel getBranchesModel() {
        return branchesModel;
    }

    public void setBranchesModel(BranchesModel branchesModel) {
        this.branchesModel = branchesModel;
    }
}
