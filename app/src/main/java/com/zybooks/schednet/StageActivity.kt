package com.zybooks.schednet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zybooks.schednet.databinding.ActivityStageBinding

class StageActivity: AppCompatActivity() {

    private lateinit var binding: ActivityStageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottom: BottomNavigationView = binding.navView
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_graph_stage_fragment) as NavHostFragment

        //window.navigationBarColor(resources.getColor(R.color.red_menu))

        val navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottom, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController = Navigation.findNavController(this, R.id.nav_graph_stage_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        //Uncomment to allow returning to login
        //NOTE: Can call "onBackPressed()" from settings to return to login
        //super.onBackPressed()
        onSupportNavigateUp()
    }
}