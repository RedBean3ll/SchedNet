package com.zybooks.schednet;

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


    private String name;
    private LocalDate date;
    private LocalTime time;

    public event(String name, LocalDate date, LocalTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
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
