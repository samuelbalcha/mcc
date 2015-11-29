package com.appdev.sammyb.whatsout;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appdev.sammyb.whatsout.models.EventModel;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class EventEdit extends DialogFragment {
    EventModel mEvent;
    EditText titleEditText, descriptionEditText, dateEditText, dateEndEditText;

    public EventEdit(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance){

        View view = inflater.inflate(R.layout.edit_dialog, viewGroup, false);
        mEvent = (EventModel)getArguments().getSerializable("editEvent");

        Button btnSave = (Button)view.findViewById(R.id.btnDelete);
        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);

        titleEditText = (EditText)view.findViewById(R.id.txtTitle);
        descriptionEditText = (EditText)view.findViewById(R.id.txtDescription);
        dateEditText = (EditText)view.findViewById(R.id.txtDate);
        dateEndEditText = (EditText)view.findViewById(R.id.txtEndDate);

        reload(mEvent);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String title =  titleEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String date = dateEditText.getText().toString().trim();
                String end = dateEndEditText.getText().toString().trim();

                if(isNullOrEmpty(title))
                {
                    Toast.makeText(getActivity().getBaseContext(), "Empty title not allowed", Toast.LENGTH_LONG).show();
                    return;
                }
                saveEvent(title, description, date, end);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arg = new Bundle();
                arg.putString("fragmentName", "editEventFragment");
                arg.putInt("btn", R.id.txtDate);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(arg);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        dateEndEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arg = new Bundle();
                arg.putString("fragmentName", "editEventFragment");
                arg.putInt("btn", R.id.txtEndDate);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(arg);
                newFragment.show(getFragmentManager(), "datePicker");
            }
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

    public void saveEvent(String title, String desc, String date, String end)
    {
        RestClient client = RestApplication.getRestClient();
        JSONObject data = new JSONObject();
        try {
            data.put("title", title);
            data.put("description", desc);
            data.put("date", date);
            data.put("dateEnd", date);

        } catch (Exception ex) {
            // json exception
        }

        client.updateEvent(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jobject) {
               mEvent = EventModel.getEvent(jobject);

            }

        }, mEvent.id, data);

        this.dismiss();

        Intent intent = new Intent(getActivity().getBaseContext(), EventItemsDetailActivity.class);
        intent.putExtra("event", mEvent);
        startActivity(intent);
    }

    private void reload(EventModel evt){
        titleEditText.setText(evt.title);
        descriptionEditText.setText(evt.description);
        dateEditText.setText(evt.date);

    }

    private final static boolean isNullOrEmpty(String val)
    {
        return (val == null || val.trim().isEmpty());
    }
}