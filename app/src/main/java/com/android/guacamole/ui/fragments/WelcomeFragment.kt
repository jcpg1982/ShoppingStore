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
import com.android.guacamole.data.models.DataJson
import com.android.guacamole.databinding.FragmentWelcomeBinding
import com.android.guacamole.ui.viewmodels.WelcomeViewModel
import com.android.guacamole.utils.Utils.hideKeyboard
import com.android.guacamole.utils.Utils.readJsonFromRaw
import com.android.guacamole.utils.Utils.stringToObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class WelcomeFragment : BaseFragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val welcomeViewModel: WelcomeViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
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
            welcomeViewModel.saveData.collect {
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
                    }
                }
            }
        }
    }

    private fun initEvents() {
        val json = readJsonFromRaw(requireContext(), R.raw.json_products)
        val datos = json.stringToObject(DataJson::class.java)
        if (datos != null) welcomeViewModel.saveData(datos)
    }

    private fun setOnClick() {
        binding?.run {
            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.welcomeToLogin)
            }
            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.welcomeToRegister)
            }
        }
    }
}