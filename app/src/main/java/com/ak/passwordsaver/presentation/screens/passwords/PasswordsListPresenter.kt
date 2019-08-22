package com.ak.passwordsaver.presentation.screens.passwords

import android.util.Log
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.PasswordShowingType
import com.ak.passwordsaver.model.db.PSDatabase
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.passwords.adapter.PasswordItemModel
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class PasswordsListPresenter : BasePSPresenter<IPasswordsListView>() {

    @Inject
    lateinit var mDatabase: PSDatabase
    @Inject
    lateinit var mSettingsPreferencesManager: SettingsPreferencesManager

    private var mCurrentPasswordId = 0L

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadPasswords()
    }

    fun passwordShowActionRequired(passwordId: Long) {
        // TODO: check passcodes (mb save this passwordId in presenter for feature)
        mCurrentPasswordId = passwordId
        val showingType = mSettingsPreferencesManager.getPasswordShowingType()

        when (showingType) {
            PasswordShowingType.DIALOG -> getPasswordDataAndStartAction(viewState::openPasswordDialogMode)
            PasswordShowingType.TOAST -> getPasswordDataAndStartAction(viewState::openPasswordToastMode)
            PasswordShowingType.IN_CARD -> viewState.openPasswordInCardMode(passwordId)
        }
    }

    private fun getPasswordDataAndStartAction(action: (name: String, content: String) -> Unit) {
        mDatabase.getPasswordsDao().getPasswordById(mCurrentPasswordId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { entity ->
                    action.invoke(entity.passwordName, entity.passwordContent)
                },
                { throwable ->
                    Log.d("dddd", "dddd")
                })
            .let(::bindDisposable)
    }

    private fun loadPasswords() {
        mDatabase.getPasswordsDao().getAllPasswords()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    if (list.isEmpty()) {
                        viewState.displayEmptyPasswordsState()
                    } else {
                        viewState.displayPasswords(convertDBEntitiesList(list))
                    }
                },
                { throwable ->
                    Log.d("de", "dede")
                })
            .let(::bindDisposable)
    }

    private fun convertDBEntitiesList(entitiesList: List<PasswordDBEntity>): List<PasswordItemModel> {
        val showingType = mSettingsPreferencesManager.getPasswordShowingType()
        val resultList = arrayListOf<PasswordItemModel>()
        for (entity in entitiesList) {
            resultList.add(
                PasswordItemModel(
                    entity.passwordId!!,
                    entity.passwordName,
                    entity.passwordUrl ?: "",
                    entity.passwordContent,
                    showingType == PasswordShowingType.IN_CARD
                )
            )
        }
        return resultList
    }
}