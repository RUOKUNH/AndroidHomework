package com.bytedance.jstu.demo.lesson4.homework

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.lang.Math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 *  author : neo
 *  time   : 2021/10/25
 *  desc   :
 */
class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var InChangeState : Boolean = false
    var setAngle = arrayOf(0.0, 0.0, 0.0)

    var hour : Int = 0
    var minute : Int = 0
    var second : Int = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val hour_paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val minute_paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val second_paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val x_offset : Float = 992f / 2
    private val y_offset : Float = 992f / 2
    private val radius_out : Float = 990f / 2
    private val radius_in : Float = 930f / 2

    private val text_radius : Float = 800f / 2

    private val second_length = radius_out
    private val minute_length = 330f
    private val hour_length = 180f

    private var start_x : Float = 0f
    private var start_y : Float = 0f
    private var end_x : Float = 0f
    private var end_y : Float = 0f

    private val angle_per_gap : Double = Math.PI / 30

    var bounds : Rect = Rect()

    init{
        textPaint.color = Color.WHITE
        textPaint.strokeWidth = 10f
        textPaint.textSize = 60f
        hour_paint.color = Color.WHITE
        hour_paint.strokeWidth = 10f
        minute_paint.color = Color.CYAN
        minute_paint.strokeWidth = 10f
        second_paint.color = Color.GREEN
        second_paint.strokeWidth = 10f
    }

    private fun getContentSize(i : Int) : Pair<Int, Int> {
        val content = i.toString()
        textPaint.getTextBounds(content, 0, content.length, bounds)
        val textWidth = bounds.width()
        val textHeight = bounds.height()
        return textWidth to textHeight
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (i in 1..60) {
            paint.color = Color.WHITE
            var r_in = radius_in
            if(i % 5 == 0) {
                r_in -= 10f
                paint.strokeWidth = 10f
                val text_x = x_offset + Math.cos(Math.PI/2 - angle_per_gap * i).toFloat() * text_radius
                val text_y = y_offset - Math.sin(Math.PI/2 - angle_per_gap * i).toFloat() * text_radius
                val (textWidth, textHeight) = getContentSize(i/5)
                canvas?.drawText((i/5).toString(), text_x - textWidth / 2f, text_y + textHeight / 2f, textPaint)
            } else{
                paint.strokeWidth = 5f
            }
            start_x = (r_in * Math.cos(-Math.PI/2 + angle_per_gap * i)).toFloat() + x_offset
            start_y = (r_in * Math.sin(-Math.PI/2 + angle_per_gap * i)).toFloat() + y_offset
            end_x = (radius_out * Math.cos(-Math.PI/2 + angle_per_gap * i)).toFloat() + x_offset
            end_y = (radius_out * Math.sin(-Math.PI/2 + angle_per_gap * i)).toFloat() + y_offset
            canvas?.drawLine(start_x, start_y, end_x, end_y, paint)
        }

        start_x = x_offset
        start_y = y_offset
        // hour
        if (!InChangeState) {
            setAngle[0] = -Math.PI / 2 + (hour + minute / 60f + second / 3600f) * angle_per_gap * 5
        }
        end_x = x_offset + Math.cos(setAngle[0]).toFloat() * hour_length
        end_y = y_offset + Math.sin(setAngle[0]).toFloat() * hour_length
        canvas?.drawLine(start_x, start_y, end_x, end_y, hour_paint)
        // minute
        if (!InChangeState) {
            setAngle[1] = -Math.PI / 2 + (minute + second / 60f) * angle_per_gap
        }
        end_x = x_offset + Math.cos(setAngle[1]).toFloat() * minute_length
        end_y = y_offset + Math.sin(setAngle[1]).toFloat() * minute_length
        canvas?.drawLine(start_x, start_y, end_x, end_y, minute_paint)
        //second
        if (!InChangeState) {
            setAngle[2] = -Math.PI / 2 + second * angle_per_gap
        }
        end_x = x_offset + Math.cos(setAngle[2]).toFloat() * second_length
        end_y = y_offset + Math.sin(setAngle[2]).toFloat() * second_length
        canvas?.drawLine(start_x, start_y, end_x, end_y, second_paint)
    }
}