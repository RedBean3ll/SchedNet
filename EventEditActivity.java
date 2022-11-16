package com.zybooks.calendarappexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity {


    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private LocalTime time;
    Button timeButton;
    int hour, minute;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        timeButton = findViewById(R.id.timeButton);

    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
    }

    public void SaveEventAction(View view){

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String eventName = eventNameET.getText().toString();
        event newEvent = new event(eventName, CalendarUtils.selectedDate, time);
        event.eventsList.add(newEvent);
        sqLiteManager.addEventToDatabase(newEvent);
        finish();
    }

    public void popTimePicker(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));

            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("SelectTime");
        timePickerDialog.show();
    }
}