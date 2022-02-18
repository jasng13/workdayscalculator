package com.example.workdayscalculator

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.MonthDay
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var startDate : LocalDate
    private lateinit var endDate : LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendar = Calendar.getInstance()

        val day = calendar[Calendar.DAY_OF_MONTH]
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]

        val holidays: Array<WorkDaysUtil.Holiday> = arrayOf(
            WorkDaysUtil.MovableHoliday(MonthDay.of(1, 1)),
            WorkDaysUtil.FixedHoliday(MonthDay.of(4, 25)),
            WorkDaysUtil.OccurrenceHoliday(Month.JUNE, DayOfWeek.MONDAY, 2)
        )

        setContentView(R.layout.activity_main)

        val startDateInput = findViewById<EditText>(R.id.startDateInput)
        val endDateInput = findViewById<EditText>(R.id.endDateInput)
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        startDateInput.setOnClickListener{
          val datePicker = DatePickerDialog(this@MainActivity,
                { view, year, month, dayOfMonth ->
                    startDate = LocalDate.of(year, month+1, dayOfMonth)
                    startDateInput.setText(resources.getString(R.string.date_format,
                        month + 1,dayOfMonth, year))
                }, year, month, day
            )

            datePicker.show()
        }

        endDateInput.setOnClickListener {
            val datePicker = DatePickerDialog(this@MainActivity,
                { view, year, month, dayOfMonth ->
                    endDate = LocalDate.of(year, month+1, dayOfMonth)
                    endDateInput.setText(resources.getString(R.string.date_format,
                        month + 1,dayOfMonth, year))

                }, year, month, day
            )
            datePicker.show()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                calculateButton.isEnabled =
                    !startDateInput.text.isNullOrEmpty() && !endDateInput.text.isNullOrEmpty()
            }

        }

        startDateInput.addTextChangedListener(textWatcher)
        endDateInput.addTextChangedListener(textWatcher)

        calculateButton.setOnClickListener {
            resultTextView.text = resources.getString(
                R.string.work_days,
                WorkDaysUtil.getNumWorkDays(startDate, endDate, holidays)
            )
        }

    }


}