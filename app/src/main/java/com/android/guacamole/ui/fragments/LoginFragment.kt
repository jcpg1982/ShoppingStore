package com.android.guacamole.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.guacamole.R
import com.android.guacamole.data.UiState
import com.android.guacamole.data.enums.ActionTextWatcher
import com.android.guacamole.databinding.FragmentLoginBinding
import com.android.guacamole.ui.interfaces.TextWatcherListener
import com.android.guacamole.ui.viewmodels.CartViewModel
import com.android.guacamole.ui.viewmodels.LoginViewModel
import com.android.guacamole.utils.Utils.generateUnique
import com.android.guacamole.utils.Utils.hideKeyboard
import com.android.guacamole.utils.Utils.readJsonFromRaw
import com.android.guacamole.utils.Validators.areContentsTheSame
import com.android.guacamole.utils.Validators.validateNotEmpty
import com.android.guacamole.utils.Validators.validateSize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val loginViewModel: LoginViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    private val isBottomEnabled: Boolean
        get() {
            var validate = false
            if (binding?.txtUser?.validateNotEmpty == true) if (binding?.txtPassword?.validateNotEmpty == true) validate =
                true
            return validate
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
        initWatcher()
        setOnClick()
    }

    override fun onResume() {
        super.onResume()
        this.hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {

        lifecycleScope.launch {
            loginViewModel.loginUser.collect {
                when (it) {
                    is UiState.Loading -> {
                        showLoading(it.isLoading, it.message)
                    }

                    is UiState.NotSuccess -> {
                        showLoading(false, "")
                        showError(true, it.throwable.message.orEmpty())
                    }

                    is UiState.Success -> {
                        showLoading(false, "")
                        if (it.data != null) {
                            cartViewModel.user = it.data
                            findNavController().navigate(R.id.loginToHome)
                        } else showError(true, "Usuario no existe o datos incorrectos")
                    }
                }
            }
        }
    }

    private fun initEvents() {
        binding?.run {
            btnAccept.isEnabled = isBottomEnabled
        }
    }

    private fun initWatcher() {
        binding?.run {
            inputUser.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher,
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    afterOrBefore: Int
                ) {
                    when (action) {
                        ActionTextWatcher.ON_TEXT_CHANGED -> {
                            btnAccept.isEnabled = isBottomEnabled
                        }

                        else -> {}
                    }
                }
            })
            inputPassword.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher,
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    afterOrBefore: Int
                ) {
                    when (action) {
                        ActionTextWatcher.ON_TEXT_CHANGED -> {
                            btnAccept.isEnabled = isBottomEnabled
                        }

                        else -> {}
                    }
                }
            })
        }
    }

    private fun setOnClick() {
        binding?.run {
            ivBack.setOnClickListener {
                findNavController().navigate(R.id.loginToWelcome)
            }
            btnAccept.setOnClickListener {
                loginViewModel.loginUser(inputUser.text.toString(), inputPassword.text.toString())
            }
        }
    }
}