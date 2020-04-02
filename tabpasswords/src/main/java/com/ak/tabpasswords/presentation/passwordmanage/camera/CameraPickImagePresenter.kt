package com.ak.tabpasswords.presentation.passwordmanage.camera

import android.graphics.Bitmap
import com.ak.base.presenter.BasePSPresenter
import com.ak.domain.data.model.internalstorage.IPSInternalStorageManager
import com.ak.tabpasswords.di.PasswordsComponentProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class CameraPickImagePresenter @Inject constructor(
    private val internalStorageManager: IPSInternalStorageManager
) : BasePSPresenter<ICameraPickImageView>() {

    private var mPickedImageBitmap: Bitmap? = null

    init {
        (applicationContext as PasswordsComponentProvider)
            .providePasswordsComponent()
            .inject(this)
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

        Single.fromCallable { internalStorageManager.saveBitmapImage(mPickedImageBitmap!!) }
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