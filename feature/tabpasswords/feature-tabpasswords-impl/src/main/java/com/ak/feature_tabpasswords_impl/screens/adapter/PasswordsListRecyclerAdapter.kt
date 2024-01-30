package com.ak.feature_tabpasswords_impl.screens.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.uicomponents.recyclerview.CustomThemeRecyclerViewAdapter
import com.ak.feature_tabpasswords_impl.R

class PasswordsListRecyclerAdapter(
    private val listener: PasswordsListClickListener
) : CustomThemeRecyclerViewAdapter<PasswordsListItemViewHolder>() {

    private var itemsList = arrayListOf<PasswordItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordsListItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.passwords_item_layout, parent, false)
        return PasswordsListItemViewHolder(view, listener)
    }

    override fun getItemCount() = itemsList.size

    override fun onBindViewHolder(theme: CustomTheme, viewHolder: PasswordsListItemViewHolder, position: Int) {
        viewHolder.bindPasswordListItemView(itemsList[position], theme)
    }

    fun insertData(passwordModels: List<PasswordItemModel>) {
        val diffCallback =
            PasswordsDiffUtilCallback(
                itemsList,
                passwordModels
            )
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemsList.clear()
        itemsList.addAll(passwordModels)
        diffResult.dispatchUpdatesTo(getListUpdateHandler())
        diffResult.dispatchUpdatesTo(this)
    }

    fun setItemsActionModeState(isInActionMode: Boolean) {
        for (i in 0 until itemsList.size) {
            val itemModel = itemsList[i]
            changePasswordItem(itemModel.copy(isInActionModeState = isInActionMode), i)
        }
    }

    fun clearContextMenuOpenedForPasswordItems() {
        for (i in 0 until itemsList.size) {
            val itemModel = itemsList[i]
            changePasswordItem(itemModel.copy(isLoadingModel = false), i)
        }
    }

    fun setContextMenuOpenedForPasswordItem(passwordId: Long) {
        val position = itemsList.indexOf(PasswordItemModel.getSearchingTempModel(passwordId))
        itemsList.find { passwordItemModel -> passwordItemModel.passwordId == passwordId }
            ?.let {
                changePasswordItem(it.copy(isLoadingModel = true), position)
            }
    }

    fun setSelectedStateForPasswordItemId(isSelected: Boolean, passwordId: Long) {
        val position = itemsList.indexOf(PasswordItemModel.getSearchingTempModel(passwordId))
        itemsList.find { passwordItemModel -> passwordItemModel.passwordId == passwordId }
            ?.let {
                changePasswordItem(it.copy(isItemSelected = isSelected), position)
            }
    }

    fun setPasswordContentVisibility(passwordId: Long, isContentVisible: Boolean) {
        val index = itemsList.indexOf(PasswordItemModel.getSearchingTempModel(passwordId))
        itemsList.find { passwordItemModel -> passwordItemModel.passwordId == passwordId }
            ?.let {
                changePasswordItem(it.copy(isPasswordContentVisible = isContentVisible), index)
            }
    }

    private fun changePasswordItem(newPasswordItemModel: PasswordItemModel, position: Int) {
        if (position >= 0) {
            itemsList[position] = newPasswordItemModel
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