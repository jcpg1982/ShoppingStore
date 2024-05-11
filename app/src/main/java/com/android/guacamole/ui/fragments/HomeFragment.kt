package com.android.guacamole.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.guacamole.R
import com.android.guacamole.data.UiState
import com.android.guacamole.data.models.CategoryWithProduct
import com.android.guacamole.databinding.FragmentHomeBinding
import com.android.guacamole.ui.adapters.HomeAdapter
import com.android.guacamole.ui.viewmodels.CartViewModel
import com.android.guacamole.ui.viewmodels.HomeViewModel
import com.android.guacamole.utils.Utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    private var homeAdapter: HomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setOnClick()
        homeViewModel.getProducts()
    }

    override fun onResume() {
        super.onResume()
        initCartObserver()
        this.hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        lifecycleScope.launch {
            homeViewModel.getProducts.collect {
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
                        val groupedByCategory = it.data.groupBy { it.category }
                        homeAdapter?.dataList = groupedByCategory.map { (category, listProducts) ->
                            CategoryWithProduct(category, listProducts)
                        }
                        homeAdapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun initCartObserver() {
        lifecycleScope.launch {
            cartViewModel.sizeProductsAdded.collect {
                if (it > 0) binding?.containerCountProducts?.visibility = View.VISIBLE
                else binding?.containerCountProducts?.visibility = View.GONE
                binding?.tvCountProductsAdd?.text = it.toString()
            }
        }

    }

    private fun setOnClick() {
        binding?.run {
            ivCart.setOnClickListener {
                if (cartViewModel.productsAdded.value.isNotEmpty())
                    findNavController().navigate(R.id.homeToCart)
                else
                    showError(true, "Debe agregar productos a su carrito antes de continuar")
            }
            ivFavorite.setOnClickListener { findNavController().navigate(R.id.homeToFavorite) }
        }
    }

    private fun setupRecycler() {
        homeAdapter = HomeAdapter(listOf(), select = { data ->
            cartViewModel.addProduct(data)
        })
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.recycler?.setHasFixedSize(true)
        binding?.recycler?.layoutManager = layoutManager
        binding?.recycler?.adapter = homeAdapter
    }
}