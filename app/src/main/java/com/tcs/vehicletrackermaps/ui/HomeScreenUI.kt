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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FleetDashboardScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF4F6F8))
    ) {
        // Real-Time Fleet Map Button
        Button(
            onClick = { /* Navigate to Map */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
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

                // Grid Items (2x2)
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

        // Control Panel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Start */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745))
            ) {
                Text("Start", color = Color.White)
            }
            Button(
                onClick = { /* Stop */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC3545))
            ) {
                Text("Stop", color = Color.White)
            }
            Button(
                onClick = { /* Optimize */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
            ) {
                Text("Optimize", color = Color.Black)
            }
        }

        // Register New Car Button
        Button(
            onClick = { /* Register New Car */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F42C1))
        ) {
            Text(text = "Register New Car", color = Color.White)
        }
    }
}

@Composable
fun FleetInfoItem(icon: ImageVector, iconTint: Color, label: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
        Text(text = label, fontSize = 16.sp, color = Color(0xFF333333))
    }
}
