package com.ak.feature_tabsettings_impl.design.configurenative

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.app_theme.theme.CustomTheme
import com.ak.app_theme.theme.CustomThemeManager
import com.ak.app_theme.theme.CustomThemePreferencesMng
import com.ak.app_theme.theme.applier.ComplexViewsApplier
import com.ak.app_theme.theme.applier.CustomThemeApplier
import com.ak.app_theme.theme.uicomponents.BaseThemeDialogFragment
import com.ak.feature_tabsettings_impl.R
import kotlinx.android.synthetic.main.layout_configure_native_theme_dialog.view.llConfigureNativeThemeDialogRoot
import kotlinx.android.synthetic.main.layout_configure_native_theme_dialog.view.rvConfigureNativeThemeDarkDescriptionsList
import kotlinx.android.synthetic.main.layout_configure_native_theme_dialog.view.rvConfigureNativeThemeLightDescriptionsList
import kotlinx.android.synthetic.main.layout_configure_native_theme_dialog.view.tvConfigureNativeThemeApplyAction
import kotlinx.android.synthetic.main.layout_configure_native_theme_dialog.view.tvConfigureNativeThemeCancelAction
import kotlinx.android.synthetic.main.layout_configure_native_theme_dialog.view.tvConfigureNativeThemeDarkDescriptionTitle
import kotlinx.android.synthetic.main.layout_configure_native_theme_dialog.view.tvConfigureNativeThemeDialogTitle
import kotlinx.android.synthetic.main.layout_configure_native_theme_dialog.view.tvConfigureNativeThemeLightDescriptionTitle

class ConfigureNativeThemeDialog : BaseThemeDialogFragment() {

    interface Listener {
        fun onConfigApplied(lightThemeId: Int, darkThemeId: Int)
        fun onConfigCancelled()
    }

    var listener: Listener? = null

    private var root: View? = null
    private var dialogTitle: TextView? = null
    private var lightThemesDescriptionsListTitle: TextView? = null
    private var darkThemesDescriptionsListTitle: TextView? = null
    private var cancelActionText: TextView? = null
    private var applyActionText: TextView? = null
    private var lightThemesDescriptionsList: RecyclerView? = null
    private var darkThemesDescriptionsList: RecyclerView? = null

    override fun applyTheme(theme: CustomTheme) {
        super.applyTheme(theme)
        CustomThemeApplier.applyBackgroundTint(theme, root, R.attr.themedPopupBackgroundColor)

        CustomThemeApplier.applyBackgroundTint(theme, lightThemesDescriptionsList, R.attr.themedPopupBackgroundColor)
        CustomThemeApplier.applyBackgroundTint(theme, darkThemesDescriptionsList, R.attr.themedPopupBackgroundColor)
        ComplexViewsApplier.applyForRecyclerView(lightThemesDescriptionsList, theme)
        ComplexViewsApplier.applyForRecyclerView(darkThemesDescriptionsList, theme)

        CustomThemeApplier.applyTextColor(
            theme,
            R.attr.themedPrimaryTextColor,
            dialogTitle,
            lightThemesDescriptionsListTitle,
            darkThemesDescriptionsListTitle,
            cancelActionText
        )
        CustomThemeApplier.applyTextColor(theme, applyActionText, R.attr.themedAccentColor)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setWindowAnimations(R.style.popup_animation_medium)
        }

        return inflater.inflate(R.layout.layout_configure_native_theme_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (listener == null) {
            dismissAllowingStateLoss()
            return
        }

        // Bind usual views for theme applying
        root = view.llConfigureNativeThemeDialogRoot
        dialogTitle = view.tvConfigureNativeThemeDialogTitle
        lightThemesDescriptionsListTitle = view.tvConfigureNativeThemeLightDescriptionTitle
        darkThemesDescriptionsListTitle = view.tvConfigureNativeThemeDarkDescriptionTitle


        // Bind and configure themes description adapter
        val groupedThemesDescriptionsByLight = CustomThemeManager.getInstance()
            .getAvailableThemes()
            .filter { it.id != CustomThemeManager.NATIVE_THEME_ID }
            .groupBy { it.isLight }

        val lightThemesDescriptionsAdapter = ConfigureNativeThemeDescriptionsAdapter(
            CustomThemePreferencesMng.getNativeLightThemeId(),
            groupedThemesDescriptionsByLight[true] ?: return
        )
        lightThemesDescriptionsList = view.rvConfigureNativeThemeLightDescriptionsList.apply {
            adapter = lightThemesDescriptionsAdapter
        }

        val darkThemesDescriptionsAdapter = ConfigureNativeThemeDescriptionsAdapter(
            CustomThemePreferencesMng.getNativeDarkThemeId(),
            groupedThemesDescriptionsByLight[false] ?: return
        )
        darkThemesDescriptionsList = view.rvConfigureNativeThemeDarkDescriptionsList.apply {
            adapter = darkThemesDescriptionsAdapter
        }

        // Bind dialog action buttons listeners
        cancelActionText = view.tvConfigureNativeThemeCancelAction.apply {
            setOnClickListener {
                listener?.onConfigCancelled()
                dismissAllowingStateLoss()
            }
        }
        applyActionText = view.tvConfigureNativeThemeApplyAction.apply {
            setOnClickListener {
                listener?.onConfigApplied(
                    lightThemesDescriptionsAdapter.selectedDescriptionId,
                    darkThemesDescriptionsAdapter.selectedDescriptionId,
                )
                dismissAllowingStateLoss()
            }
        }
    }

    companion object {
        private const val DIALOG_TAG = "ConfNativeThemeDialog"

        fun show(
            fragmentManager: FragmentManager,
            listener: Listener,
        ): ConfigureNativeThemeDialog {
            return ConfigureNativeThemeDialog().apply {
                this.listener = listener
                show(fragmentManager, DIALOG_TAG)
            }
        }
    }
}