package com.android.guacamole.ui.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.android.guacamole.ui.activities.BaseActivity
import java.lang.RuntimeException

abstract class BaseFragment : Fragment() {

    private var baseActivity: BaseActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = when (context) {
            is BaseActivity -> context
            else -> throw RuntimeException(context.toString())
        }
    }

    fun showLoading(shown: Boolean, message: String) {
        baseActivity?.showLoading(shown, message)
    }

    fun showError(shown: Boolean, message: String) {
        baseActivity?.showError(shown, message)
    }

}