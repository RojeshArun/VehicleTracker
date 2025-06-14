package com.tcs.vehicletrackermaps.ui.maps

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


open class VehicleTrackerMaps : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }

    companion object {
        /**
         * Applies insets to the container view to properly handle window insets.
         *
         * @param container the container view to apply insets to
         */
        fun applyInsets(container: View) {
            ViewCompat.setOnApplyWindowInsetsListener(
                container,
                OnApplyWindowInsetsListener { view: View?, insets: WindowInsetsCompat? ->
                    val innerPadding =
                        insets!!.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
                    view!!.setPadding(
                        innerPadding.left,
                        innerPadding.top,
                        innerPadding.right,
                        innerPadding.bottom
                    )
                    insets
                }
            )
        }
    }
}