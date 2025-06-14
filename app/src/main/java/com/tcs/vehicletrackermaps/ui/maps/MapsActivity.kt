package com.tcs.vehicletrackermaps.ui.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.us.vehicletracker.ui.theme.VehicleTrackerTheme // Your app's theme for preview

/**
 * Composable function for displaying a Google Map with markers.
 * This screen is intended to be part of the Compose Navigation in HomeScreen.
 */
@Composable
fun MapsScreen() {
    // Example list of places/locations to display on the map
    val places = listOf(
        "Brisbane" to LatLng(-27.47093, 153.0235),
        "Sydney" to LatLng(-33.87365, 151.20689),
        "Melbourne" to LatLng(-37.81319, 144.96298),
        "Adelaide" to LatLng(-34.92873, 138.59995),
        "Perth" to LatLng(-31.95285, 115.85734)
    )

    // Remember the camera position state for the map.
    // This allows you to programmatically control the map's camera.
    val cameraPositionState = rememberCameraPositionState {
        // Initial camera position centered on the first place in the list with a moderate zoom level.
        position = CameraPosition.fromLatLngZoom(places.first().second, 4f)
    }

    // GoogleMap Composable from the maps-compose library.
    GoogleMap(
        modifier = Modifier.fillMaxSize(), // Makes the map fill the entire available screen space.
        cameraPositionState = cameraPositionState, // Binds the map's camera to our state.
        properties = MapProperties(mapType = MapType.NORMAL), // Sets the map's visual properties (e.g., normal, satellite, terrain).
        uiSettings = MapUiSettings(zoomControlsEnabled = false) // Configures UI controls (e.g., disable default zoom buttons).
    ) {
        // Iterate through the list of places to add a MarkerInfoWindow for each.
        places.forEach { (title, latLng) ->
            MarkerInfoWindow(
                state = MarkerState(position = latLng), // Sets the geographical position of the marker.
                title = title, // The main title text for the info window when the marker is tapped.
                snippet = "Population: TBD", // A smaller descriptive text in the info window.
                // Custom icon for the marker. Using a default marker with an azure hue.
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            ) { markerState ->
                // Custom Composable content for the Marker's Info Window.
                // This will be displayed when the user taps on the marker.
                Column(
                    // Apply background with a vertical gradient, rounded corners, and slight transparency.
                    Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White,    // Top color of the gradient
                                    Color.LightGray // Bottom color of the gradient
                                )
                            ),
                            shape = RoundedCornerShape(8.dp), // Rounded corners for the info window background.
                            alpha = 0.9f // Sets the opacity of the background (0.0f is fully transparent, 1.0f is fully opaque).
                        )
                        .padding(8.dp) // Inner padding within the info window content.
                ) {
                    // Display the marker's title. Use Elvis operator (?:) for null safety.
                    Text(
                        markerState.title ?: "Unknown Title",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                    // Display the marker's snippet.
                    Text(
                        markerState.snippet ?: "No snippet available",
                        color = Color.Magenta
                    )
                }
            }
        }
    }
}

/**
 * Preview for the MapsScreen Composable, allowing visual inspection in Android Studio.
 * Uses the VehicleTrackerTheme for consistent styling in the preview.
 */
@Preview(showBackground = true, widthDp = 360, heightDp = 640) // Define preview size
@Composable
fun MapsScreenPreview() {
    VehicleTrackerTheme {
        MapsScreen()
    }
}
