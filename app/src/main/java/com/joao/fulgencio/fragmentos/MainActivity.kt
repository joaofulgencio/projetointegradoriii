package com.joao.fulgencio.fragmentos

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.joao.fulgencio.fragmentos.databinding.ActivityMainBinding

private const val SESSION_PREF = "session_pref"
private const val LOGGED_IN = "logged_in"
private const val LOGIN_TIME = "login_time"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        sharedPreferences = getSharedPreferences(SESSION_PREF, Context.MODE_PRIVATE)
        initToolbar()
    }

    private fun initToolbar() {
        val navHostFragment = binding.navHostFragment.getFragment<NavHostFragment>()
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navigateUp = navController.navigateUp()
        Log.d("NavController", "Navigate up: $navigateUp")
        return navigateUp || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // Infla o arquivo menu_main.xml
        return true
    }

    // Gerencia as ações de clique nos itens do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                Log.d("MainActivity", "Logout action clicked")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        // Limpa os dados do SharedPreferences
        with(sharedPreferences.edit()) {
            remove(LOGGED_IN)
            remove(LOGIN_TIME)
            apply()
        }

        // Volta ao fragmento de login (FirstFragment)
        val navHostFragment = binding.navHostFragment.getFragment<NavHostFragment>()
        navController = navHostFragment.navController
        navController.navigate(R.id.action_global_firstFragment)

        Log.d("MainActivity", "Logout action completed")
    }

}