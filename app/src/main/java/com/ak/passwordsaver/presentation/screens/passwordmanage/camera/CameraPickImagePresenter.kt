package com.ak.passwordsaver.presentation.screens.passwordmanage.camera

import android.graphics.Bitmap
import com.ak.passwordsaver.PSApplication
import com.ak.passwordsaver.data.model.internalstorage.IPSInternalStorageManager
import com.ak.passwordsaver.presentation.base.BasePSPresenter
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class CameraPickImagePresenter : BasePSPresenter<ICameraPickImageView>() {

    @Inject
    lateinit var mPSInternalStorageManager: IPSInternalStorageManager

    private var mPickedImageBitmap: Bitmap? = null

    init {
        PSApplication.appInstance.getApplicationComponent().inject(this)
    }

    fun onImagePicked(bitmap: Bitmap) {
        mPickedImageBitmap = bitmap
        viewState.displayPreviewImageStrategy(mPickedImageBitmap!!)
    }

    fun onPickedImageRemoved() {
        mPickedImageBitmap = null
        viewState.displayTakeImageStrategy()
    }

    fun savePickedImageAndFinish() {
        if (mPickedImageBitmap == null) {
            return
        }

        Single.fromCallable { mPSInternalStorageManager.saveBitmapImage(mPickedImageBitmap!!) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { path ->
                    val resultPath = if (path.isNullOrEmpty()) "" else path
                    viewState.sendSuccessImagePickResult(resultPath)
                },
                { throwable ->
                    viewState.showShortTimeMessage("aaa, blyaaat'")
                }
            )
            .let(this::bindDisposable)
    }
}