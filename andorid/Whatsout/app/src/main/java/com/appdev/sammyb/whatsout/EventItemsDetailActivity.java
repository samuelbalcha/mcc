package com.appdev.sammyb.whatsout;

import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appdev.sammyb.whatsout.models.EventModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.joda.time.DateTime;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * An activity representing a single EventItems detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link EventItemsListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link EventItemsDetailFragment}.
 */
public class EventItemsDetailActivity extends ActionBarActivity {
    private EventModel mEvent;
    private static final String whatOutCalendarText = "Whats OUT";
    private int whatOutCalendarId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventitems_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            mEvent = (EventModel)getIntent().getSerializableExtra("event");

            arguments.putSerializable("event", mEvent);

            EventItemsDetailFragment fragment = new EventItemsDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.eventitems_detail_container, fragment)
                    .commit();

            Button btnEdit = (Button)findViewById(R.id.btnUpdate);
            Button btnDelete = (Button)findViewById(R.id.btnDelete);
            Button btnSync = (Button)findViewById(R.id.btnSync);


            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    showDialogFragment("edit");
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    showDialogFragment("delete");
                }
            });

            btnSync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    // Event has already been synced
                    if(mEvent.gid == null) {

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

                                ContentResolver cr = getContentResolver();
                                ContentValues values = new ContentValues();
                                System.out.println(mEvent.sdate.getMillis());
                                System.out.println(mEvent.title);

                                values.put(CalendarContract.Events.DTSTART, mEvent.sdate.getMillis());
                                values.put(CalendarContract.Events.DTEND, mEvent.edate.getMillis());
                                values.put(CalendarContract.Events.TITLE, mEvent.title);
                                values.put(CalendarContract.Events.DESCRIPTION, mEvent.description);
                                values.put(CalendarContract.Events.CALENDAR_ID, whatOutCalendarId);
                                values.put(CalendarContract.Events.EVENT_LOCATION, mEvent.location);
                                values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Helsinki");

                                try {
                                    Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

                                    // get the event ID that is the last element in the Uri
                                    long eventID = Long.parseLong(uri.getLastPathSegment());

                                    Toast toast = Toast.makeText(getApplicationContext(), "Event synced!", Toast.LENGTH_SHORT);
                                    toast.show();

                                    // update event id as gid to avoid duplicates
                                    RestClient client = RestApplication.getRestClient();
                                    JSONObject data = new JSONObject();
                                    try {
                                        data.put("gid", eventID);
                                    } catch (Exception ex) {
                                        // json exception
                                        ex.printStackTrace();
                                    }

                                    client.updateEvent(new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject jobject) {
                                            mEvent = EventModel.getEvent(jobject);
                                        }
                                    }, mEvent.id, data);

                                    Toast rtoast = Toast.makeText(getApplicationContext(), "Event synced!", Toast.LENGTH_SHORT);
                                    rtoast.show();

                                } catch(SecurityException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Event has already been synced!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, EventItemsListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogFragment(String name){

        FragmentManager fm = getFragmentManager();
        Bundle args = new Bundle();
        args.putSerializable("editEvent", mEvent);

        if(name == "edit"){
            EventEdit eventEdit = new EventEdit();
            eventEdit.setArguments(args);
            eventEdit.show(fm, "dialog fragment");
        }
        if(name == "delete"){
            EventDelete eventDelete = new EventDelete();
            eventDelete.setArguments(args);
            eventDelete.show(fm, "dialog fragment");
        }
    }
}
