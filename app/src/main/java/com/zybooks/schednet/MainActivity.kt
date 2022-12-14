package com.zybooks.schednet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.zybooks.schednet.utils.DatabaseManager

/**
 *  File: MainActivity.kt
 *  @author Matthew Clark
 */

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val attemptAlpha = DatabaseManager(this).readAutoLoggedUser()
        if(attemptAlpha > -1) {
            val intent = Intent(this, StageActivity::class.java)
            intent.putExtra(StageActivity.MAGIC_NUMBER, attemptAlpha)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.activity_main)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController = Navigation.findNavController(this, R.id.nav_graph_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}