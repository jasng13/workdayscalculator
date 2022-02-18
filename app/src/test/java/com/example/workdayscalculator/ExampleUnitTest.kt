package com.example.workdayscalculator

import org.junit.Test

import org.junit.Assert.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.MonthDay

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun `start date and end date is the same`() {
        val holidays: Array<WorkDaysUtil.Holiday> = arrayOf(
            WorkDaysUtil.MovableHoliday(MonthDay.of(1, 1)),
            WorkDaysUtil.FixedHoliday(MonthDay.of(4, 25)),
            WorkDaysUtil.OccurrenceHoliday(Month.JUNE, DayOfWeek.MONDAY, 2)
        )

        val startDate = LocalDate.of(2022,1,1)
        val endDate = LocalDate.of(2022,1,1)
        val result = WorkDaysUtil.getNumWorkDays(startDate, endDate,holidays) == 0

        assert(result)
    }

    @Test
    fun `get workdays with fixed holidays`() {
        val holidays: Array<WorkDaysUtil.Holiday> = arrayOf(
            WorkDaysUtil.MovableHoliday(MonthDay.of(1, 1)),
            WorkDaysUtil.FixedHoliday(MonthDay.of(4, 25)),
            WorkDaysUtil.OccurrenceHoliday(Month.JUNE, DayOfWeek.MONDAY, 2)
        )
        val startDate = LocalDate.of(2022,1,1)
        val endDate = LocalDate.of(2022,1,31)
        val result = WorkDaysUtil.getNumWorkDays(startDate, endDate,holidays) == 20

        assert(result)
    }

    @Test
    fun `get workdays with special holidays`() {
        val holidays: Array<WorkDaysUtil.Holiday> = arrayOf(
            WorkDaysUtil.MovableHoliday(MonthDay.of(1, 1)),
            WorkDaysUtil.FixedHoliday(MonthDay.of(4, 25)),
            WorkDaysUtil.OccurrenceHoliday(Month.JUNE, DayOfWeek.MONDAY, 2)
        )
        val startDate = LocalDate.of(2021,1,1)
        val endDate = LocalDate.of(2021,12,31)

        val result = WorkDaysUtil.getNumWorkDays(startDate, endDate, holidays) == 259

        assert(result)
    }


}