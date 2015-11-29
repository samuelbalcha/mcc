package com.appdev.sammyb.whatsout;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appdev.sammyb.whatsout.models.EventModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class EventDelete extends DialogFragment  {
    EventModel mEvent;
    public EventDelete(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance){

        View view = inflater.inflate(R.layout.delete_dialog, viewGroup, false);
        mEvent = (EventModel)getArguments().getSerializable("editEvent");

        Button btnDelete = (Button)view.findViewById(R.id.btnDelete);
        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){ deleteEvent(); }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { cancel(); }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

    }

    public void  cancel(){
        this.dismiss();
    }

    public void deleteEvent()
    {
        RestClient client = RestApplication.getRestClient();
        client.deleteEvent(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jobject) {

            }
        }, mEvent.id);

        this.dismiss();

        Intent intent = new Intent(getActivity().getBaseContext(), EventItemsListActivity.class);
        startActivity(intent);
    }

}

