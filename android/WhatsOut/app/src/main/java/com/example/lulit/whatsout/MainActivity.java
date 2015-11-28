package com.example.lulit.whatsout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lulit.whatsout.models.EventModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.json.JSONArray;

import java.util.ArrayList;


import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ArrayList<EventModel> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listView = (ListView) findViewById(R.id.listView);

        RestClient client = RestApplication.getRestClient();

        final MainActivity that = this;

        client.getEvents(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jarray) {
                events = EventModel.fromJson(jarray);

                EventsAdapter eadapter = new EventsAdapter(that.getApplicationContext(), events);
                listView.setAdapter(eadapter);
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        //sync to calendar
        if (id == R.id.sync_calendar) {
            Intent calIntent = new Intent(Intent.ACTION_INSERT);
            calIntent.setType("vnd.android.cursor.item/event");

            DateTime sdate = new DateTime("2015-12-01T07:00:00.000Z");
            DateTime edate = new DateTime("2015-12-01T10:00:00.000Z");

            calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, sdate.getMillis()/1000);
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, edate.getMillis()/1000);
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
            calIntent.putExtra(CalendarContract.Events.TITLE, "Birthday");
            calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Birthday oo");
            calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Otaneimi");

            startActivity(calIntent);



        }

        return super.onOptionsItemSelected(item);
    }
}
