package com.tcs.vehicletrackermaps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.us.vehicletracker.ui.theme.VehicleTrackerTheme // Your app's theme for preview

/**
 * Composable function for the Fleet Dashboard screen.
 * Displays fleet summary, control buttons, and navigation options.
 *
 * @param modifier Modifier to be applied to the dashboard layout.
 * @param onViewMapClick Lambda to be invoked when "View Real-Time Fleet Map" button is clicked.
 * @param onRegisterCarClick Lambda to be invoked when "Register New Car" button is clicked.
 */
@Composable
fun FleetDashboardScreen(
    modifier: Modifier = Modifier,
    onViewMapClick: () -> Unit, // Callback for navigating to the map screen
    onRegisterCarClick: () -> Unit // Callback for navigating to the car registration screen
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF4F6F8)) // Light background color
    ) {
        // Real-Time Fleet Map Button
        Button(
            onClick = onViewMapClick, // Now calls the lambda for map navigation
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)) // Blue button
        ) {
            Text(text = "View Real-Time Fleet Map", color = Color.White)
        }

        // Fleet Summary Card
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Fleet Summary",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Grid Items (2x2 layout for fleet stats)
                Column {
                    Row(Modifier.fillMaxWidth()) {
                        FleetInfoItem(
                            icon = Icons.Default.Place,
                            iconTint = Color(0xFF007BFF),
                            label = "Total: 4",
                            modifier = Modifier.weight(1f)
                        )
                        FleetInfoItem(
                            icon = Icons.Default.Explore,
                            iconTint = Color(0xFF17A2B8),
                            label = "Avg Speed: 22.5",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(Modifier.fillMaxWidth()) {
                        FleetInfoItem(
                            icon = Icons.Default.BatteryChargingFull,
                            iconTint = Color(0xFF28A745),
                            label = "Battery: 76%",
                            modifier = Modifier.weight(1f)
                        )
                        FleetInfoItem(
                            icon = Icons.Default.Warning,
                            iconTint = Color(0xFFDC3545),
                            label = "Alerts: 0",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Control Panel Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween // Distribute buttons evenly
        ) {
            Button(
                onClick = onViewMapClick, // FIXED: Now calls the lambda for map navigation
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745)) // Green
            ) {
                Text("Start", color = Color.White)
            }
            Button(
                onClick = { /* Handle Stop Action */ }, // Placeholder for actual stop logic
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC3545)) // Red
            ) {
                Text("Stop", color = Color.White)
            }
            Button(
                onClick = { /* Handle Optimize Action */ }, // Placeholder for actual optimize logic
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)) // Yellow
            ) {
                Text("Optimize", color = Color.Black)
            }
        }

        // Register New Car Button
        Button(
            onClick = onRegisterCarClick, // Now calls the lambda for car registration navigation
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F42C1)) // Purple
        ) {
            Text(text = "Register New Car", color = Color.White)
        }
    }
}

/**
 * Helper Composable to display a single fleet information item (icon + label).
 *
 * @param icon The [ImageVector] for the icon.
 * @param iconTint The color to tint the icon.
 * @param label The text label for the item.
 * @param modifier Modifier to be applied to the item layout.
 */
@Composable
fun FleetInfoItem(icon: ImageVector, iconTint: Color, label: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null, // Content description for accessibility
            tint = iconTint,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
        Text(text = label, fontSize = 16.sp, color = Color(0xFF333333))
    }
}

@Preview(showBackground = true)
@Composable
fun FleetDashboardScreenPreview() {
    VehicleTrackerTheme {
        FleetDashboardScreen(
            onViewMapClick = {}, // Provide empty lambdas for preview
            onRegisterCarClick = {} // Provide empty lambdas for preview
        )
    }
}
