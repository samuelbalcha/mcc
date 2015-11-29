package com.appdev.sammyb.whatsout;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.appdev.sammyb.whatsout.models.EventModel;

public class EventItemsListActivity extends ActionBarActivity
        implements EventItemsListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventitems_list);

        if (findViewById(R.id.eventitems_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((EventItemsListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.eventitems_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link EventItemsListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     * @param evt
     */
    @Override
    public void onItemSelected(EventModel evt) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable("event", evt);
            EventItemsDetailFragment fragment = new EventItemsDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.eventitems_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, EventItemsDetailActivity.class);

            detailIntent.putExtra("event", evt);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater =  getMenuInflater(); // getSupportMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();

        switch (item.getItemId()) {
            case R.id.btnAdd:
                EventAdd eventAdd = new EventAdd();
                eventAdd.show(fm, "addEventFragment");
                return true;

            default:
                break;
        }

        return false;
    }

}
