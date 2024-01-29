package com.example.feature_customthememanager_impl.managetheme

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ak.app_theme.R
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeColorAttr
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.app_theme.theme.CustomThemeManager.Companion.BLUE_THEME_ID
import com.ak.base.extensions.hexToIntColor
import com.ak.base.livedata.SingleEventLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.theme.CustomUserThemeRepoEntity
import com.ak.core_repo_api.intefaces.theme.ICustomUserThemesRepository
import com.example.feature_customthememanager_impl.managetheme.adapter.ColorModificationItemModel
import com.example.feature_customthememanager_impl.managetheme.adapter.ModificationItemModel
import com.example.feature_customthememanager_impl.managetheme.mapper.ICustomThemeMapper
import com.example.feature_customthememanager_impl.managetheme.simplemodifhelper.ISimpleModificationsHelper
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManageCustomThemeViewModel @Inject constructor(
    private val mapper: ICustomThemeMapper,
    private val themesRepository: ICustomUserThemesRepository,
    private val simpleModificationsHelper: ISimpleModificationsHelper,
): BasePSViewModel() {

    private val modificationsList = mutableListOf<ModificationItemModel>()

    private var themeIdToEdit: Long? = null
    private var simpleManageEnabled = true

    private val modificationsListLiveData = MutableLiveData<List<ModificationItemModel>>()
    private val lightThemeStateLiveData = MutableLiveData<Boolean>()
    private val modificationNameLiveData = MutableLiveData<String>()
    private val leaveScreenLiveData = SingleEventLiveData<Unit?>()

    fun subscribeToModificationsList(): LiveData<List<ModificationItemModel>> = modificationsListLiveData
    fun subscribeToLightThemeState(): LiveData<Boolean> = lightThemeStateLiveData
    fun subscribeToModificationsName(): LiveData<String> = modificationNameLiveData
    fun subscribeToLeaveScreen(): LiveData<Unit?> = leaveScreenLiveData

    fun obtainModificationsList(customThemeId: Long?, context: Context) {
        viewModelScope.launch {
            if (customThemeId != null && customThemeId >= 0) {
                prepareForEdit(customThemeId, context)
            } else {
                prepareForAdd()
            }
        }
    }

    private suspend fun prepareForEdit(
        customThemeId: Long,
        context: Context,
    ) = withContext(Dispatchers.IO) {
        themeIdToEdit = customThemeId
        val themeForModification = themesRepository.getThemeById(customThemeId)?.toCustomTheme(context)
        val name = (themeForModification?.name as? CustomTheme.Name.StringName)?.value ?: ""
        themeForModification?.let { prepareUI(it, name) }
    }

    private suspend fun prepareForAdd() = withContext(Dispatchers.IO) {
        val themeForModification = CustomThemeManager.getInstance()
            .getAvailableThemes()
            .find { it.id == BLUE_THEME_ID }
        themeForModification?.let { prepareUI(it, "") }
    }

    private suspend fun prepareUI(
        themeForModification: CustomTheme,
        themeName: String,
    ) = withContext(Dispatchers.Main) {
        updateAndNotifyModificationsList(mapper.customThemeToModificationsList(themeForModification))
        lightThemeStateLiveData.value = themeForModification.isLight
        modificationNameLiveData.value = themeName
    }

    fun onChangeColor(itemId: Int, newColorValue: Int) {
        val oldModifications = modificationsList
        val oldModificationItem = oldModifications.find {
            it.itemId == itemId
        } as? ColorModificationItemModel ?: return
        val oldModificationItemIndex = oldModifications.indexOf(oldModificationItem)

        val newModificationItem = oldModificationItem.copy(colorIntValue = newColorValue)
        val newModifications = oldModifications.toMutableList().apply {
            set(oldModificationItemIndex, newModificationItem)
        }

        viewModelScope.launch {
            updateAndNotifyModificationsList(newModifications)
        }
    }

    fun onSaveNewThemeAndLeave(
        themeName: String,
        isLightTheme: Boolean,
    ) {
        viewModelScope.launch {
            val modificationsToSave = if (simpleManageEnabled) {
                simpleModificationsHelper.createFullModification(modificationsList, isLightTheme)
            } else {
                modificationsList
            }
            val themeIdToSave = themeIdToEdit ?: themesRepository.getNextThemeId()
            val customTheme = mapper.modificationsToRepoEntity(
                themeId = themeIdToSave,
                themeName = themeName.ifBlank { "Custom Theme" },
                isLightTheme = isLightTheme,
                modifications = modificationsToSave,
            )
            themesRepository.addNewThemes(listOf(customTheme))
            leaveScreenLiveData.call()
        }
    }

    fun onSimpleManageStateChanged(isEnabled: Boolean) {
        simpleManageEnabled = isEnabled
        viewModelScope.launch {
            updateAndNotifyModificationsList(ArrayList(modificationsList))
        }
    }

    private suspend fun updateAndNotifyModificationsList(
        newModifications: List<ModificationItemModel>,
    ) = withContext(Dispatchers.IO) {
        modificationsList.clear()
        modificationsList.addAll(newModifications)

        val modificationsToNotify = if (simpleManageEnabled) {
            val customColorAttrs = simpleModificationsHelper.getSimpleModificationColorAttrs()
            modificationsList.filter {
                if (it is ColorModificationItemModel) {
                    customColorAttrs.contains(it.attrCustomId)
                } else {
                    true
                }
            }
        } else {
            modificationsList
        }

        modificationsListLiveData.postValue(modificationsToNotify)
    }

    private fun CustomUserThemeRepoEntity.toCustomTheme(context: Context): CustomTheme {
        val themeStyle = if (isLight()) {
            R.style.CustomTheme_Blue
        } else {
            R.style.CustomTheme_Orange
        }
        val builder = CustomTheme.Builder(context)
            .id(getThemeId().toInt())
            .name(getName())
            .themeStyle(themeStyle)
            .lightThemeFlag(isLight())

        getColorAttrs().forEach { customColorAttr ->
            builder.overrideColorAttr(
                CustomThemeColorAttr.customIdToAndroid(customColorAttr.attrCustomId),
                customColorAttr.colorHexValue.hexToIntColor(),
            )
        }

        return builder.build()
    }
}