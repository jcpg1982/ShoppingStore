package com.android.guacamole.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.guacamole.R
import com.android.guacamole.data.UiState
import com.android.guacamole.data.dataBase.entity.ShoppingCartDetailEntity
import com.android.guacamole.data.enums.ActionTextWatcher
import com.android.guacamole.data.enums.TypePayment
import com.android.guacamole.databinding.FragmentPaymentBinding
import com.android.guacamole.ui.adapters.SpinnerAnyAdapter
import com.android.guacamole.ui.interfaces.TextWatcherListener
import com.android.guacamole.ui.viewmodels.CartViewModel
import com.android.guacamole.utils.Utils
import com.android.guacamole.utils.Utils.formatDecimal
import com.android.guacamole.utils.Utils.generateUnique
import com.android.guacamole.utils.Utils.hideKeyboard
import com.android.guacamole.utils.Validators.validateNotEmpty
import com.android.guacamole.utils.Validators.validateSize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class PaymentFragment : BaseFragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val cartViewModel: CartViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    private var mTypePayment: TypePayment? = null

    val validaCard: Boolean
        get() {
            var validated = false
            if (binding?.txtCcUser?.validateNotEmpty == true) if (binding?.txtCcNumber?.validateSize(
                    16
                ) == true
            ) if (binding?.txtCcDate?.validateNotEmpty == true) if (binding?.txtCcCcv?.validateSize(
                    3
                ) == true
            ) validated = true
            return validated
        }

    val validaCash: Boolean
        get() {
            var validated = false
            if (binding?.txtAmountPayment?.validateNotEmpty == true) if (binding?.inputAmountPayment?.text.toString()
                    .toDouble() >= cartViewModel.amountTotal.value
            ) validated = true
            return validated
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        cartViewModel.initValues()
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClick()
        initWatcher()
        loadSpinnerTypePayment(listOf(TypePayment.CARD, TypePayment.CASH))
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
        lifecycleScope.launch {
            cartViewModel.amountTotal.collect {
                binding?.txtTotal?.text = "S/ ${it.formatDecimal}"
                val igv = it * (18.00 / 100.00)
                binding?.txtIgv?.text = "S/ ${igv.formatDecimal}"
                val subTotal = it - igv
                binding?.txtSubTotal?.text = "S/ ${subTotal.formatDecimal}"
            }
        }
        lifecycleScope.launch {
            cartViewModel.saveShoppingCart.collect {
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
                        findNavController().navigate(R.id.paymentToHome)
                    }
                }
            }
        }
    }

    private fun setOnClick() {
        binding?.run {
            ivBack.setOnClickListener { findNavController().popBackStack() }
            btnPay.setOnClickListener {
                showLoading(true, "Loading....")
                cartViewModel.shoppingCartEntity.apply {
                    this.dateTime = Utils.dateTime
                    typePayment = mTypePayment
                    amountReceived =
                        if (mTypePayment == TypePayment.CARD) cartViewModel.amountTotal.value
                        else inputAmountPayment.text.toString().toDouble()
                    amountPaid = cartViewModel.amountTotal.value
                    idCardInformation =
                        if (mTypePayment == TypePayment.CARD) cartViewModel.cardInformationEntity.idCardInformation
                        else null
                }
                cartViewModel.cardInformationEntity.apply {
                    if (mTypePayment == TypePayment.CARD) {
                        nameUser = inputCcUser.text.toString()
                        numberCC = inputCcNumber.text.toString()
                        dateCC = inputCcDate.text.toString()
                        ccvCC = inputCcCcv.text.toString()
                    }
                }
                cartViewModel.listShoppingCartDetail =
                    cartViewModel.productsAdded.value.mapIndexed { index, detailProduct ->
                        ShoppingCartDetailEntity(
                            "$index-$generateUnique",
                            cartViewModel.shoppingCartEntity.idShoppingCart,
                            detailProduct
                        )
                    }
                if (mTypePayment == TypePayment.CARD) cartViewModel.saveCardInformation()
                else cartViewModel.saveShoppingCartDetail()
            }
        }
    }

    private fun initWatcher() {
        binding?.run {
            inputAmountPayment.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher,
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    afterOrBefore: Int
                ) {
                    when (action) {
                        ActionTextWatcher.ON_TEXT_CHANGED -> {
                            btnPay.isEnabled = validaCash
                            val amount = if (inputAmountPayment.text.isNullOrEmpty()) 0.00
                            else inputAmountPayment.text.toString().toDouble()
                            val vuelto = amount - cartViewModel.amountTotal.value
                            verVuelto(vuelto)
                        }

                        else -> {}
                    }
                }
            })

            inputCcUser.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher,
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    afterOrBefore: Int
                ) {
                    when (action) {
                        ActionTextWatcher.ON_TEXT_CHANGED -> {
                            btnPay.isEnabled = validaCard
                        }

                        else -> {}
                    }
                }
            })

            inputCcNumber.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher,
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    afterOrBefore: Int
                ) {
                    when (action) {
                        ActionTextWatcher.ON_TEXT_CHANGED -> {
                            btnPay.isEnabled = validaCard
                        }

                        else -> {}
                    }
                }
            })

            inputCcDate.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher,
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    afterOrBefore: Int
                ) {
                    when (action) {
                        ActionTextWatcher.ON_TEXT_CHANGED -> {
                            btnPay.isEnabled = validaCard
                        }

                        else -> {}
                    }
                }
            })

            inputCcCcv.addTextChangedListener(object : TextWatcherListener {
                override fun action(
                    action: ActionTextWatcher,
                    charSequence: CharSequence?,
                    start: Int,
                    count: Int,
                    afterOrBefore: Int
                ) {
                    when (action) {
                        ActionTextWatcher.ON_TEXT_CHANGED -> {
                            btnPay.isEnabled = validaCard
                        }

                        else -> {}
                    }
                }
            })
        }
    }

    private fun loadSpinnerTypePayment(listTypePayment: List<TypePayment>) {
        val adapter = SpinnerAnyAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, listTypePayment
        )
        binding?.run {
            spTypePayment.threshold = 1
            spTypePayment.setAdapter(adapter)
            spTypePayment.setText("---- Seleccione tipo de pago ----")
            spTypePayment.setOnItemClickListener { adapterView, view, position, id ->
                mTypePayment = adapterView.getItemAtPosition(position) as? TypePayment
                spTypePayment.setText(mTypePayment?.typePayment)
                binding?.txtTypePayment?.error = null
                when (mTypePayment) {
                    TypePayment.CASH -> {
                        val amount = if (inputAmountPayment.text.isNullOrEmpty()) 0.00
                        else inputAmountPayment.text.toString().toDouble()
                        val vuelto = amount - cartViewModel.amountTotal.value
                        containerCard.visibility = View.GONE
                        containerCash.visibility = View.VISIBLE
                        verVuelto(vuelto)
                        btnPay.isEnabled = validaCash
                    }

                    TypePayment.CARD -> {
                        containerCash.visibility = View.GONE
                        containerCard.visibility = View.VISIBLE
                        btnPay.isEnabled = validaCard
                    }

                    else -> {}
                }
            }
        }
    }

    fun verVuelto(vuelto: Double) {
        binding?.run {
            if (vuelto >= 0) {
                txtVuelto.visibility = View.VISIBLE
                txtVuelto.text = "S/ ${vuelto.formatDecimal}"
            } else txtVuelto.visibility = View.GONE
        }
    }
}