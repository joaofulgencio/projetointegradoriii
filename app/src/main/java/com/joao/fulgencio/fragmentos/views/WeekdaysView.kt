package com.joao.fulgencio.fragmentos.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.Calendar

class WeekdaysView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = android.graphics.Color.RED // You can choose any color for the border
        strokeWidth = 4f // Set the border thickness
    }
    private val calendar = Calendar.getInstance()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val today = Calendar.getInstance() // Get today's date for comparison
        val weekDayToday = today.get(Calendar.DAY_OF_WEEK)
        val dayOfMonthToday = today.get(Calendar.DAY_OF_MONTH)

        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val diffToMonday = Calendar.MONDAY - currentDayOfWeek
        calendar.add(Calendar.DATE, diffToMonday)

        val widthStep = width / 5

        for (i in 0 until 5) {
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val x = widthStep * i + widthStep / 2f
            canvas.drawText(day.toString(), x, height / 2f, paint)

            // Check if this day is today
            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                day == dayOfMonthToday) {
                // Draw a rectangle around the current day
                val textBounds = Rect()
                paint.getTextBounds(day.toString(), 0, day.toString().length, textBounds)
                val textWidth = paint.measureText(day.toString())
                val textHeight = textBounds.height().toFloat()
                canvas.drawRect(x - textWidth / 2, height / 2f - textHeight, x + textWidth / 2, height / 2f + textHeight, borderPaint)
            }

            calendar.add(Calendar.DATE, 1)
        }
        // Reset the date back to today after drawing
        calendar.add(Calendar.DATE, -5)
    }
}