package com.osdifa.anrifo;

import com.osdifa.anrifo.Notification.MyResponse;
import com.osdifa.anrifo.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAR-UFXOw:APA91bFhPjsajydYuC7EtLh_ZeQM4pt4bt232HH-81ImwJqWaESnqt-XYYhbd4TiA2_ANQZ24Us4vXZweujfpIZR6HJnGu8J4sNsThaRefx6yuqCLDPIyogsTVqZ3QTYl0W1V0-5opWu"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
