package com.example.feature_customthememanager_impl.di

import androidx.lifecycle.ViewModel
import com.ak.base.viewmodel.BaseViewModelFactory
import com.ak.base.viewmodel.IViewModelAssistedFactory
import com.ak.core_repo_api.intefaces.theme.ICustomUserThemesRepository
import com.example.feature_customthememanager_impl.managetheme.ManageCustomThemeViewModel
import com.example.feature_customthememanager_impl.managetheme.mapper.ICustomThemeMapper
import com.example.feature_customthememanager_impl.managetheme.simplemodifhelper.ISimpleModificationsHelper
import javax.inject.Inject

class CustomThemeManagerFactoryComposite @Inject constructor(
    viewModels: MutableMap<Class<out ViewModel>, IViewModelAssistedFactory<out ViewModel>>,
) : BaseViewModelFactory(viewModels)

class ManageCustomThemeViewModelAssistedFactory @Inject constructor(
    private val mapper: ICustomThemeMapper,
    private val themesRepository: ICustomUserThemesRepository,
    private val simpleModificationsHelper: ISimpleModificationsHelper,
) : IViewModelAssistedFactory<ManageCustomThemeViewModel> {
    override fun create(): ManageCustomThemeViewModel {
        return ManageCustomThemeViewModel(mapper, themesRepository, simpleModificationsHelper)
    }
}