package com.ak.passwordsaver.presentation.screens.passwords

import android.util.Log
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.model.PasswordShowingType
import com.ak.passwordsaver.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.base.managers.bitmapdecoder.IBitmapDecoderManager
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
    @Inject
    lateinit var mBitmapDecoderManager: IBitmapDecoderManager

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

    fun passwordShowActionRequired(passwordId: Long, isVisiblePasswordContent: Boolean) {
        mCurrentPasswordId = passwordId
        val isAuthEnabled = mSettingsPreferencesManager.isPincodeEnabled()
        if (isVisiblePasswordContent && isAuthEnabled) {
            viewState.startSecurityAuthAction()
            return
        }
        showPasswordAction(isVisiblePasswordContent, passwordId)
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
                    val listForDisplay = convertDBEntitiesList(list)
                    viewState.setLoadingState(false)
                    viewState.displayPasswords(listForDisplay)
                    viewState.setEmptyPasswordsState(list.isEmpty())
                    handleListForDisplay(listForDisplay)
                },
                { throwable ->
                    Log.d("de", "dede")
                })
            .let(this::bindDisposable)
    }

    private fun handleListForDisplay(listForDisplay: List<PasswordItemModel>) {
        if (listForDisplay.size >= AppConstants.TOOLBAR_SCROLL_MIN_PASSWORDS_SIZE) {
            viewState.enableToolbarScrolling()
        } else {
            viewState.disableToolbarScrolling()
        }
    }

    private fun convertDBEntitiesList(entitiesList: List<PasswordDBEntity>): List<PasswordItemModel> {
        val showingType = mSettingsPreferencesManager.getPasswordShowingType()
        val resultList = arrayListOf<PasswordItemModel>()
        entitiesList.forEach {
            val avatarBitmap = mBitmapDecoderManager.decodeBitmap(it.passwordAvatarPath)
            resultList.add(
                PasswordItemModel(
                    it.passwordId!!,
                    it.passwordName,
                    avatarBitmap,
                    it.passwordContent,
                    showingType == PasswordShowingType.IN_CARD
                )
            )
        }
        return resultList
    }
}