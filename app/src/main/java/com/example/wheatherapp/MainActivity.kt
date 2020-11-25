package com.example.wheatherapp

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        drawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolBar, R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.nav_menu_white)

        bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.navigation_today_weather -> {

                }

                R.id.navigation_weekly_weather -> {

                }

                R.id.navigation_share_weather -> {

                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // add item to action bar
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu)
    }

}