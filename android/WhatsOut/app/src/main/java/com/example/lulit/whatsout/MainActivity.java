package com.example.lulit.whatsout;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;


import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ArrayList<EventModel> events;

    private static final String whatOutCalendarText = "Whats OUT";
    private int whatOutCalendarId;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

            // Query for available calendars and fetch whats out calendar
            Cursor cursor = getContentResolver().query(Uri.parse("content://com.android.calendar/calendars"),
                    new String[]{"_id", "calendar_displayName", "account_name"}, "", null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String[] calNames = new String[cursor.getCount()];
                int[] calIds = new int[cursor.getCount()];
                for (int i = 0; i < calNames.length; i++) {
                    calIds[i] = cursor.getInt(0);
                    calNames[i] = cursor.getString(1);
                    System.out.println("calid=" + cursor.getInt(0) + " calname=" + cursor.getString(1));
                    if(cursor.getString(1).equals(whatOutCalendarText)) {
                        whatOutCalendarId = cursor.getInt(0);
                    }
                    cursor.moveToNext();
                }
                cursor.close();
                if (whatOutCalendarId > 0) {



                    DateTime sdate = new DateTime("2015-12-02T07:00:00.000Z");
                    DateTime edate = new DateTime("2015-12-02T10:00:00.000Z");


                    /*
                     ****Add calendar with intent
                     *
                    Intent calIntent = new Intent(Intent.ACTION_INSERT);
                    calIntent.setType("vnd.android.cursor.item/event");
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, sdate.getMillis());
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, edate.getMillis());
                    calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                    calIntent.putExtra(CalendarContract.Events.TITLE, "Birthdayyy");
                    calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Birthday oo");
                    calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Otaneimi");
                    calIntent.putExtra(CalendarContract.Events.CALENDAR_ID, whatOutCalendarId);
                    startActivity(calIntent);*/

                    ContentResolver cr = getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(CalendarContract.Events.DTSTART, sdate.getMillis());
                    values.put(CalendarContract.Events.DTEND, edate.getMillis());
                    values.put(CalendarContract.Events.TITLE, "Jazzercise");
                    values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
                    values.put(CalendarContract.Events.CALENDAR_ID, whatOutCalendarId);
                    values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Helsinki");

                    try {
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

                        // get the event ID that is the last element in the Uri
                        long eventID = Long.parseLong(uri.getLastPathSegment());

                        System.out.println(eventID);

                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lulit.whatsout/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lulit.whatsout/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }
}
