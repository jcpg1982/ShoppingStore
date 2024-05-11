package com.android.guacamole.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.guacamole.data.UiState
import com.android.guacamole.data.dataBase.entity.User
import com.android.guacamole.data.enums.Action
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
class RegisterViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    var user = User()

    private val _registerUser: MutableStateFlow<UiState<Unit>> =
        MutableStateFlow(UiState.Loading(false, ""))
    val registerUser = _registerUser.asStateFlow()

    fun registerUser(user: User) {
        viewModelScope.launch {
            userUseCase.invoke(user, Action.INSERT)
                .flowOn(Dispatchers.IO)
                .onStart {
                    _registerUser.value = UiState.Loading(true, "Loading...")
                    delay(3000)
                }.catch { e ->
                    _registerUser.value = UiState.NotSuccess(-1, e)
                }.collect {
                    _registerUser.value = UiState.Success(it)
                }
        }
    }
}