package com.appdev.sammyb.whatsout;

import android.content.Context;

public class RestApplication extends com.activeandroid.app.Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        RestApplication.context = this;
    }
    public static RestClient getRestClient() {
        return RestClient.getInstance(RestClient.class, RestApplication.context);
    }
}
