package com.madev.virtualwaitingroom.Callback;

import com.madev.virtualwaitingroom.Model.CategoryModel;
import com.madev.virtualwaitingroom.Model.UserAppointment;

import java.util.List;

public interface IUserAppointmentCallbackListener {
    void onAppointLoadSuccess(List<UserAppointment> userAppointmentList);
    void onAppointLoadFailed(String message);
}
