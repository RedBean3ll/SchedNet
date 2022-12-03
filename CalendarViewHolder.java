package com.zybooks.schednet;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.schednet.CalendarAdapter;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final ArrayList<LocalDate> days;
    public final View parentView;
    public final TextView dayOfMonth;
    private final CalendarAdapter.onItemListener onItemListener;

    public CalendarViewHolder( @NonNull View itemView, CalendarAdapter.onItemListener onItemListener,ArrayList<LocalDate> days){

        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.days = days;
    }

    public void onClick(View view){
        onItemListener.onItemClick(getAdapterPosition(),days.get(getAdapterPosition()));
    }
}
