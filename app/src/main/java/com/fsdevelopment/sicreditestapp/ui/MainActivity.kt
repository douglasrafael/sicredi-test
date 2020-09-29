package com.fsdevelopment.sicreditestapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.fsdevelopment.sicreditestapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.navHostContainer)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailScreen, R.id.checkInDialog -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE
            }
        }

        bottomNavigationView.setupWithNavController(navController)
    }
}
