package com.zybooks.schednet.model

import androidx.annotation.Nullable
import java.time.LocalDate

/**
 *  File: CalendarObject.kt
 *  @author Matthew Clark
 */

class CalendarObject(@Nullable day: LocalDate?, @Nullable eventPresent: Boolean?) {

    var calendarDay: LocalDate?
    var isActive: Boolean

    init {
        calendarDay = day
        isActive = eventPresent ?: false
    }

    override fun toString(): String {
        return "${calendarDay.toString()}:$isActive"
    }
}