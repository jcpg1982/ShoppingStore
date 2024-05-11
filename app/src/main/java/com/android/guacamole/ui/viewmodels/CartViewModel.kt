package com.android.guacamole.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.guacamole.data.UiState
import com.android.guacamole.data.dataBase.entity.CardInformationEntity
import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.dataBase.entity.ShoppingCartDetailEntity
import com.android.guacamole.data.dataBase.entity.ShoppingCartEntity
import com.android.guacamole.data.dataBase.entity.User
import com.android.guacamole.data.models.DetailProduct
import com.android.guacamole.ui.usesCase.CardInformationUseCase
import com.android.guacamole.ui.usesCase.ShoppingCartDetailUseCase
import com.android.guacamole.ui.usesCase.ShoppingCartUseCase
import com.android.guacamole.utils.Utils.generateUnique
import com.android.guacamole.utils.Utils.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cardInformationUseCase: CardInformationUseCase,
    private val shoppingCartDetailUseCase: ShoppingCartDetailUseCase,
    private val shoppingCartUseCase: ShoppingCartUseCase
) : ViewModel() {

    var user: User? = null

    private val _saveShoppingCart: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading(false, ""))
    val saveShoppingCart = _saveShoppingCart.asStateFlow()

    private val _productsAdded: MutableStateFlow<List<DetailProduct>> = MutableStateFlow(listOf())
    val productsAdded = _productsAdded.asStateFlow()
    private val _sizeProductsAdded: MutableStateFlow<Int> = MutableStateFlow(0)
    val sizeProductsAdded = _sizeProductsAdded.asStateFlow()
    private val _amountTotal: MutableStateFlow<Double> = MutableStateFlow(getAmountTotal)
    val amountTotal = _amountTotal.asStateFlow()

    val getAmountTotal: Double get() = productsAdded.value.sumOf { it.quantity.orZero * it.price.orZero }

    lateinit var cardInformationEntity: CardInformationEntity
    lateinit var shoppingCartEntity: ShoppingCartEntity
    var listShoppingCartDetail = listOf<ShoppingCartDetailEntity>()

    fun initValues() {
        cardInformationEntity = CardInformationEntity(idCardInformation = generateUnique)
        shoppingCartEntity =
            ShoppingCartEntity(idShoppingCart = generateUnique, userId = user?.userId)
    }

    fun addProduct(productEntity: ProductEntity) {
        val detail = DetailProduct(productEntity)
        _productsAdded.update { list ->
            val index = list.indexOfFirst { it.idProduct == detail.idProduct }
            if (index != -1) {
                val item = list[index]
                list.toMutableList().apply {
                    this[index] =
                        item.copy(quantity = item.quantity.orZero + detail.quantity.orZero)
                }
            } else {
                list + detail
            }
        }
        _sizeProductsAdded.update { productsAdded.value.size }
        _amountTotal.update { getAmountTotal }
    }

    fun addQuantity(detailProduct: DetailProduct) {
        _productsAdded.update { list ->
            val index = list.indexOfFirst { it.idProduct == detailProduct.idProduct }
            if (index != -1) {
                val item = list[index]
                list.toMutableList().apply {
                    this[index] = item.copy(quantity = item.quantity.orZero + 1)
                }
            } else list
        }
        _sizeProductsAdded.update { productsAdded.value.size }
        _amountTotal.update { getAmountTotal }
    }

    fun removeQuantity(detailProduct: DetailProduct) {
        _productsAdded.update { list ->
            val index = list.indexOfFirst { it.idProduct == detailProduct.idProduct }
            if (index != -1) {
                val item = list[index]
                list.toMutableList().apply {
                    this[index] = item.copy(quantity = item.quantity.orZero - 1)
                }
            } else list
        }
        _sizeProductsAdded.update { productsAdded.value.size }
        _amountTotal.update { getAmountTotal }
    }

    fun removeProduct(detailProduct: DetailProduct) {
        _productsAdded.update { list ->
            val index = list.indexOfFirst { it.idProduct == detailProduct.idProduct }
            if (index != -1) {
                list.toMutableList().apply {
                    removeAt(index)
                }
            } else list
        }
        _sizeProductsAdded.update { productsAdded.value.size }
        _amountTotal.update { getAmountTotal }
    }

    fun saveCardInformation() {
        viewModelScope.launch {
            cardInformationUseCase.invoke(cardInformationEntity).flowOn(Dispatchers.IO).onStart {
                _saveShoppingCart.value = UiState.Loading(true, "Loading...")
            }.catch { e ->
                _saveShoppingCart.value = UiState.NotSuccess(-1, e)
            }.collect { saveShoppingCartDetail() }
        }
    }

    fun saveShoppingCartDetail() {
        viewModelScope.launch {
            shoppingCartDetailUseCase.invoke(listShoppingCartDetail).flowOn(Dispatchers.IO)
                .onStart {
                    _saveShoppingCart.value = UiState.Loading(true, "Loading...")
                }.catch { e ->
                    _saveShoppingCart.value = UiState.NotSuccess(-1, e)
                }.collect { saveShoppingCart() }
        }
    }

    fun saveShoppingCart() {
        viewModelScope.launch {
            shoppingCartUseCase.invoke(shoppingCartEntity).flowOn(Dispatchers.IO).onStart {
                _saveShoppingCart.value = UiState.Loading(true, "Loading...")
            }.catch { e ->
                _saveShoppingCart.value = UiState.NotSuccess(-1, e)
            }.collect {
                resetData()
                _saveShoppingCart.value = UiState.Success(true)
                _saveShoppingCart.value = UiState.Loading(false, "")
            }
        }
    }

    fun resetData() {
        _productsAdded.update { listOf() }
        _sizeProductsAdded.update { productsAdded.value.size }
        _amountTotal.update { getAmountTotal }
        cardInformationEntity = CardInformationEntity("")
        listShoppingCartDetail = listOf()
        shoppingCartEntity = ShoppingCartEntity("")
    }
}