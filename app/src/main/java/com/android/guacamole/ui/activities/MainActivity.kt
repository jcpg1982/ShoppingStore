package com.android.guacamole.ui.activities

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.android.guacamole.R
import com.android.guacamole.databinding.ActivityMainBinding
import com.android.guacamole.ui.dialogs.Alerts
import com.android.guacamole.ui.dialogs.Loading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun initDialogs() {
        loaging = Loading(this)
        alert = Alerts(this)
    }

    override fun showLoading(shown: Boolean, message: String) {
        loaging.showDialog(shown, message)
    }

    override fun showError(shown: Boolean, message: String) {
        alert.showDialog(shown, message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}