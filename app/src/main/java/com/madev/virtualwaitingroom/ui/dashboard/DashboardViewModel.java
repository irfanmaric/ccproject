package com.madev.virtualwaitingroom.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.platforminfo.UserAgentPublisher;
import com.madev.virtualwaitingroom.Callback.ICategoryCallbackListener;
import com.madev.virtualwaitingroom.Callback.IUserAppointmentCallbackListener;
import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Model.BranchesModel;
import com.madev.virtualwaitingroom.Model.CategoryModel;
import com.madev.virtualwaitingroom.Model.UserAppointment;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel implements IUserAppointmentCallbackListener {

    private MutableLiveData<List<UserAppointment>> mutableLiveDataUserAppointList;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private IUserAppointmentCallbackListener appointmentCallbackListener;

    public DashboardViewModel() {
        appointmentCallbackListener = this;
    }

    public MutableLiveData<List<UserAppointment>> getMutableLiveDataUserAppointList() {
        if(mutableLiveDataUserAppointList == null){
            mutableLiveDataUserAppointList = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadAppointment();
        }
        return mutableLiveDataUserAppointList;
    }

    private void loadAppointment() {
        List<UserAppointment> tempList = new ArrayList<>();
        DatabaseReference userAppointReference = FirebaseDatabase.getInstance().getReference(Common.USER_APPOINT);
        userAppointReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnapshot:snapshot.getChildren()){
                    UserAppointment userAppointment = itemSnapshot.getValue(UserAppointment.class);

                    tempList.add(userAppointment);
                }
                appointmentCallbackListener.onAppointLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                appointmentCallbackListener.onAppointLoadFailed(error.getMessage());
            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }


    @Override
    public void onAppointLoadSuccess(List<UserAppointment> userAppointmentList) {
        mutableLiveDataUserAppointList.setValue(userAppointmentList);

    }

    @Override
    public void onAppointLoadFailed(String message) {
        messageError.setValue(message);
    }
}