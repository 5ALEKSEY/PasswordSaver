package com.ak.feature_tabpasswords_impl.screens.presentation.passwords

import android.util.Log
import com.ak.base.constants.AppConstants
import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.core_repo_api.intefaces.ISettingsPreferencesManager
import com.ak.feature_tabpasswords_api.interfaces.IPasswordsInteractor
import com.ak.feature_tabpasswords_api.interfaces.PasswordFeatureEntity
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.screens.adapter.PasswordItemModel
import com.ak.feature_tabpasswords_impl.screens.logic.IDataBufferManager
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import moxy.InjectViewState

@InjectViewState
class PasswordsListPresenter @Inject constructor(
    private val passwordsInteractor: IPasswordsInteractor,
    private val settingsPreferencesManager: ISettingsPreferencesManager,
    private val internalStorageManager: IPSInternalStorageManager,
    private val dataBufferManager: IDataBufferManager,
    private val resourceManager: IResourceManager
) : BasePSPresenter<IPasswordsListView>() {

    private var mCurrentPasswordId = 0L

    init {
        FeatureTabPasswordsComponent.get().inject(this)
    }

    fun loadPasswords() {
        passwordsInteractor.getAllPasswords()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                viewState.setLoadingState(true)
                viewState.setEmptyPasswordsState(false)
            }
            .subscribe(
                { list ->
                    val listForDisplay = convertFeatureEntitiesList(list)
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

    fun onShowPasswordActions(passwordId: Long, isContentVisible: Boolean) {
        mCurrentPasswordId = passwordId
        viewState.showPasswordActionsDialog(isContentVisible)
    }

    // from actions bottom sheet dialog
    fun onShowPasswordAction() {
        viewState.setPasswordContentVisibility(mCurrentPasswordId, true)
        mCurrentPasswordId = 0L
    }

    fun onHidePasswordAction() {
        viewState.setPasswordContentVisibility(mCurrentPasswordId, false)
        mCurrentPasswordId = 0L
    }

    fun onCopyPasswordAction() {
        getPasswordDataAndStartAction {
            dataBufferManager.copyStringData(it.getPasswordContent())
            viewState.showShortTimeMessage(resourceManager.getString(R.string.copied_to_clipboard_message))
            mCurrentPasswordId = 0L
        }
    }

    fun onEditPasswordAction() {
        viewState.showEditPasswordScreen(mCurrentPasswordId)
        mCurrentPasswordId = 0L
    }

    fun onDeletePasswordAction() {
        passwordsInteractor.deletePasswordById(mCurrentPasswordId)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { mCurrentPasswordId = 0L }
            .subscribe(
                {
                },
                { throwable ->
                    viewState.showShortTimeMessage(throwable.message ?: "unknown")
                })
            .let(this::bindDisposable)
    }

    private inline fun getPasswordDataAndStartAction(crossinline action: (entity: PasswordFeatureEntity) -> Unit) {
        passwordsInteractor.getPasswordById(mCurrentPasswordId)
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

    private fun handleListForDisplay(listForDisplay: List<PasswordItemModel>) {
        if (listForDisplay.size >= AppConstants.TOOLBAR_SCROLL_MIN_PASSWORDS_SIZE) {
            viewState.enableToolbarScrolling()
        } else {
            viewState.disableToolbarScrolling()
        }
    }

    private fun convertFeatureEntitiesList(entitiesList: List<PasswordFeatureEntity>): List<PasswordItemModel> {
        val resultList = arrayListOf<PasswordItemModel>()
        entitiesList.forEach {
            val avatarBitmap =
                internalStorageManager.getBitmapImageFromPath(it.getPasswordAvatarPath())
            resultList.add(
                PasswordItemModel(
                    it.getPasswordId()!!,
                    it.getPasswordName(),
                    avatarBitmap,
                    it.getPasswordContent()
                )
            )
        }
        return resultList
    }
}