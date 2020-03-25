package com.bouchra.myapplicationechange.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    //la clé d'authentification Firebase afin que nous puissions authentifier le client.
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAABYElTa4:APA91bFcB2DAGX1slodra1BI47TNgw4jWvyhBASdDHKqvGZ0M9rCwBTjl2AWD7Z8kciNzHwRuabf149clOynQpYWSHyyH8gFF3V7T43RvPEgRbgznEDUJZRKdJdd9SadjQpNPOZGh85M"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
