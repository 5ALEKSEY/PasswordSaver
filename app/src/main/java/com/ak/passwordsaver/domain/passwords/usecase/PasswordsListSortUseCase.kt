package com.ak.passwordsaver.domain.passwords.usecase

import com.ak.domain.data.model.db.entities.PasswordDBEntity

class PasswordsListSortUseCase {

    fun descendingOrderById(unsortedPasswordsList: List<PasswordDBEntity>) =
        orderListById(unsortedPasswordsList, true)

    fun ascendingOrderById(unsortedPasswordsList: List<PasswordDBEntity>) =
        orderListById(unsortedPasswordsList, false)

    private fun orderListById(unsortedPasswordsList: List<PasswordDBEntity>, isDesc: Boolean)
            : List<PasswordDBEntity> {
        val comparator = if (isDesc) {
            compareByDescending(PasswordDBEntity::passwordId)
        } else {
            compareBy(PasswordDBEntity::passwordId)
        }
        return unsortedPasswordsList.sortedWith(comparator)
    }
}