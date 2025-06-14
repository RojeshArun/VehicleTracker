package com.tcs.vehicletrackermaps.ui.registercar

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions // Import for KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType // Import for KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete // Import for Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity // Import for AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tcs.vehicletrackermaps.data.Car // Ensure this import path is correct for your Car data class
import com.us.vehicletracker.ui.theme.VehicleTrackerTheme // Your app's theme for preview
import java.util.*

/**
 * Composable function for the Add Car screen.
 * Allows users to input car details and register a new car,
 * including Google Places Autocomplete for location.
 *
 * @param onCarRegistered Lambda to be invoked upon successful car registration,
 * typically used to navigate back.
 */
@Composable
fun AddCarScreen(
    onCarRegistered: () -> Unit
) {
    val context = LocalContext.current
    // Obtains a ViewModel instance for this Composable.
    // Ensure AddCarViewModel is correctly set up for ViewModelProvider or Hilt.
    val viewModel: AddCarViewModel = viewModel()

    // State variables for input fields
    var vin by remember { mutableStateOf("") }
    var make by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var owner by remember { mutableStateOf("") } // This field represents the direct location/owner input
    var locationSearchText by remember { mutableStateOf("") } // Text displayed from Places Autocomplete

    // State variables for validation errors
    var vinError by remember { mutableStateOf<String?>(null) }
    var makeError by remember { mutableStateOf<String?>(null) }
    var modelError by remember { mutableStateOf<String?>(null) }
    var plateError by remember { mutableStateOf<String?>(null) }
    var ownerError by remember { mutableStateOf<String?>(null) }

    // Initialize Google Places API.
    // It's best practice to initialize this once in your Application class or a splash screen
    // to avoid re-initialization on every recomposition.
    // Replace "YOUR_GOOGLE_API_KEY" with your actual API key, ideally read from BuildConfig.
    if (!Places.isInitialized()) {
        Places.initialize(context.applicationContext, "YOUR_GOOGLE_API_KEY", Locale.US) // Use BuildConfig.Maps_API_KEY
    }


    // ActivityResultLauncher to handle the result from Google Places Autocomplete Activity
    val placesAutocompleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                // Get the selected place from the intent
                val place = Autocomplete.getPlaceFromIntent(result.data!!)
                locationSearchText = place.name ?: "" // Update display text
                // You might want to also update the 'owner' state with place.name or another relevant field,
                // depending on how you want to use the selected location in your Car object.
            }
            AutocompleteActivity.RESULT_ERROR -> { // Correct import: AutocompleteActivity.RESULT_ERROR
                val status = Autocomplete.getStatusFromIntent(result.data!!)
                Toast.makeText(context, "Error: ${status.statusMessage}", Toast.LENGTH_SHORT).show()
            }
            Activity.RESULT_CANCELED -> {
                // User explicitly canceled the autocomplete flow
                Toast.makeText(context, "Location search cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Lambda to launch the Google Places Autocomplete Intent
    val launchPlacesAutocomplete = {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(context)
        placesAutocompleteLauncher.launch(intent)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8)) // Light background for the screen
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
                .padding(horizontal = 20.dp, vertical = 16.dp) // Overall screen padding
                .verticalScroll(rememberScrollState()), // Make the content scrollable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp) // Spacing between input fields
        ) {
            Spacer(modifier = Modifier.height(60.dp)) // Top padding

            Text(
                text = "Register New Car",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // VIN Input Field
            LabelledTextField(
                label = "VIN",
                value = vin,
                onValueChange = {
                    vin = it
                    vinError = validateVin(it) // Validate as user types
                },
                placeholder = "Enter 17-character VIN",
                isError = vinError != null,
                errorMessage = vinError,
                keyboardType = KeyboardType.Text // VIN contains letters and numbers
            )

            // Make Input Field
            LabelledTextField(
                label = "Make",
                value = make,
                onValueChange = {
                    make = it
                    makeError = validateMake(it)
                },
                placeholder = "e.g., Toyota, Ford",
                isError = makeError != null,
                errorMessage = makeError
            )

            // Model Input Field
            LabelledTextField(
                label = "Model",
                value = model,
                onValueChange = {
                    model = it
                    modelError = validateModel(it)
                },
                placeholder = "e.g., Camry, F-150",
                isError = modelError != null,
                errorMessage = modelError
            )

            // Number Plate Input Field
            LabelledTextField(
                label = "Number Plate",
                value = plate,
                onValueChange = {
                    plate = it
                    plateError = validatePlate(it)
                },
                placeholder = "e.g., KA01AB1234 or ABC-1234",
                isError = plateError != null,
                errorMessage = plateError
            )

            // Direct Location/Owner Input Field (used for the Car.owner field)
            LabelledTextField(
                label = "Current Location", // Or "Owner Name" if 'owner' refers to person
                value = owner,
                onValueChange = {
                    owner = it
                    ownerError = validateOwner(it)
                },
                placeholder = "e.g., City, State",
                isError = ownerError != null,
                errorMessage = ownerError
            )

            // Google Places Search Location Field (read-only, triggers autocomplete)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Search Via Google Places (Optional)",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = locationSearchText,
                    onValueChange = { /* Read-only, do nothing here */ },
                    readOnly = true, // User cannot type directly into this field
                    label = { Text("Click to search for a precise location") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { launchPlacesAutocomplete() }, // Launches the Places Autocomplete UI
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007BFF),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }


            // Register Button
            Button(
                onClick = {
                    // Trigger full validation on button click
                    vinError = validateVin(vin)
                    makeError = validateMake(make)
                    modelError = validateModel(model)
                    plateError = validatePlate(plate)
                    ownerError = validateOwner(owner) // Validate the direct 'owner' field

                    val isFormValid = vinError == null && makeError == null &&
                            modelError == null && plateError == null && ownerError == null

                    if (isFormValid) {
                        // Create Car object (consider how to use locationSearchText if it's the actual location)
                        val car = Car(
                            vin = vin,
                            make = make,
                            model = model,
                            plate = plate,
                            owner = owner // Using the direct input for 'owner'
                            // If you want to store the Google Places result as the car's location:
                            // location = locationSearchText // Add a 'location' field to your Car data class
                        )
                        viewModel.addCar(car) // Call ViewModel to save the car
                        Toast.makeText(context, "Car Registered Successfully!", Toast.LENGTH_SHORT).show()

                        // Clear input fields after successful registration
                        vin = ""
                        make = ""
                        model = ""
                        plate = ""
                        owner = ""
                        locationSearchText = "" // Clear Places search text

                        onCarRegistered() // Invoke the lambda to navigate back
                    } else {
                        Toast.makeText(context, "Please correct the errors before registering.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F42C1)), // Purple button
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(text = "Register Car", color = Color.White)
            }

            // Optional: Autofill Button for Development/Testing
            Button(
                onClick = {
                    val randomCar = generateRandomCar()
                    vin = randomCar.vin
                    make = randomCar.make
                    model = randomCar.model
                    plate = randomCar.plate
                    owner = randomCar.owner
                    locationSearchText = randomCar.owner // Or some default for Places field
                    // Clear errors when autofilling
                    vinError = null
                    makeError = null
                    modelError = null
                    plateError = null
                    ownerError = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                Text(text = "Autofill Random Data", color = Color.Black)
            }
        }
    }
}

/**
 * Reusable Composable for a labeled text input field with error display.
 *
 * @param label The label text displayed above the input field.
 * @param value The current text value of the input field.
 * @param onValueChange Callback to be invoked when the value changes.
 * @param placeholder The placeholder text inside the input field.
 * @param modifier Modifier to be applied to the Column containing the text field.
 * @param isError Boolean indicating if there is a validation error.
 * @param errorMessage Optional error message to display below the field.
 * @param keyboardType The type of keyboard to display (e.g., Text, Number).
 */
@Composable
fun LabelledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(placeholder) },
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF007BFF),
                unfocusedBorderColor = Color.LightGray,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

