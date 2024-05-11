package com.android.guacamole.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.guacamole.R
import com.android.guacamole.databinding.FragmentCartBinding
import com.android.guacamole.ui.adapters.CartAdapter
import com.android.guacamole.ui.viewmodels.CartViewModel
import com.android.guacamole.utils.Utils.formatDecimal
import com.android.guacamole.utils.Utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class CartFragment : BaseFragment() {

    private var _binding: FragmentCartBinding? = null
    private val cartViewModel: CartViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    private var cartAdapter: CartAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setOnClick()
    }

    override fun onResume() {
        super.onResume()
        initObserver()
        this.hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {

        //cart
        lifecycleScope.launch {
            cartViewModel.productsAdded.collect {
                cartAdapter?.dataList = it
                cartAdapter?.notifyDataSetChanged()
            }
        }
        lifecycleScope.launch {
            cartViewModel.amountTotal.collect {
                binding?.txtTotal?.text = "S/ ${it.formatDecimal}"
                val igv = it * (18.00 / 100.00)
                binding?.txtIgv?.text = "S/ ${igv.formatDecimal}"
                val subTotal = it - igv
                binding?.txtSubTotal?.text = "S/ ${subTotal.formatDecimal}"
            }
        }

    }

    private fun setOnClick() {
        binding?.run {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            btnPay.setOnClickListener { findNavController().navigate(R.id.cartToPayment) }
        }
    }

    private fun setupRecycler() {
        cartAdapter = CartAdapter(listOf(),
            delete = { data -> cartViewModel.removeProduct(data) },
            add = { data -> cartViewModel.addQuantity(data) },
            remove = { data -> cartViewModel.removeQuantity(data) })
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.recycler?.setHasFixedSize(true)
        binding?.recycler?.layoutManager = layoutManager
        binding?.recycler?.adapter = cartAdapter
    }
}