package com.hotel.bookingapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hotel.bookingapp.viewmodel.HotelViewModel

class HotelViewModelFactory(
    private val repository: HotelRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HotelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HotelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}