package com.zybooks.calendarappexample;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<event> {


    public EventAdapter(@NonNull Context context, List<event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        event event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

            TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);

           String eventTitle = event.getName() + ", "+ CalendarUtils.formattedTime(event.getTime());
            eventCellTV.setText(eventTitle);


            return convertView;
        }
        return convertView;



    }}
