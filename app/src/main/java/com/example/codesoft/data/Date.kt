package com.example.codesoft.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return dateFormat.format(date)
}
fun formatDayOfWeek(date: Date): String {
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return dayFormat.format(date)
}


fun formatday(date: Date): String {
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
    return dateFormat.format(date)
}
fun formatTime(date: Date): String {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return timeFormat.format(date)
}