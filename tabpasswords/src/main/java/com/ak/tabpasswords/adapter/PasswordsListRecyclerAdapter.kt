package com.ak.tabpasswords.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.ak.tabpasswords.R

class PasswordsListRecyclerAdapter(
    private val onShowPasswordAction: (passwordId: Long, newVisibilityState: Boolean) -> Unit,
    private val onShowPasswordItemActions: (passwordId: Long) -> Unit,
    private val onPasswordItemSingleClick: (passwordId: Long) -> Unit,
    private val onPasswordItemLongClick: (passwordId: Long) -> Unit
) : RecyclerView.Adapter<PasswordsListItemViewHolder>() {

    private var mItemsList = arrayListOf<PasswordItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordsListItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.passwords_item_layout, parent, false)
        return PasswordsListItemViewHolder(
            view,
            onShowPasswordAction,
            onShowPasswordItemActions,
            onPasswordItemSingleClick,
            onPasswordItemLongClick
        )
    }

    override fun getItemCount() = mItemsList.size

    override fun onBindViewHolder(viewHolder: PasswordsListItemViewHolder, position: Int) {
        viewHolder.bindPasswordListItemView(mItemsList[position])
    }

    fun insertData(passwordModels: List<PasswordItemModel>) {
        val diffCallback =
            PasswordsDiffUtilCallback(
                mItemsList,
                passwordModels
            )
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mItemsList.clear()
        mItemsList.addAll(passwordModels)
        diffResult.dispatchUpdatesTo(getListUpdateHandler())
        diffResult.dispatchUpdatesTo(this)
    }

    fun setItemsActionModeState(isInActionMode: Boolean) {
        for (i in 0 until mItemsList.size) {
            val itemModel = mItemsList[i]
            itemModel.isInActionModeState = isInActionMode
            changePasswordItem(itemModel, i)
        }
    }

    fun setPasswordContentVisibility(passwordId: Long, isContentVisible: Boolean) {
        val index = mItemsList.indexOf(PasswordItemModel.getSearchingTempModel(passwordId))
        mItemsList.find { passwordItemModel -> passwordItemModel.passwordId == passwordId }
            ?.let {
                it.isPasswordContentVisible = isContentVisible
                changePasswordItem(it, index)
            }
    }

    fun setSelectedStateForPasswordItemId(isSelected: Boolean, passwordId: Long) {
        val position = mItemsList.indexOf(PasswordItemModel.getSearchingTempModel(passwordId))
        mItemsList.find { passwordItemModel -> passwordItemModel.passwordId == passwordId }
            ?.let {
                it.isItemSelected = isSelected
                changePasswordItem(it, position)
            }
    }

    private fun changePasswordItem(newPasswordItemModel: PasswordItemModel, position: Int) {
        if (position >= 0) {
            mItemsList[position] = newPasswordItemModel
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

    class PasswordsDiffUtilCallback(
        private val oldList: List<PasswordItemModel>,
        private val newList: List<PasswordItemModel>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].isTheSameContent(newList[newItemPosition])
    }
}