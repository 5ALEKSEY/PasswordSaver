package com.ak.feature_tabaccounts_impl.screens.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewAdapter
import com.ak.feature_tabaccounts_impl.R

class AccountsListRecyclerAdapter(
    private val listener: AccountListClickListener
) : CustomThemeRecyclerViewAdapter<AccountsListItemViewHolder>() {

    private var itemsList = arrayListOf<AccountItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsListItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.accounts_item_layout, parent, false)
        return AccountsListItemViewHolder(view, listener)
    }

    override fun getItemCount() = itemsList.size

    override fun onBindViewHolder(theme: CustomTheme, viewHolder: AccountsListItemViewHolder, position: Int) {
        viewHolder.bindAccountListItemView(itemsList[position], theme)
    }

    fun insertData(accountModels: List<AccountItemModel>) {
        val diffCallback = AccountsDiffUtilCallback(itemsList, accountModels)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemsList.clear()
        itemsList.addAll(accountModels)
        diffResult.dispatchUpdatesTo(getListUpdateHandler())
        diffResult.dispatchUpdatesTo(this)
    }

    fun setItemsActionModeState(isInActionMode: Boolean) {
        for (i in 0 until itemsList.size) {
            val itemModel = itemsList[i]
            changeAccountItem(itemModel.copy(isInActionModeState = isInActionMode), i)
        }
    }

    fun setSelectedStateForAccountItemId(isSelected: Boolean, accountId: Long) {
        val position = itemsList.indexOf(AccountItemModel.getSearchingTempModel(accountId))
        itemsList.find { it.accountId == accountId }
            ?.let {
                changeAccountItem(it.copy(isItemSelected = isSelected), position)
            }
    }

    fun setAccountContentVisibility(accountId: Long, isContentVisible: Boolean) {
        val index = itemsList.indexOf(AccountItemModel.getSearchingTempModel(accountId))
        itemsList.find { accountItemModel -> accountItemModel.accountId == accountId }
            ?.let {
                changeAccountItem(it.copy(isAccountContentVisible = isContentVisible), index)
            }
    }

    fun clearContextMenuOpenedForAccountItems() {
        for (i in 0 until itemsList.size) {
            val itemModel = itemsList[i]
            changeAccountItem(itemModel.copy(isLoadingModel = false), i)
        }
    }

    fun setContextMenuOpenedForAccountItem(accountId: Long) {
        val position = itemsList.indexOf(AccountItemModel.getSearchingTempModel(accountId))
        itemsList.find { accountItemModel -> accountItemModel.accountId == accountId }
            ?.let {
                changeAccountItem(it.copy(isLoadingModel = true), position)
            }
    }

    private fun changeAccountItem(newAccountItemModel: AccountItemModel, position: Int) {
        if (position >= 0) {
            itemsList[position] = newAccountItemModel
            notifyItemChanged(position)
        }
    }

    private fun getListUpdateHandler(): ListUpdateCallback {
        return object : ListUpdateCallback {
            override fun onChanged(p0: Int, p1: Int, p2: Any?) {
                Log.d("TEMPTAG", "onChanged p0=$p0, p1=$p1")
            }

            override fun onMoved(p0: Int, p1: Int) {
                Log.d("TEMPTAG", "onMoved p0=$p0, p1=$p1")
            }

            override fun onInserted(p0: Int, p1: Int) {
                Log.d("TEMPTAG", "onInserted p0=$p0, p1=$p1")
            }

            override fun onRemoved(p0: Int, p1: Int) {
                Log.d("TEMPTAG", "onRemoved p0=$p0, p1=$p1")
            }
        }
    }

    class AccountsDiffUtilCallback(
        private val oldList: List<AccountItemModel>,
        private val newList: List<AccountItemModel>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].isTheSameContent(newList[newItemPosition])
    }
}