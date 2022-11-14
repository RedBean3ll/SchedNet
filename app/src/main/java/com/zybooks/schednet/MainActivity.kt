package com.zybooks.schednet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController = Navigation.findNavController(this, R.id.nav_graph_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}