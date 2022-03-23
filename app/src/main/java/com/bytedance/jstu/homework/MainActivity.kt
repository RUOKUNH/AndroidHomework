package com.bytedance.jstu.homework

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bytedance.jstu.demo.lesson4.homework.ClockView
import java.time.LocalDateTime
import java.util.*
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    companion object {
        const val LOCK_HOUR = 0
        const val LOCK_MINUTE = 1
        const val LOCK_SECOND = 2
        const val NO_LOCK = 3
    }
    private var second_offset : Int = 0
    private var time_set : Boolean = true
    private var cur_time = IntArray(3)
    private var lock_state = NO_LOCK
    private var clockV : ClockView? = null
    private var change_bt : Button? = null
    private var InChangeState : Boolean = false

    private val handler : Handler = Handler(Looper.getMainLooper()) { msg ->
        run {
            cur_time[0] = msg.data["hour"] as Int
            cur_time[1] = msg.data["minute"] as Int
            cur_time[2] = msg.data["second"] as Int
            clockV?.hour = msg.data["hour"] as Int
            clockV?.minute = msg.data["minute"] as Int
            clockV?.second = msg.data["second"] as Int
            clockV?.invalidate()

            findViewById<TextView>(R.id.time).text = msg.data["time"].toString()
        }
        true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clockV = findViewById(R.id.clock)
        change_bt = findViewById(R.id.change)
        change_bt?.setOnClickListener {
            time_set = false
            when (InChangeState) {
                false -> {
                    InChangeState = true
                    clockV?.InChangeState = true
                    change_bt?.text = "完成设置"
                }
                true -> {
                    InChangeState = false
                    clockV?.InChangeState = false
                    change_bt?.text = "修改时间"
                }
            }
        }

        findViewById<Button>(R.id.restore).setOnClickListener {
            second_offset = 0
        }

        clockV?.setOnTouchListener { v, event ->

            val action: Int? = event?.action

            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("Touch", "${event.getX()}, ${event.getY()}")
                    if(InChangeState) {
                        setLockState(event.x.toDouble(), event.y.toDouble())
                        Log.d("LockState", lock_state.toString())
                    }
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    when(lock_state) {
                        LOCK_HOUR -> {
                            val hour_angle = Math.atan2(event.y.toDouble()-496, event.x.toDouble()-496)
                            clockV?.setAngle?.set(0, hour_angle)
                            val hour = (((hour_angle + 5 * Math.PI / 2) % (Math.PI * 2)) / (Math.PI / 6)).toInt()
                            if (Math.abs(cur_time[0] - hour) < Math.abs(cur_time[0] - (hour+12))) {
                                cur_time[0] = hour
                            } else {
                                cur_time[0] = hour + 12
                            }
                            findViewById<TextView>(R.id.time).text = String.format("%02d:%02d:%02d", cur_time[0], cur_time[1], cur_time[2])
                            clockV?.invalidate()
                        }
                        LOCK_MINUTE -> {
                            val minute_angle = Math.atan2(event.y.toDouble()-496, event.x.toDouble()-496)
                            clockV?.setAngle?.set(1, minute_angle)
                            val minute = (((minute_angle + 5 * Math.PI / 2) % (Math.PI * 2)) / (Math.PI / 30)).toInt()
                            cur_time[1] = minute
                            findViewById<TextView>(R.id.time).text = String.format("%02d:%02d:%02d", cur_time[0], cur_time[1], cur_time[2])
                            clockV?.invalidate()
                        }
                        LOCK_SECOND -> {
                            val second_angle = Math.atan2(event.y.toDouble()-496, event.x.toDouble()-496)
                            clockV?.setAngle?.set(2, second_angle)
                            val second = (((second_angle + 5 * Math.PI / 2) % (Math.PI * 2)) / (Math.PI / 30)).toInt()
                            cur_time[2] = second
                            findViewById<TextView>(R.id.time).text = String.format("%02d:%02d:%02d", cur_time[0], cur_time[1], cur_time[2])
                            clockV?.invalidate()
                        }
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    lock_state = NO_LOCK
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    lock_state = NO_LOCK
                    true
                }
                MotionEvent.ACTION_OUTSIDE -> {
                    lock_state = NO_LOCK
                    true
                }
                else -> super.onTouchEvent(event)
            }
        }

        val timeThread = TimeThread()
        timeThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setLockState(touch_x : Double, touch_y : Double) {
        val angle_second = (cur_time[2]%60) / 30f * Math.PI
        val angle_minute = (cur_time[1]%60) / 30f * Math.PI + angle_second / 60f
        val angle_hour = (cur_time[0]%12) / 6f * Math.PI + angle_minute / 12f
        val angle_touch = (Math.atan2(touch_y-496, touch_x-496) + Math.PI * 5 / 2) % (Math.PI * 2)
        if (Math.abs(angle_second - angle_touch) < Math.PI / 9) {
            lock_state = LOCK_SECOND
        } else if (Math.abs(angle_minute - angle_touch) < Math.PI / 9) {
            lock_state = LOCK_MINUTE
        } else if (Math.abs(angle_hour - angle_touch) < Math.PI / 9) {
            lock_state = LOCK_HOUR
        } else {
            lock_state = NO_LOCK
        }
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        val action: Int? = event?.action
//
//        return when (action) {
//            MotionEvent.ACTION_DOWN -> {
//                Log.d("Touch", "${event.getX()}, ${event.getY()}")
//                if(InChangeState) {
//                    setLockState(event.x.toDouble(), event.y.toDouble())
//                    Log.d("LockState", lock_state.toString())
//                }
//                true
//            }
//            MotionEvent.ACTION_MOVE -> {
//                when(lock_state) {
//                    LOCK_HOUR -> {
//                        val hour_angle = Math.atan2(event.y.toDouble()-496, event.x.toDouble()-496)
//                        clockV?.setAngle?.set(0, hour_angle)
//                        val hour = (((hour_angle + 5 * Math.PI / 2) % (Math.PI * 2)) / (Math.PI / 6)).toInt()
//                        if (Math.abs(cur_time[0] - hour) < Math.abs(cur_time[0] - (hour+12))) {
//                            cur_time[0] = hour
//                        } else {
//                            cur_time[0] = hour + 12
//                        }
//                        String.format("%02d:%02d:%02d", cur_time[0], cur_time[1], cur_time[2])
//                    }
//                    LOCK_MINUTE -> {
//                        val minute_angle = Math.atan2(event.y.toDouble()-496, event.x.toDouble()-496)
//                        clockV?.setAngle?.set(1, minute_angle)
//                        val minute = (((minute_angle + 5 * Math.PI / 2) % (Math.PI * 2)) / (Math.PI / 30)).toInt()
//                        cur_time[1] = minute
//                        String.format("%02d:%02d:%02d", cur_time[0], cur_time[1], cur_time[2])
//                    }
//                    LOCK_SECOND -> {
//                        val second_angle = Math.atan2(event.y.toDouble()-496, event.x.toDouble()-496)
//                        clockV?.setAngle?.set(2, second_angle)
//                        val second = (((second_angle + 5 * Math.PI / 2) % (Math.PI * 2)) / (Math.PI / 30)).toInt()
//                        cur_time[2] = second
//                        String.format("%02d:%02d:%02d", cur_time[0], cur_time[1], cur_time[2])
//                    }
//                }
//                true
//            }
//            MotionEvent.ACTION_UP -> {
//                lock_state = NO_LOCK
//                true
//            }
//            MotionEvent.ACTION_CANCEL -> {
//                lock_state = NO_LOCK
//                true
//            }
//            MotionEvent.ACTION_OUTSIDE -> {
//                lock_state = NO_LOCK
//                true
//            }
//            else -> super.onTouchEvent(event)
//        }
//    }

    private fun getTimeWithOffset(date: LocalDateTime): Array<Int> {
        val now_second = 3600 * (date.hour + 8) + 60 * date.minute + date.second
        val new_second = now_second + second_offset
        val hour : Int = (new_second / 3600) % 24
        val minute : Int = (new_second / 60) % 60
        val second : Int = new_second % 60

        return arrayOf(hour, minute, second)
    }

    private fun setOffset(date : LocalDateTime) {
        val new_second = 3600 * cur_time[0] + 60 * cur_time[1] + cur_time[2]
        val now_second = 3600 * (date.hour + 8) + 60 * date.minute + date.second
        second_offset = (new_second + 24 * 3600 - now_second) % (24 * 3600)
    }

    inner class TimeThread : Thread() {

        override fun run() {
            super.run()
            while (true) {
                val date = LocalDateTime.now()
                if (!InChangeState) {
                    if (!time_set) {
                        setOffset(date)
                        time_set = true
                    }
                    val time_with_offset: Array<Int> = getTimeWithOffset(date)
                    val msg = Message.obtain()
                    msg.data = Bundle().apply {
                        putInt("hour", time_with_offset[0])
                        putInt("minute", time_with_offset[1])
                        putInt("second", time_with_offset[2])
                        putString(
                            "time",
                            String.format(
                                "%02d:%02d:%02d",
                                time_with_offset[0],
                                time_with_offset[1],
                                time_with_offset[2]
                            )
                        )
                    }
                    handler.sendMessage(msg)
                }
                sleep(1000)
            }
        }
    }
}