// --- Validation Functions (Top-level for reusability) ---

private fun validateVin(vin: String): String? {
    val vinText = vin.trim()
    return when {
        vinText.isEmpty() -> "VIN is required."
        vinText.length != 17 || !vinText.matches(Regex("^[A-HJ-NPR-Z0-9]{17}$")) -> "VIN must be 17 characters (no I, O, Q)."
        else -> null
    }
}

private fun validateMake(make: String): String? {
    val makeText = make.trim()
    return when {
        makeText.isEmpty() -> "Make is required."
        !makeText.matches(Regex("^[A-Za-z\\s]{2,}$")) -> "Make must be alphabetic with at least 2 characters." // Allow spaces
        else -> null
    }
}

private fun validateModel(model: String): String? {
    val modelText = model.trim()
    return when {
        modelText.isEmpty() -> "Model is required."
        !modelText.matches(Regex("^[A-Za-z0-9\\-\\s]+$")) -> "Model can only contain letters, numbers, hyphens, and spaces."
        else -> null
    }
}

private fun validatePlate(plate: String): String? {
    val plateText = plate.trim().uppercase(Locale.getDefault()) // Normalize to uppercase
    return when {
        plateText.isEmpty() -> "Number plate is required."
        // Example Indian (KA01AB1234) or US (ABC-1234) like format
        !plateText.matches(Regex("^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$")) &&
                !plateText.matches(Regex("^[A-Z]{3}-[0-9]{4}$")) &&
                !plateText.matches(Regex("^[0-9]{2}[A-Z]{1,2}[0-9]{2,4}$")) // Example for states where numbers come first
            -> "Invalid plate format (e.g., KA01AB1234, ABC-1234, or 12AB3456)."
        else -> null
    }
}

