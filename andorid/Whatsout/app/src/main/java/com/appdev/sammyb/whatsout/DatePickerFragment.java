package com.appdev.sammyb.whatsout;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerFragment extends android.app.DialogFragment implements
        DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceSateate) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Bundle mArgs = getArguments();
        String fragmentId = mArgs.getString("fragmentName");
        int btnId = mArgs.getInt("btn");

        Fragment fg = getFragmentManager().findFragmentByTag(fragmentId);
        EditText dateTxt = (EditText)(fg.getView()).findViewById(btnId);

        Date date = null;

        try {

            date = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day);
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            dateTxt.setText(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.dismiss();

    }
}