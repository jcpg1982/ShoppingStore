package com.android.guacamole.data

sealed class UiState<out T> {

    data class Loading(val isLoading: Boolean, val message: String) : UiState<Nothing>()

    data class NotSuccess(val code: Int, val throwable: Throwable) : UiState<Nothing>()

    data class Success<out R>(val data: R) : UiState<R>()
}