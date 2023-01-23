package com.task.branch.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.task.branch.model.Message
import java.time.LocalDate
import java.time.Period
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Utils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getGroupedChats(messages : List<Message>) : List<ArrayList<Message>> {
        val threadIds = mutableListOf<Int>().apply {
            messages.forEach {
                if (!contains(it.threadId))
                    this.add(it.threadId!!)
            }
        }

        val chats = mutableListOf<ArrayList<Message>>()
        threadIds.forEach { threadId ->
            chats.add(messages.filter { threadId == it.threadId } as ArrayList<Message>)
        }

        chats.forEach {
            it.sortWith { o1, o2 ->
                ZonedDateTime.parse(o1.timestamp)!!.compareTo(
                    ZonedDateTime.parse(o2.timestamp!!)
                )
            }
        }
        chats.sortWith { o1, o2 ->
            ZonedDateTime.parse(o2.last().timestamp)!!.compareTo(
                ZonedDateTime.parse(o1.last().timestamp!!)
            )
        }
        return chats
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getParsedTimeStamp(timestamp : String) : String {
        ZonedDateTime.parse(timestamp).apply {
            val now = ZonedDateTime.now()
            return if (year == now.year && month == now.month && now.dayOfMonth - dayOfMonth <= 1)
                "Today"
            else if (this.plusDays(7) > now)
                this.dayOfWeek.toString()
            else
                "$year/$monthValue/$dayOfMonth"
        }
    }
}