package com.tcs.vehicletrackermaps.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tcs.vehicletrackermaps.ui.maps.MapsScreen // Import your MapsScreen Composable
import com.tcs.vehicletrackermaps.ui.registercar.AddCarScreen // Import your AddCarScreen Composable
import com.us.vehicletracker.ui.theme.VehicleTrackerTheme // Your application's theme

/**
 * Main Activity for the Vehicle Tracker application.
 * Hosts the Jetpack Compose navigation graph.
 */
class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge display for a modern look

        setContent {
            VehicleTrackerTheme { // Apply your custom theme defined in ui.theme
                val navController = rememberNavController() // Create and remember the NavController for navigation

                // Scaffold provides basic screen layout structure (like top bar, bottom bar, etc.)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // AppNavHost defines the navigation routes and associated Composables
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding) // Apply padding from Scaffold to content
                    )
                }
            }
        }
    }
}

/**
 * Defines the navigation graph for the application.
 *
 * @param navController The [NavHostController] to manage navigation state.
 * @param modifier Modifier to be applied to the [NavHost].
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController, // Link NavHost to the NavController
        startDestination = "dashboard", // The initial screen when the app launches
        modifier = modifier
    ) {
        // Route for the Fleet Dashboard screen
        composable("dashboard") {
            FleetDashboardScreen(
                // Lambdas for navigation actions triggered from the dashboard
                onViewMapClick = { navController.navigate("maps") }, // Navigate to MapsScreen
                onRegisterCarClick = { navController.navigate("registerCar") } // Navigate to AddCarScreen
            )
        }

        // Route for the Maps screen
        composable("maps") {
            MapsScreen() // Display the MapsScreen Composable
            // If MapsScreen needed to navigate back or to another screen, you'd pass a lambda here too.
        }

        // Route for the Add Car screen
        composable("registerCar") {
            AddCarScreen(
                // Lambda to handle completion of car registration, navigates back
                onCarRegistered = {
                    navController.popBackStack() // Pops the "registerCar" route off the stack, returning to dashboard
                    // Alternative: navController.navigate("dashboard") { popUpTo("dashboard") { inclusive = true } }
                }
            )
        }

        // Add more composable routes here as your application grows, e.g.:
        // composable("car_details/{vin}") { backStackEntry ->
        //     val vin = backStackEntry.arguments?.getString("vin")
        //     CarDetailsScreen(vin = vin ?: "")
        // }
    }
}