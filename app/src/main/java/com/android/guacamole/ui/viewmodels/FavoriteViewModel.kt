package com.android.guacamole.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.guacamole.data.UiState
import com.android.guacamole.data.models.SalesReportWithProducts
import com.android.guacamole.ui.usesCase.ShoppingCartDetailUseCase
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
class FavoriteViewModel @Inject constructor(
    private val shoppingCartDetailUseCase: ShoppingCartDetailUseCase
) : ViewModel() {

    private val _obtainResume: MutableStateFlow<UiState<SalesReportWithProducts>> =
        MutableStateFlow(UiState.Loading(false, ""))
    val obtainResume = _obtainResume.asStateFlow()

    fun obtainResume(userId: String) {
        viewModelScope.launch {
            shoppingCartDetailUseCase.invoke(userId).flowOn(Dispatchers.IO).onStart {
                _obtainResume.value = UiState.Loading(true, "Loading...")
                delay(3000)
            }.catch { e ->
                _obtainResume.value = UiState.NotSuccess(-1, e)
            }.collect {
                _obtainResume.value = UiState.Success(it)
            }
        }
    }
}