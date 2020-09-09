package com.example.reminder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePicker extends DialogFragment
{
    Calendar mCalendar = Calendar.getInstance();
    int year = mCalendar.get(Calendar.YEAR);
    int month = mCalendar.get(Calendar.MONTH);
    int day = mCalendar.get(Calendar.DAY_OF_MONTH);
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(),
                (DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);
    }
}
