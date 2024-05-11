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
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.guacamole.data.UiState
import com.android.guacamole.databinding.FragmentFavoriteBinding
import com.android.guacamole.ui.adapters.FavoriteAdapter
import com.android.guacamole.ui.adapters.HomeAdapter
import com.android.guacamole.ui.viewmodels.CartViewModel
import com.android.guacamole.ui.viewmodels.FavoriteViewModel
import com.android.guacamole.utils.Utils.formatQuantity
import com.android.guacamole.utils.Utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FavoriteFragment : BaseFragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    private var favoriteAdapter: FavoriteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setOnClick()
        cartViewModel.user?.userId?.let { favoriteViewModel.obtainResume(it) }
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
            favoriteViewModel.obtainResume.collect {
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
                        Log.e("TAG", "Success: ${it.data}")
                        binding?.run {
                            txtTotalBuy.text = it.data.totalVentas.formatQuantity
                            txtBuyCash.text = it.data.ventasEfectivo.formatQuantity
                            txtBuyCard.text = it.data.ventasTarjeta.formatQuantity
                            favoriteAdapter?.dataList = it.data.productos
                            favoriteAdapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private fun setOnClick() {
        binding?.run {
            ivBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupRecycler() {
        favoriteAdapter = FavoriteAdapter(listOf())
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.recycler?.setHasFixedSize(true)
        binding?.recycler?.layoutManager = layoutManager
        binding?.recycler?.adapter = favoriteAdapter
    }
}