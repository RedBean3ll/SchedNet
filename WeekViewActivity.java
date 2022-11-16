package com.zybooks.calendarappexample;

import static com.zybooks.calendarappexample.CalendarUtils.daysInMonthArray;
import static com.zybooks.calendarappexample.CalendarUtils.daysInWeekArray;
import static com.zybooks.calendarappexample.CalendarUtils.monthYearFromDate;
import static com.zybooks.calendarappexample.CalendarUtils.selectedDate;
import static com.zybooks.calendarappexample.event.eventsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.onItemListener{


    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    @SuppressLint("StaticFieldLeak")
    public static ListView eventListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();

        loadFromDBToMemory();

    }



    private void loadFromDBToMemory() {

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateEventListArray();
    }


    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }




    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);

    }



    public void previousWeekAction(View view){

        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }




    @Override
    public void onItemClick(int position, LocalDate date) {

        CalendarUtils.selectedDate = date;
        setWeekView();
        }

    @Override
    public void onItemLongClick(int position) {
        eventsList.remove(position);
        setEventAdapter();
    }

    @Override
        protected void onResume(){
        super.onResume();
        setEventAdapter();


        }


    //hides events if not on a certain day
    private void setEventAdapter() {
        ArrayList<event> dailyEvents = event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);

    }


    public void newEventAction(View view){
        startActivity(new Intent(this, EventEditActivity.class));
        }

    public void backAction(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
    }
