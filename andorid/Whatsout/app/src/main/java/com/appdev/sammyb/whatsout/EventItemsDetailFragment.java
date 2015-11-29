package com.appdev.sammyb.whatsout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdev.sammyb.whatsout.models.EventModel;

/**
 * A fragment representing a single EventItems detail screen.
 * This fragment is either contained in a {@link EventItemsListActivity}
 * in two-pane mode (on tablets) or a {@link EventItemsDetailActivity}
 * on handsets.
 */
public class EventItemsDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;
    private EventModel mEvent;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventItemsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvent = (EventModel)getArguments().getSerializable("event");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_event, container, false);

        if (mEvent != null) {
            ((TextView) rootView.findViewById(R.id.tvTitle)).setText(mEvent.title);
            ((TextView) rootView.findViewById(R.id.tvDate)).setText(mEvent.date);
        }

        return rootView;
    }

}
