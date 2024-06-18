package com.juniorandrerosa.smartbuddy

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val duration: Double,
    val time: String,
    val owner: String
)
