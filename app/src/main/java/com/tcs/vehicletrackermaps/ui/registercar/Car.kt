package com.tcs.vehicletrackermaps.data

/**
 * Data class representing a Car in the application.
 * Adjust fields as necessary for your specific car attributes.
 */
data class Car(
    val vin: String,
    val make: String,
    val model: String,
    val plate: String,
    val owner: String // Represents the owner or initial location
    // You can add more fields like:
    // val lat: Double? = null,
    // val lng: Double? = null,
    // val registrationDate: Long? = null
)