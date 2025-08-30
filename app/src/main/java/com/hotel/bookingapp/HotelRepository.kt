package com.hotel.bookingapp


import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hotel.bookingapp.model.Hotel
import com.hotel.bookingapp.model.RoomAvailability


class HotelRepository(private val context: Context) {

    private fun readAsset(fileName: String): String =
        context.assets.open(fileName).bufferedReader().use { it.readText() }

    fun getHotels(): List<Hotel> {
        val json = readAsset("ListOfHotel.json")
        val jsonObject = Gson().fromJson(json, Map::class.java)
        val itemsJson = Gson().toJson(jsonObject["items"])
        val type = object : TypeToken<List<Hotel>>() {}.type
        return Gson().fromJson(itemsJson, type)
    }

    fun getRoomAvailabilities(): List<RoomAvailability> {
        val json = readAsset("NumberOfRoomAvailability.json")
        val jsonObject = Gson().fromJson(json, Map::class.java)
        val itemsJson = Gson().toJson(jsonObject["items"])
        val type = object : TypeToken<List<RoomAvailability>>() {}.type
        return Gson().fromJson(itemsJson, type)
    }
}