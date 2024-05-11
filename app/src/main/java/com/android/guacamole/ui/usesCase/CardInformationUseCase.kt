package com.android.guacamole.ui.usesCase

import com.android.guacamole.data.dataBase.entity.CardInformationEntity
import com.android.guacamole.data.repositorio.Repository
import javax.inject.Inject

class CardInformationUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(cardInformationEntity: CardInformationEntity) =
        repository.insertCardInformationEntity(cardInformationEntity)

}