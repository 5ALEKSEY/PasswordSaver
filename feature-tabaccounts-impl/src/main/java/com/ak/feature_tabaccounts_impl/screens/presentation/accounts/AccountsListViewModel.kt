package com.ak.feature_tabaccounts_impl.screens.presentation.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ak.base.constants.AppConstants
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IDateAndTimeManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabaccounts_api.interfaces.AccountFeatureEntity
import com.ak.feature_tabaccounts_api.interfaces.IAccountsInteractor
import com.ak.feature_tabaccounts_impl.R
import com.ak.feature_tabaccounts_impl.screens.adapter.AccountItemModel
import com.ak.feature_tabaccounts_impl.screens.logic.IDataBufferManager
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountsListViewModel @Inject constructor(
    private val accountsInteractor: IAccountsInteractor,
    private val dataBufferManager: IDataBufferManager,
    private val resourceManager: IResourceManager,
    private val dateAndTimeManager: IDateAndTimeManager,
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

    private var loadAccountsJob: Job? = null

    fun loadPasswords() {
        loadingStateLD.value = true
        emptyAccountsStateLD.value = false

        loadAccountsJob?.cancel()
        loadAccountsJob = viewModelScope.launch(Dispatchers.IO) {
            accountsInteractor.getAllAccounts().collect { list ->
                val listForDisplay = convertFeatureEntitiesList(list)
                withContext(Dispatchers.Main) {
                    loadingStateLD.value = false
                    accountsListLD.value = listForDisplay
                    emptyAccountsStateLD.value = list.isEmpty()
                    handleListForDisplay(listForDisplay)
                }
            }
        }
    }

    // from actions bottom sheet dialog
    fun onCopyAccountLoginAction(accountId: Long) {
        viewModelScope.launch {
            val accountEntity = accountsInteractor.getAccountById(accountId)
            dataBufferManager.copyStringData(accountEntity.getAccountLogin())
            shortTimeMessageLiveData.value = resourceManager.getString(R.string.copied_to_clipboard_message)
        }
    }

    fun onCopyAccountPasswordAction(accountId: Long) {
        viewModelScope.launch {
            val accountEntity = accountsInteractor.getAccountById(accountId)
            dataBufferManager.copyStringData(accountEntity.getAccountPassword())
            shortTimeMessageLiveData.value = resourceManager.getString(R.string.copied_to_clipboard_message)
        }
    }

    fun onEditAccountAction(accountId: Long) {
        showEditAccountScreenLD.value = accountId
    }

    fun onDeleteAccountAction(accountId: Long) {
        viewModelScope.launch {
            accountsInteractor.deleteAccountById(accountId)
        }
    }

    fun pinAccount(accountId: Long) {
        viewModelScope.launch {
            accountsInteractor.pinAccount(accountId, dateAndTimeManager.getCurrentTimeInMillis())
        }
    }

    fun unpinAccount(accountId: Long) {
        viewModelScope.launch {
            accountsInteractor.unpinAccount(accountId)
        }
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
                    it.getAccountPassword(),
                    it.getAccountPinTimestamp() != null,
                )
            )
        }
        return resultList
    }
}