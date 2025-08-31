package com.hotel.bookingapp.model

data class RoomAvailability(
    val hotel_id: String,
    val room_type_id: String,
    val room_type_name: String,
    val noRoomsAvailable: Int
)
