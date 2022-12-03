package com.zybooks.schednet.utils

import android.content.Context
import com.zybooks.schednet.model.CalendarObject
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.collections.ArrayList

/**
 *  File: DateConversions.kt
 *  @author Matthew Clark
 *  @author Amanda Thomas
 */

class DateConversions {

    companion object {
        fun formatTime(time: ZonedDateTime): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
            return time.format(formatter)
        }

        fun formatMonthYear(date: LocalDate): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            return date.format(formatter)
        }

        fun formatListDate(date: ZonedDateTime): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, y")
            return date.format(formatter)
        }

        fun getDateStampTodo(indexedValue: Int): Long {
            var currentDay: ZonedDateTime = ZonedDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)

            when(indexedValue) {
                1 -> return System.currentTimeMillis()
                2 -> {
                    return currentDay.toInstant().toEpochMilli()
                }
                3 -> {
                    currentDay = currentDay.minusWeeks(1)
                    return currentDay.toInstant().toEpochMilli()
                }
                4 -> {
                    currentDay = currentDay.minusMonths(1)
                    return currentDay.toInstant().toEpochMilli()
                }
                5 -> {
                    currentDay = currentDay.minusYears(1)
                    return  currentDay.toInstant().toEpochMilli()
                }
                else -> return 0
            }
        }

        fun getDateStampCalendar(indexedValue: Int): Long {
            var currentDay: ZonedDateTime = ZonedDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)

            when(indexedValue) {
                1 -> return System.currentTimeMillis()
                2 -> {
                    currentDay = currentDay.minusWeeks(1)
                    return currentDay.toInstant().toEpochMilli()
                }
                3 -> {
                    currentDay = currentDay.minusMonths(1)
                    return currentDay.toInstant().toEpochMilli()
                }
                4 -> {
                    currentDay = currentDay.minusYears(1)
                    return currentDay.toInstant().toEpochMilli()
                }
                else -> return 0
            }
        }

        fun daysInMonth(context: Context, id: Int, date: LocalDate): MutableList<CalendarObject> {
            //PART 1: CREATE LIST
            val monthCollection: MutableList<CalendarObject> = ArrayList()

            val daysInMonth = YearMonth.from(date).lengthOfMonth()
            val firstOfMonth = date.withDayOfMonth(1)
            val weekday = firstOfMonth.dayOfWeek.value

            var count = 1
            while (count <= 42) {
                if(count <= weekday || count > daysInMonth + weekday) {
                    monthCollection.add(CalendarObject(null, null))
                } else {
                    monthCollection.add(CalendarObject(LocalDate.of(date.year, date.month, count - weekday), null))
                }
                count++
            }

            //PART 2: ASSIGN ACTIVE VALUE
            val monthDateTime = ZonedDateTime.of(date, LocalTime.of(0,0,0,0), ZoneId.systemDefault())
            val monthsEvents: ArrayList<ZonedDateTime> = DatabaseManager(context).readMonthlyEventTimestamp(id, monthDateTime.toInstant().toEpochMilli(), YearMonth.of(monthDateTime.year, monthDateTime.dayOfMonth).lengthOfMonth()-1)

            for(item: ZonedDateTime in monthsEvents) {
                monthCollection[item.dayOfMonth+weekday-1].isActive = true
            }

            return monthCollection
        }

        fun getNewEventPosition(date: ZonedDateTime, month: LocalDate): Int {
            if(date.monthValue != month.monthValue) { return -1 }

            val weekday = month.dayOfWeek.value
            return date.dayOfMonth+weekday-1
        }

        fun dateTimeConversion(dateTime: ZonedDateTime): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            return dateTime.format(formatter)
        }
    }
}