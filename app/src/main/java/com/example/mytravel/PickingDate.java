package com.example.mytravel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PickingDate extends MainActivity {
    View view;
    private Button datePickerOutButton, datePickerInButton;

    public PickingDate(View view, Button datePickerOutButton, Button datePickerInButton) {
        this.view = view;
        this.datePickerOutButton = datePickerOutButton;
        this.datePickerInButton = datePickerInButton;
    }

    public DatePickerDialog initDatePicker() {
        Button button = (Button) view;
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            button.setText(date);
            String dateOut = datePickerOutButton.getText().toString();
            String dateIn = datePickerInButton.getText().toString();
            if (!dateOut.isEmpty() && !dateIn.isEmpty()) {
                if (!datesToCompare(dateOut, dateIn)) {
                    datePickerInButton.setText(dateOut);
                    Toast.makeText(getContext(),"Podano błędy zakres dat", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        return new DatePickerDialog(this.view.getContext(), style, dateSetListener, year, month, day);
    }

    public String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    public String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "Jan";
        }
    }

    private boolean datesToCompare(String dateOut, String dateIn) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        try {
            Date d1 = formatter.parse(dateOut);
            Date d2 = formatter.parse(dateIn);
            if(d2.before(d1))
            {
                return false;
            }
        }
        catch(ParseException e)
        {
            System.out.println(e);
        }
        return true;
    }
}
