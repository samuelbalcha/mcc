package com.appdev.sammyb.whatsout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.appdev.sammyb.whatsout.models.EventModel;

import java.util.ArrayList;

public class EventsAdapter extends ArrayAdapter<EventModel> {
    public EventsAdapter(Context context, ArrayList<EventModel> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EventModel event = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        //TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);


        tvTitle.setText(event.title);
        //tvDescription.setText(event.description);
        tvDate.setText(event.date);

        return convertView;
    }
}