private fun validateOwner(owner: String): String? {
    val ownerText = owner.trim()
    return when {
        ownerText.isEmpty() -> "Location/Owner name is required."
        !ownerText.matches(Regex("^[A-Za-z\\s.']{2,50}$")) -> "Location/Owner must be alphabetic and 2–50 characters long (can contain spaces, dots, apostrophes)."
        else -> null
    }
}

// --- Random Data Generation Functions (Top-level for reusability) ---

private fun generateRandomCar(): Car {
    val makes = listOf("Toyota", "Honda", "Ford", "BMW", "Hyundai", "Suzuki", "Kia", "Tesla", "Mercedes-Benz")
    val models = listOf("Corolla", "Civic", "Mustang", "320i", "i20", "Swift", "Seltos", "Model 3", "C-Class", "Sonata")
    val cities = listOf("Mumbai", "Delhi", "Bangalore", "Chennai", "Hyderabad", "Pune", "Kolkata", "Ahmedabad", "Surat", "Jaipur", "Lucknow", "Nagoya", "Tokyo", "Osaka")

    val vin = (1..17).map {
        val chars = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789"
        chars.random()
    }.joinToString("")

    val make = makes.random()
    val model = models.random()
    val plate = generateRandomPlate()
    val owner = cities.random()

    return Car(vin, make, model, plate, owner)
}

private fun generateRandomPlate(): String {
    val letters = ('A'..'Z').toList()
    val digits = ('0'..'9').toList()

    val formatChoice = (0..2).random() // Choose a random plate format

    return when (formatChoice) {
        0 -> { // e.g., KA01AB1234 (Indian style)
            val stateCode = "${letters.random()}${letters.random()}"
            val districtCode = "${digits.random()}${digits.random()}"
            val series = "${letters.random()}${letters.random()}"
            val number = (1000..9999).random()
            "$stateCode$districtCode$series$number"
        }
        1 -> { // e.g., ABC-1234 (US style)
            val prefix = "${letters.random()}${letters.random()}${letters.random()}"
            val number = (1000..9999).random()
            "$prefix-$number"
        }
        else -> { // e.g., 12AB3456 (another generic style)
            val firstDigits = "${digits.random()}${digits.random()}"
            val midLetters = "${letters.random()}${letters.random()}"
            val lastDigits = (1000..9999).random()
            "$firstDigits$midLetters$lastDigits"
        }
    }
}


@Preview(showBackground = true, widthDp = 360)
@Composable
fun AddCarScreenPreview() {
    VehicleTrackerTheme {
        AddCarScreen(onCarRegistered = {})
    }
}