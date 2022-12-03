package com.zybooks.schednet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: StageActivity.kt
 *  @author Matthew Clark
 */

class StageActivity: AppCompatActivity() {



    companion object {
        const val MAGIC_NUMBER = "login.ky"
    }

    private var mNumber: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage)

        mNumber = intent.extras?.getInt(MAGIC_NUMBER)!!
        if(mNumber > 0) {
            DatabaseManager(this).resetCalendarDate(mNumber)
        }

        val bottom: BottomNavigationView = findViewById(R.id.nav_view)
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_graph_stage_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph_stage)
        NavigationUI.setupWithNavController(bottom, navController)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MAGIC_NUMBER, mNumber)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController = Navigation.findNavController(this, R.id.nav_graph_stage_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        onSupportNavigateUp()
    }
}