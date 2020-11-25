package com.example.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.weatherapp.dagger.ViewModelFactory
import com.example.weatherapp.tabs.ShareWeatherFragment
import com.example.weatherapp.tabs.TodayWeatherFragment
import com.example.weatherapp.tabs.WeeklyWeatherFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

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

        // load this fragment on launch
        loadFragment(TodayWeatherFragment())

        bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.navigation_today_weather -> {
                    loadFragment(TodayWeatherFragment())
                }

                R.id.navigation_weekly_weather -> {
                    loadFragment(WeeklyWeatherFragment())
                }

                R.id.navigation_share_weather -> {
                    loadFragment(ShareWeatherFragment())
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            this.replace(R.id.frame_container, fragment)
            this.addToBackStack(null)
            this.commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // add item to action bar
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}