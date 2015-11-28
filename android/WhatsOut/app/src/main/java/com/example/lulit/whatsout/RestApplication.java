package com.example.lulit.whatsout;

import android.content.Context;

public class RestApplication extends com.activeandroid.app.Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        RestApplication.context = this;
    }
    public static RestClient getRestClient() {
        return (RestClient) RestClient.getInstance(RestClient.class, RestApplication.context);
    }
}
