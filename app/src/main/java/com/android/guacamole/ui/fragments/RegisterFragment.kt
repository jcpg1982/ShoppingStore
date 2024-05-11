package com.android.guacamole.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.guacamole.R
import com.android.guacamole.data.UiState
import com.android.guacamole.data.enums.ActionTextWatcher
import com.android.guacamole.databinding.FragmentRegisterBinding
import com.android.guacamole.ui.interfaces.TextWatcherListener
import com.android.guacamole.ui.viewmodels.RegisterViewModel
import com.android.guacamole.utils.Utils.generateUnique
import com.android.guacamole.utils.Utils.hideKeyboard
import com.android.guacamole.utils.Validators.areContentsTheSame
import com.android.guacamole.utils.Validators.validateNotEmpty
import com.android.guacamole.utils.Validators.validateSize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val registerViewModel: RegisterViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    private val isBottomEnabled: Boolean
        get() {
            var validate = false
            if (binding?.txtName?.validateNotEmpty == true)
                if (binding?.txtLastName?.validateNotEmpty == true)
                    if (binding?.txtUser?.validateNotEmpty == true)
                        if (binding?.txtPassword?.validateSize(6) == true)
                            if (binding?.txtRePassword?.validateSize(6) == true)
                                if (binding?.txtRePassword?.areContentsTheSame(binding?.inputPassword?.text.toString()) == true)
                                    validate = true
            return validate
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
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
            registerViewModel.registerUser.collect {
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
                        findNavController().navigate(R.id.registerToWelcome)
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
            inputName.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher, charSequence: CharSequence?, start: Int,
                    count: Int, afterOrBefore: Int
                ) {
                    when (action) {
                        ActionTextWatcher.ON_TEXT_CHANGED -> {
                            btnAccept.isEnabled = isBottomEnabled
                        }

                        else -> {}
                    }
                }
            })
            inputLastName.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher, charSequence: CharSequence?, start: Int,
                    count: Int, afterOrBefore: Int
                ) {
                    when (action) {
                        ActionTextWatcher.ON_TEXT_CHANGED -> {
                            btnAccept.isEnabled = isBottomEnabled
                        }

                        else -> {}
                    }
                }
            })
            inputUser.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher, charSequence: CharSequence?, start: Int,
                    count: Int, afterOrBefore: Int
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
                    action: ActionTextWatcher, charSequence: CharSequence?, start: Int,
                    count: Int, afterOrBefore: Int
                ) {
                    when (action) {
                        ActionTextWatcher.ON_TEXT_CHANGED -> {
                            btnAccept.isEnabled = isBottomEnabled
                        }

                        else -> {}
                    }
                }
            })
            inputRePassword.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher, charSequence: CharSequence?, start: Int,
                    count: Int, afterOrBefore: Int
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
                findNavController().navigate(R.id.registerToWelcome)
            }
            btnAccept.setOnClickListener {
                registerViewModel.user = registerViewModel.user.copy(
                    userId = generateUnique,
                    name = inputName.text.toString(),
                    lastName = inputLastName.text.toString(),
                    user = inputUser.text.toString(),
                    password = inputPassword.text.toString()
                )
                registerViewModel.registerUser(registerViewModel.user)
            }
        }
    }
}