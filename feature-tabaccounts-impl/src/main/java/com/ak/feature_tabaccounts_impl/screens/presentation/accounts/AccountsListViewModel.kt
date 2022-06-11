package com.ak.feature_tabaccounts_impl.screens.presentation.accounts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.constants.AppConstants
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.screens.adapter.AccountItemModel
import com.ak.feature_tabaccounts_impl.screens.logic.IDataBufferManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class AccountsListViewModel @Inject constructor(
    private val accountsInteractor: IAccountsInteractor,
    private val dataBufferManager: IDataBufferManager,
    private val resourceManager: IResourceManager
) : BasePSViewModel() {

    private val loadingStateLD = MutableLiveData<Boolean>()
    private val emptyAccountsStateLD = MutableLiveData<Boolean>()
    private val showEditAccountScreenLD = SingleEventLiveData<Long>()
    private val toolbarScrollingStateLD = MutableLiveData<Boolean>()
    private val accountsListLD = MutableLiveData<List<AccountItemModel>>()

    fun subscribeToLoadingState(): LiveData<Boolean> = loadingStateLD
    fun subscribeEmptyAccountsState(): LiveData<Boolean> = emptyAccountsStateLD
    fun subscribeToShowEditPasswordScreen(): LiveData<Long> = showEditAccountScreenLD
    fun subscribeToToolbarScrollingState(): LiveData<Boolean> = toolbarScrollingStateLD
    fun subscribeToAccountsList(): LiveData<List<AccountItemModel>> = accountsListLD

    fun loadPasswords() {
        accountsInteractor.getAllAccounts()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingStateLD.value = true
                emptyAccountsStateLD.value = false
            }
            .subscribe(
                { list ->
                    val listForDisplay = convertFeatureEntitiesList(list)
                    loadingStateLD.value = false
                    accountsListLD.value = listForDisplay
                    emptyAccountsStateLD.value = list.isEmpty()
                    handleListForDisplay(listForDisplay)
                },
                { throwable ->
                    Log.d("de", "dede")
                })
            .let(this::bindDisposable)
    }

    // from actions bottom sheet dialog
    fun onCopyAccountLoginAction(accountId: Long) {
        getAccountDataAndStartAction(accountId) {
            dataBufferManager.copyStringData(it.getAccountLogin())
            shortTimeMessageLiveData.value = resourceManager.getString(R.string.copied_to_clipboard_message)
        }
    }

    fun onCopyAccountPasswordAction(accountId: Long) {
        getAccountDataAndStartAction(accountId) {
            dataBufferManager.copyStringData(it.getAccountPassword())
            shortTimeMessageLiveData.value = resourceManager.getString(R.string.copied_to_clipboard_message)
        }
    }

    fun onEditAccountAction(accountId: Long) {
        showEditAccountScreenLD.value = accountId
    }

    fun onDeleteAccountAction(accountId: Long) {
        accountsInteractor.deleteAccountById(accountId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                },
                { throwable ->
                    shortTimeMessageLiveData.value = throwable.message ?: "unknown"
                })
            .let(this::bindDisposable)
    }

    private inline fun getAccountDataAndStartAction(accountId: Long, crossinline action: (entity: AccountFeatureEntity) -> Unit) {
        accountsInteractor.getAccountById(accountId)
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

    private fun handleListForDisplay(listForDisplay: List<AccountItemModel>) {
        toolbarScrollingStateLD.value = listForDisplay.size >= AppConstants.TOOLBAR_SCROLL_MIN_PASSWORDS_SIZE
    }

    private fun convertFeatureEntitiesList(entitiesList: List<AccountFeatureEntity>): List<AccountItemModel> {
        val resultList = arrayListOf<AccountItemModel>()
        entitiesList.forEach {
            resultList.add(
                AccountItemModel(
                    it.getAccountId()!!,
                    it.getAccountName(),
                    it.getAccountLogin(),
                    it.getAccountPassword()
                )
            )
        }
        return resultList
    }
}