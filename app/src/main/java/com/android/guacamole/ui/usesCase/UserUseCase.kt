package com.android.guacamole.ui.usesCase

import com.android.guacamole.data.dataBase.entity.User
import com.android.guacamole.data.enums.Action
import com.android.guacamole.data.repositorio.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(user: User, action: Action): Flow<Unit> =
        when (action) {
            Action.INSERT -> repository.insertUser(user)
            Action.UPDATE -> repository.updateUser(user)
            Action.DELETE -> repository.deleteByIdUser(user.userId)
            Action.GET -> TODO()
        }

    suspend operator fun invoke(user: String, password: String): Flow<User?> =
        repository.getUserByIdUser(user, password)
}