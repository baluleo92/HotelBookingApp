package com.hotel.bookingapp.model

data class Hotel(
    val id: String,
    val title: String,
    val address: String,
    val city: String,
    val country: String,
    val thumbnail: List<String>,
    val rating: String,
    val roomInfo: String,
    var roomsAvailable: Map<String, Int>? = null // roomTypeId to number mapping
)

