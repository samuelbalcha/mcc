package com.appdev.sammyb.whatsout;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import cz.msebera.android.httpclient.Header;

public class EventAdd extends DialogFragment  {

    EditText titleEditText;
    EditText descriptionEditText;
    EditText dateEditText;
    private  int year, month, day;
    String date;

    public EventAdd(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, final Bundle savedInstance){

        View view = inflater.inflate(R.layout.additem_dialog, viewGroup, false);

        Button btnSave = (Button)view.findViewById(R.id.btnDelete);
        Button btnCancel = (Button)view.findViewById(R.id.btnCancel);

        titleEditText = (EditText)view.findViewById(R.id.txtTitle);
        descriptionEditText = (EditText)view.findViewById(R.id.txtDescription);
        dateEditText = (EditText)view.findViewById(R.id.txtDate);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String title =  titleEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();

                if(isNullOrEmpty(title))
                {
                    Toast.makeText(getActivity().getBaseContext(), "Empty title not allowed", Toast.LENGTH_LONG).show();
                    return;
                }
                createEvent(title, description, date);
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

    public void createEvent(String title, String desc, String date)
    {
        final RestClient client = RestApplication.getRestClient();
        JSONObject data = new JSONObject();

        try {
            data.put("title", title);
            data.put("description", desc);
            data.put("date", date);

        } catch (Exception ex) {
            // json exception
        }

        client.createEvent(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jobject) {

            }

        }, data);

        this.dismiss();
    }

    private final static boolean isNullOrEmpty(String val)
    {
        return (val == null || val.trim().isEmpty());
    }

}
