package com.zybooks.schednet.Utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

class DateConversions {

    companion object {
        //TODO: SUBSTITUTE IN BETTER COMPATIBLE DATETIME

        fun formatDate(date: LocalDate): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            return date.format(formatter)
        }

        fun formatTime(time: LocalTime): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm::ss a")
            return time.format(formatter)
        }

        fun formatMonthYear(date: LocalDate): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            return date.format(formatter)
        }

        //TODO: May break from null in collection
        fun daysInMonth(date: LocalDate): ArrayList<LocalDate?> {
            val monthCollection: ArrayList<LocalDate?> = ArrayList()

            val daysInMonth = YearMonth.from(date).lengthOfMonth()
            val firstOfMonth = date.withDayOfMonth(1)
            val weekday = firstOfMonth.dayOfWeek.value

            var count = 1
            while (count <= 42) {
                if(count <= weekday || count > daysInMonth + weekday) {
                    monthCollection.add(null)
                } else {
                    monthCollection.add(LocalDate.of(date.year, date.month, count - weekday))
                }
                count++
            }
            return monthCollection
        }

        fun eventDaysInMonth(date: LocalDate): ArrayList<Boolean?> {

            val eventDayCollection: ArrayList<LocalDate> = ArrayList()
            val whereEventList: ArrayList<Boolean?> = arrayListOf(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false)
            val weekday = date.withDayOfMonth(1).dayOfWeek.value

            for(item: LocalDate in eventDayCollection) {
               whereEventList[item.dayOfMonth+weekday-1] = true
            }

            return whereEventList
        }

        /*
        @RequiresApi(Build.VERSION_CODES.O)
        fun daysInWeek(selectedDate: LocalDate): ArrayList<LocalDate> {
            val weekCollection: ArrayList<LocalDate> = ArrayList()
            var current: LocalDate? = ukn(selectedDate)
            val endDate: LocalDate? = current?.plusWeeks(1)

            while(current?.isBefore(endDate) == true) {
                weekCollection.add(current)
                current = current.plusDays(1)
            }

            return weekCollection
        }
         */

        fun dateTimeConversion(dateTime: ZonedDateTime): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            return dateTime.format(formatter)
        }

        fun ukn(current: LocalDate): LocalDate? {
            val previousWeek = current.minusWeeks(1)
            var localCurrent = current

            while (localCurrent.isAfter(previousWeek)) {
                if(current.dayOfWeek == DayOfWeek.SUNDAY) {
                    return localCurrent
                }
                localCurrent = current.minusDays(1)
            }
            return null
        }
    }
}