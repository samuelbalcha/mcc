package com.example.lulit.whatsout;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.lulit.whatsout.models.EventModel;


public class EventActivity extends FragmentActivity {

    private static Context context;
    EventDetailFragment fragmentEventDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetail);

        EventModel event = (EventModel) getIntent().getSerializableExtra("event");
        if(savedInstanceState == null) {
           /* fragmentEventDetail = new EventDetailFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id., fragmentEventDetail);
            ft.commit();*/
        }


    }


}
