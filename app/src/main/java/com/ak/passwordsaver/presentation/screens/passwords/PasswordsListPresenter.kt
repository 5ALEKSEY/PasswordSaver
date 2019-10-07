package com.ak.passwordsaver.presentation.screens.passwords

import android.util.Log
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.PasswordShowingType
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.screens.passwords.adapter.PasswordItemModel
import com.ak.passwordsaver.presentation.screens.passwords.logic.PasswordsListInteractor
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class PasswordsListPresenter : BasePSPresenter<IPasswordsListView>() {

    @Inject
    lateinit var mPasswordsListInteractor: PasswordsListInteractor
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

    fun onSecurityAuthSuccessful() {
        showPasswordAction(true, mCurrentPasswordId)
    }

    fun onSecurityAuthCanceled() {
        Log.d("Alex_tester", "canceled")
    }

    fun passwordShowActionRequired(passwordId: Long, newVisibilityState: Boolean) {
        // TODO: check passcodes (mb save this passwordId in presenter for feature)
        mCurrentPasswordId = passwordId
        if (newVisibilityState) {
            viewState.startSecurityAuthAction()
        } else {
            showPasswordAction(newVisibilityState, passwordId)
        }
    }

    private fun showPasswordAction(newVisibilityState: Boolean, passwordId: Long) {
        val showingType = mSettingsPreferencesManager.getPasswordShowingType()

        if (showingType == PasswordShowingType.IN_CARD && !newVisibilityState) {
            // hide password content for 'in card' mode
            viewState.setPasswordVisibilityInCardMode(passwordId, false)
        } else {
            // open state
            when (showingType) {
                PasswordShowingType.TOAST -> getPasswordDataAndStartAction(viewState::openPasswordToastMode)
                PasswordShowingType.IN_CARD -> viewState.setPasswordVisibilityInCardMode(
                    passwordId,
                    true
                )
            }
        }
    }

    private fun getPasswordDataAndStartAction(action: (name: String, content: String) -> Unit) {
        mPasswordsListInteractor.getPasswordById(mCurrentPasswordId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { entity ->
                    action.invoke(entity.passwordName, entity.passwordContent)
                },
                { throwable ->
                    Log.d("dddd", "dddd")
                })
            .let(this::bindDisposable)
    }

    private fun loadPasswords() {
        mPasswordsListInteractor.getAndListenAllPasswords()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                viewState.setLoadingState(true)
                viewState.setEmptyPasswordsState(false)
            }
            .subscribe(
                { list ->
                    viewState.setLoadingState(false)
                    viewState.displayPasswords(convertDBEntitiesList(list))
                    viewState.setEmptyPasswordsState(list.isEmpty())
                },
                { throwable ->
                    Log.d("de", "dede")
                })
            .let(this::bindDisposable)
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