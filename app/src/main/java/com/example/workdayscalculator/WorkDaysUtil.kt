package com.example.workdayscalculator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.*
import java.time.DayOfWeek.*
import java.util.*


class WorkDaysUtil {

    sealed class Holiday {
        abstract fun getDate(year: Int): LocalDate
    }

    data class FixedHoliday(val monthDay: MonthDay): Holiday() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun getDate(year: Int) = this.monthDay.atYear(year)
    }

    data class MovableHoliday(val monthDay: MonthDay): Holiday() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun getDate(year: Int): LocalDate {
            val date = this.monthDay.atYear(year)
            val dow = date.dayOfWeek
            val offset: Long =
                when (dow) {
                    SATURDAY -> { 2 }
                    SUNDAY -> { 1 }
                    else -> { 0 }
                }

            return date.plusDays(offset)
        }
    }

    data class OccurrenceHoliday(val month: Month, val dow: DayOfWeek, val nth: Int): Holiday() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun getDate(year: Int): LocalDate {
            val firstDateOfMonth = LocalDate.of(year, this.month, 1)
            val firstDow = firstDateOfMonth.dayOfWeek
            val offsetToTargetDow = (this.dow.value + 7 - firstDow.value) % 7
            val offsetWeeks = 7 * (this.nth - 1)
            val totalOffset = offsetToTargetDow + offsetWeeks

            return firstDateOfMonth.plusDays(totalOffset.toLong())
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    companion object {
        fun getNumWorkDays(startDate: LocalDate, endDate: LocalDate, holidays: Array<Holiday>): Int {

            var numWorkDays = 0

            val holidaysDateSet = sortedSetOf<LocalDate>()
            for (year in startDate.plusDays(1).year..endDate.minusDays(1).year) {
                holidaysDateSet.addAll(holidays.map { it.getDate(year) })
            }

            //alternative once java 9 is available: startDate.plusDays(1).datesUntil(endDate)
            for (date in getDates(startDate.plusDays(1), endDate)) {
                val dow = date.dayOfWeek

                if (dow.value >= 6) { // get mon - fri only
                    continue
                }

                if (date in holidaysDateSet) {
                    continue
                }

                numWorkDays++
            }

            return numWorkDays
        }

        private fun getDates(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
            val dates: ArrayList<LocalDate> = ArrayList<LocalDate>()
            val startCalendar: Calendar = Calendar.getInstance()
            val convertedStartDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            startCalendar.time = convertedStartDate

            val endCalendar : Calendar = Calendar.getInstance()
            val convertedEndDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            endCalendar.time = convertedEndDate

            val cal1: Calendar = Calendar.getInstance()
            cal1.time = convertedStartDate

            val cal2: Calendar = Calendar.getInstance()
            cal2.time = convertedEndDate

            while (!cal1.after(cal2)) {
                dates.add(
                    cal1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                )
                cal1.add(Calendar.DATE, 1)
            }
            return dates
        }
    }


}

