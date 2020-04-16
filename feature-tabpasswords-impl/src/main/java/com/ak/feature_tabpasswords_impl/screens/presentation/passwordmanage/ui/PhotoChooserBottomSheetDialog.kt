package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentManager
import com.ak.base.extensions.getColorCompat
import com.ak.base.extensions.setSafeClickListener
import com.ak.feature_tabpasswords_impl.R
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.manager.IPSCameraManager
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.gallery.manager.IPSGalleryManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_photo_chooser_dialog.view.*
import javax.inject.Inject

class PhotoChooserBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        private const val BOTTOM_SHEET_DIALOG_TAG = "PhotoChooserBottomSheetDialog"
        const val CAMERA_CHOOSE_ACTION_ID = 1
        const val GALLERY_CHOOSE_ACTION_ID = 2

        fun showDialog(fragmentManager: FragmentManager): PhotoChooserBottomSheetDialog {
            val sheetDialogInstance = PhotoChooserBottomSheetDialog()
            sheetDialogInstance.show(fragmentManager, BOTTOM_SHEET_DIALOG_TAG)
            return sheetDialogInstance
        }
    }

    @Inject
    lateinit var mPSCameraManager: IPSCameraManager
    @Inject
    lateinit var mPSGalleryManager: IPSGalleryManager

    lateinit var onChooseAvatarActionListener: (chooseActionId: Int) -> Unit

    private var mFragmentView: View? = null
    private lateinit var mCameraPreviewView: TextureView

    override fun getTheme() = R.style.PhotoChooserBottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeatureTabPasswordsComponent.get().inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentView = inflater.inflate(R.layout.layout_photo_chooser_dialog, container, false)
        return mFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCameraPreviewView = view.findViewById(R.id.texvPhotoChooserCameraPreview)
        mPSCameraManager.initCameraManager(true, mCameraPreviewView)
        view.ivPhotoChooserGalleryPreview.apply {
            val lastGalleryImage = mPSGalleryManager.getLastGalleryImage()
            if (lastGalleryImage != null) {
                setImageBitmap(lastGalleryImage)
            } else {
                @ColorInt
                val noLastImageColor = context.getColorCompat(R.color.colorBlack)
                setImageDrawable(ColorDrawable(noLastImageColor))
            }
        }
        view.vChooseCameraAction.setSafeClickListener {
            if (this::onChooseAvatarActionListener.isInitialized) {
                onChooseAvatarActionListener.invoke(CAMERA_CHOOSE_ACTION_ID)
            }
        }
        view.vChooseGalleryAction.setSafeClickListener {
            if (this::onChooseAvatarActionListener.isInitialized) {
                onChooseAvatarActionListener.invoke(GALLERY_CHOOSE_ACTION_ID)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::mPSCameraManager.isInitialized) {
            mPSCameraManager.openCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::mPSCameraManager.isInitialized) {
            mPSCameraManager.closeCamera()
        }
    }
}