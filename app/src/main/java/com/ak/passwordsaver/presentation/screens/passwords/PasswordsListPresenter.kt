package com.ak.passwordsaver.presentation.screens.passwords

import android.util.Log
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.data.model.PasswordShowingType
import com.ak.passwordsaver.data.model.db.entities.PasswordDBEntity
import com.ak.passwordsaver.data.model.internalstorage.IPSInternalStorageManager
import com.ak.passwordsaver.data.model.preferences.SettingsPreferencesManager
import com.ak.passwordsaver.domain.passwords.IPasswordsInteractor
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.ak.passwordsaver.presentation.base.constants.AppConstants
import com.ak.passwordsaver.presentation.screens.passwords.adapter.PasswordItemModel
import com.ak.passwordsaver.presentation.screens.passwords.logic.IDataBufferManager
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class PasswordsListPresenter : BasePSPresenter<IPasswordsListView>() {

    @Inject
    lateinit var mPasswordsInteractor: IPasswordsInteractor
    @Inject
    lateinit var mSettingsPreferencesManager: SettingsPreferencesManager
    @Inject
    lateinit var mPSInternalStorageManager: IPSInternalStorageManager
    @Inject
    lateinit var mDataBufferManager: IDataBufferManager

    private var mCurrentPasswordId = 0L

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadPasswords()
    }

    fun passwordShowActionRequired(passwordId: Long, isVisiblePasswordContent: Boolean) {
        mCurrentPasswordId = passwordId
        startShowingPassword(isVisiblePasswordContent, passwordId)
    }

    fun onShowPasswordActions(passwordId: Long) {
        mCurrentPasswordId = passwordId
        viewState.showPasswordActionsDialog()
    }

    // from actions bottom sheet dialog
    fun onCopyPasswordAction() {
        getPasswordDataAndStartAction {
            mDataBufferManager.copyStringData(it.passwordContent)
            viewState.showShortTimeMessage("Copied to clipboard")
            mCurrentPasswordId = 0L
        }
    }

    fun onEditPasswordAction() {
        viewState.showEditPasswordScreen(mCurrentPasswordId)
        mCurrentPasswordId = 0L
    }

    fun onDeletePasswordAction() {
        mPasswordsInteractor.deletePasswordById(mCurrentPasswordId)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { mCurrentPasswordId = 0L }
            .subscribe(
                {
                    viewState.showShortTimeMessage("deleted")
                },
                { throwable ->
                    viewState.showShortTimeMessage(throwable.message ?: "unknown")
                })
            .let(this::bindDisposable)
    }

    private fun startShowingPassword(newVisibilityState: Boolean, passwordId: Long) {
        val showingType = mSettingsPreferencesManager.getPasswordShowingType()

        if (showingType == PasswordShowingType.IN_CARD && !newVisibilityState) {
            // hide password content for 'in card' mode
            viewState.setPasswordVisibilityInCardMode(passwordId, false)
        } else {
            // open state
            when (showingType) {
                PasswordShowingType.TOAST -> getPasswordDataAndStartAction {
                    viewState.openPasswordToastMode(it.passwordName, it.passwordContent)
                    mCurrentPasswordId = 0L
                }
                PasswordShowingType.IN_CARD -> viewState.setPasswordVisibilityInCardMode(
                    passwordId,
                    true
                )
            }
        }
    }

    private inline fun getPasswordDataAndStartAction(crossinline action: (entity: PasswordDBEntity) -> Unit) {
        mPasswordsInteractor.getPasswordById(mCurrentPasswordId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { entity ->
                    action(entity)
                },
                { throwable ->
                    Log.d("dddd", "dddd")
                })
            .let(this::bindDisposable)
    }

    private fun loadPasswords() {
        mPasswordsInteractor.getAllPasswords()
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
            val avatarBitmap =
                mPSInternalStorageManager.getBitmapImageFromPath(it.passwordAvatarPath)
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