package com.android.guacamole.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.guacamole.data.UiState
import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.models.pojo.ProductWithStoreAndCategory
import com.android.guacamole.ui.usesCase.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productUseCase: ProductUseCase
) : ViewModel() {

    private val _getProducts: MutableStateFlow<UiState<List<ProductWithStoreAndCategory>>> =
        MutableStateFlow(UiState.Loading(false, ""))
    val getProducts = _getProducts.asStateFlow()

    fun getProducts() {
        viewModelScope.launch {
            productUseCase.invoke().flowOn(Dispatchers.IO).onStart {
                _getProducts.value = UiState.Loading(true, "Loading...")
            }.catch { e ->
                _getProducts.value = UiState.NotSuccess(-1, e)
            }.collect {
                _getProducts.value = UiState.Success(it)
            }
        }
    }
}