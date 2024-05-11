package com.android.guacamole.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.guacamole.data.UiState
import com.android.guacamole.data.dataBase.entity.User
import com.android.guacamole.ui.usesCase.UserUseCase
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
class LoginViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    private val _loginUser: MutableStateFlow<UiState<User?>> =
        MutableStateFlow(UiState.Loading(false, ""))
    val loginUser = _loginUser.asStateFlow()

    fun loginUser(user: String, password: String) {
        viewModelScope.launch {
            userUseCase.invoke(user, password)
                .flowOn(Dispatchers.IO)
                .onStart {
                    _loginUser.value = UiState.Loading(true, "Loading...")
                    delay(3000)
                }.catch { e ->
                    _loginUser.value = UiState.NotSuccess(-1, e)
                }.collect {
                    _loginUser.value = UiState.Success(it)
                }
        }
    }
}