package com.android.guacamole.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.guacamole.ui.dialogs.Alerts
import com.android.guacamole.ui.dialogs.Loading

abstract class BaseActivity : AppCompatActivity() {

    lateinit var loaging: Loading
    lateinit var alert: Alerts
    abstract fun initDialogs()
    abstract fun showLoading(shown: Boolean, message: String)
    abstract fun showError(shown: Boolean, message: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDialogs()
    }
}