package com.madev.virtualwaitingroom.Remote;

import com.madev.virtualwaitingroom.Model.FCMResponse;
import com.madev.virtualwaitingroom.Model.FCMSendData;
import com.madev.virtualwaitingroom.Model.MyResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAAzIWhCc:APA91bGBmP1HIvXmxuWE7wr-PPi6UdFS3KAHUZ-_rXln5H749SEcEBkpyaG6HauNs0UDHvl9aKFTheCq_ZjSw8PQtrq3Rqv_uiE4ucOLIvC7ZdPy9fDA5PjCzfpqbv3xFkCcYwJCd6c3"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body FCMSendData body);
}
