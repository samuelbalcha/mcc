package com.appdev.sammyb.whatsout;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.appdev.sammyb.whatsout.models.EventModel;


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
