package com.madev.virtualwaitingroom.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.madev.virtualwaitingroom.Model.BranchesModel;
import com.madev.virtualwaitingroom.Model.CategoryModel;
import com.madev.virtualwaitingroom.Model.FCMSendData;
import com.madev.virtualwaitingroom.Model.User;
import com.madev.virtualwaitingroom.Model.UserAppointment;
import com.madev.virtualwaitingroom.Remote.FCMRetrofitClient;
import com.madev.virtualwaitingroom.Remote.IFCMService;

public class Common {
    public static final String COMMENT_REF = "Comments";
    public static int PICK_IMAGE_REQUEST = 71;
    public static final int DEFAULT_COLUMN_COUNT = 0 ;
    public static final int FULL_WIDTH_COLUMN = 1;
    public static final String CATEGORY_REF = "Category";
    public static final String USER_APPOINT = "UserAppoint";
    public static User currentUser;
    public static CategoryModel categorySelected;
    public static BranchesModel selectedFood;
    public static UserAppointment userAppointment;
    private static final String BASE_URL ="https://fcm.googleapis.com/";

    public static IFCMService getFCMClient(){
        return FCMRetrofitClient.getClient(BASE_URL).create(IFCMService.class);
    }

    public static boolean isConnectedToInternet (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null){

                for(int i=0;i<info.length;i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static String createTopicAppoint() {
        return new StringBuilder("/topics/new_appoint").toString();
    }
}
