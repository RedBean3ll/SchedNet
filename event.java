package com.zybooks.calendarappexample;

import android.graphics.Color;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class event {


    public static ArrayList<event> eventsList = new ArrayList<>();

    public static ArrayList<event> eventsForDate(LocalDate date){
        ArrayList<event> events = new ArrayList<>();
        for(event event: eventsList){
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    private int id;
    private String name;
    private LocalDate date;
    private LocalTime time;

    public event(String name, LocalDate date, LocalTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
