package com.ak.feature_tabaccounts_impl.screens.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.ak.feature_tabaccounts_impl.R

class AccountsListRecyclerAdapter(
    private val onShowAccountItemActions: (passwordId: Long) -> Unit,
    private val onAccountItemSingleClick: (passwordId: Long) -> Unit,
    private val onAccountItemLongClick: (passwordId: Long) -> Unit
) : RecyclerView.Adapter<AccountsListItemViewHolder>() {

    private var itemsList = arrayListOf<AccountItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsListItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.accounts_item_layout, parent, false)
        return AccountsListItemViewHolder(
                view,
                onShowAccountItemActions,
                onAccountItemSingleClick,
                onAccountItemLongClick
        )
    }

    override fun getItemCount() = itemsList.size

    override fun onBindViewHolder(viewHolder: AccountsListItemViewHolder, position: Int) {
        viewHolder.bindAccountListItemView(itemsList[position])
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
            itemModel.isInActionModeState = isInActionMode
            changeAccountItem(itemModel, i)
        }
    }

    fun setSelectedStateForAccountItemId(isSelected: Boolean, accountId: Long) {
        val position = itemsList.indexOf(AccountItemModel.getSearchingTempModel(accountId))
        itemsList.find { it.accountId == accountId }
            ?.let {
                it.isItemSelected = isSelected
                changeAccountItem(it, position)
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