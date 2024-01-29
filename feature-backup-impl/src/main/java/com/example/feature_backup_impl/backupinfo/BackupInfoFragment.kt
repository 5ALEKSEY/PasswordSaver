package com.example.feature_backup_impl.backupinfo

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ak.base.constants.AppConstants
import com.ak.base.navigation.NavDeepLinkDestination
import com.ak.base.ui.dialog.PSDialog
import com.ak.base.ui.dialog.PSDialogBuilder
import com.ak.base.ui.recycler.decorator.PsDividerItemDecoration
import com.ak.base.ui.recycler.decorator.PsDividerItemDecorationSettings
import com.ak.base.viewmodel.injectViewModel
import com.ak.feature_backup_impl.R
import com.example.feature_backup_impl.backupinfo.adapter.BackupInfoRecyclerViewAdapter
import com.example.feature_backup_impl.backupinfo.adapter.BackupListItemModel
import com.example.feature_backup_impl.base.BaseBackupModuleFragment
import com.example.feature_backup_impl.di.FeatureBackupComponent
import com.example.feature_backup_impl.fileshare.IShareFileManager
import javax.inject.Inject

class BackupInfoFragment : BaseBackupModuleFragment<BackupInfoViewModel>() {

    private var lNoBackupState: View? = null
    private var lNoDataToBackupState: View? = null
    private var rvBackupItemsList: RecyclerView? = null
    private var btnCreateBackupAction: Button? = null
    private var btnNoBackupImportFile: Button? = null
    private var btnNoBackupImportWithGAccount: Button? = null
    private var btnAddNewAccount: Button? = null
    private var btnAddNewPassword: Button? = null
    private var btnNoDataToBackupImportFile: Button? = null
    private var btnNoDataToBackupImportWithGAccount: Button? = null

    private var backListItemsAdapter: BackupInfoRecyclerViewAdapter? = null

    @Inject
    protected lateinit var shareFileMng: IShareFileManager

    private var pickBackupFileDialog: PSDialog? = null

    override fun getFragmentLayoutResId() = R.layout.fragment_backup_info

    override fun createViewModel(): BackupInfoViewModel {
        return injectViewModel(viewModelsFactory)
    }

    override fun injectFragment(component: FeatureBackupComponent) {
        component.inject(this)
    }

    override fun findViews(fragmentView: View) {
        super.findViews(fragmentView)
        with(fragmentView) {
            lNoBackupState = findViewById(R.id.lNoBackupState)
            lNoDataToBackupState = findViewById(R.id.lNoDataToBackupState)
            rvBackupItemsList = findViewById(R.id.rvBackupItemsList)
            btnCreateBackupAction = findViewById(R.id.btnCreateBackupAction)
            btnNoBackupImportFile = findViewById(R.id.btnNoBackupImportFile)
            btnNoBackupImportWithGAccount = findViewById(R.id.btnNoBackupImportWithGAccount)
            btnAddNewAccount = findViewById(R.id.btnAddNewAccount)
            btnAddNewPassword = findViewById(R.id.btnAddNewPassword)
            btnNoDataToBackupImportFile = findViewById(R.id.btnNoDataToBackupImportFile)
            btnNoDataToBackupImportWithGAccount = findViewById(R.id.btnNoDataToBackupImportWithGAccount)
        }
    }

    override fun initView(fragmentView: View) {
        super.initView(fragmentView)
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        applyForToolbarController {
            setToolbarTitle(R.string.backup_info_toolbar_title)
            setupBackAction(R.drawable.ic_back_action) {
                navController.popBackStack()
            }
        }
    }

    override fun subscriberToViewModel(viewModel: BackupInfoViewModel) {
        super.subscriberToViewModel(viewModel)
        with(viewModel) {
            subscribeToBackupState().observe(viewLifecycleOwner) {
                when (it) {
                    is BackupListState.BackupExists -> initBackupExistsState(it.items)
                    BackupListState.NoDataToBackup -> initNoDataToBackupState()
                    BackupListState.NoBackup -> initNoBackupState()
                }
            }
            subscribeToShareBackup().observe(viewLifecycleOwner) {
                shareFileMng.shareFileExternally(requireContext(), it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        pickBackupFileDialog?.dismissAllowingStateLoss()
        viewModel.loadBackupListItems()
    }

    private fun initNoBackupState() = with(fragmentView) {
        lNoBackupState?.isVisible = true
        rvBackupItemsList?.isVisible = false
        lNoDataToBackupState?.isVisible = false
        btnCreateBackupAction?.setOnClickListener {
            viewModel.saveBackup()
        }
        btnNoBackupImportFile?.setOnClickListener {
            pickBackupFile()
        }

        // no support for now
        btnNoBackupImportWithGAccount?.isVisible = false
        btnNoBackupImportWithGAccount?.setOnClickListener {

        }
    }

    private fun initNoDataToBackupState() = with(fragmentView) {
        lNoDataToBackupState?.isVisible = true
        lNoBackupState?.isVisible = false
        rvBackupItemsList?.isVisible = false
        btnAddNewPassword?.setOnClickListener {
            navigate(NavDeepLinkDestination.AddNewPassword)
        }
        btnAddNewAccount?.setOnClickListener {
            navigate(NavDeepLinkDestination.AddNewAccount)
        }
        btnNoDataToBackupImportFile?.setOnClickListener {
            pickBackupFile()
        }

        // no support for now
        btnNoDataToBackupImportWithGAccount?.isVisible = false
        btnNoDataToBackupImportWithGAccount?.setOnClickListener {

        }
    }

    private fun initBackupExistsState(items: List<BackupListItemModel>) = with(fragmentView) {
        rvBackupItemsList?.isVisible = true
        lNoDataToBackupState?.isVisible = false
        lNoBackupState?.isVisible = false
        backListItemsAdapter?.submitNewItems(items)
    }

    private fun initRecyclerView() {
        backListItemsAdapter = BackupInfoRecyclerViewAdapter(
            viewModel::shareBackup,
            viewModel::refreshBackup,
            viewModel::onSimpleBackupActionClicked,
        )

        rvBackupItemsList?.apply {
            adapter = backListItemsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(PsDividerItemDecoration(PsDividerItemDecorationSettings(context)))
        }
    }

    private fun pickBackupFile() {
        fun runPickIntent() {
            requireActivity().startActivityFromFragment(
                this,
                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = AppConstants.BACKUP_FILE_MIME_TYPE
                },
                AppConstants.BACKUP_FILE_PICK_REQUEST_CODE
            )
        }

        pickBackupFileDialog?.dismissAllowingStateLoss()
        pickBackupFileDialog = PSDialogBuilder(childFragmentManager)
            .title(getString(R.string.dialog_attention_title))
            .description(getString(R.string.dialog_import_backup_file_description))
            .positive(getString(R.string.dialog_import_backup_file_positive_text)) {
                pickBackupFileDialog?.dismissAllowingStateLoss()
                runPickIntent()
            }
            .cancelable(false)
            .buildAndShow()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != AppConstants.BACKUP_FILE_PICK_REQUEST_CODE) {
            return
        }

        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            viewModel.importBackupWithFile(data.data!!)
        }
    }
}