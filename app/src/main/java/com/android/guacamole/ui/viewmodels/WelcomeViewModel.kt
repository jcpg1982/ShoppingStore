package com.android.guacamole.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.guacamole.data.UiState
import com.android.guacamole.data.dataBase.entity.User
import com.android.guacamole.data.models.DataJson
import com.android.guacamole.ui.usesCase.CategoryUseCase
import com.android.guacamole.ui.usesCase.ProductUseCase
import com.android.guacamole.ui.usesCase.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val categoryUseCase: CategoryUseCase,
    private val storeUseCase: StoreUseCase,
    private val productUseCase: ProductUseCase
) : ViewModel() {

    private val _saveData: MutableStateFlow<UiState<Boolean>> =
        MutableStateFlow(UiState.Loading(false, ""))
    val saveData = _saveData.asStateFlow()

    fun saveData(dataJson: DataJson) {
        viewModelScope.launch {
            if (dataJson.category?.isNotEmpty() == true) {
                categoryUseCase.invoke(dataJson.category!!)
                    .flowOn(Dispatchers.IO).onStart {
                        _saveData.value = UiState.Loading(true, "Loading...")
                    }.catch { e ->
                        _saveData.value = UiState.NotSuccess(-1, e)
                    }.collect { saveStore(dataJson) }
            } else {
                saveStore(dataJson)
            }
        }
    }

    fun saveStore(dataJson: DataJson) {
        viewModelScope.launch {
            if (dataJson.store?.isNotEmpty() == true) {
                storeUseCase.invoke(dataJson.store!!)
                    .flowOn(Dispatchers.IO).onStart {
                        _saveData.value = UiState.Loading(true, "Loading...")
                    }.catch { e ->
                        _saveData.value = UiState.NotSuccess(-1, e)
                    }.collect { saveProducts(dataJson) }
            } else {
                saveProducts(dataJson)
            }
        }
    }

    fun saveProducts(dataJson: DataJson) {
        viewModelScope.launch {
            if (dataJson.products?.isNotEmpty() == true) {
                productUseCase.invoke(dataJson.products!!)
                    .flowOn(Dispatchers.IO).onStart {
                        _saveData.value = UiState.Loading(true, "Loading...")
                    }.catch { e ->
                        _saveData.value = UiState.NotSuccess(-1, e)
                    }.collect {
                        _saveData.value = UiState.Success(true)
                    }
            } else {
                _saveData.value = UiState.Success(true)
            }
        }
    }
}