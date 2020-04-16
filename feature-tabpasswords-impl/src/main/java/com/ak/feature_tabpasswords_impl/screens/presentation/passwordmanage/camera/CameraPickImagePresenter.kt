package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera

import android.graphics.Bitmap
import android.util.Log
import com.ak.base.presenter.BasePSPresenter
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.feature_tabpasswords_impl.di.FeatureTabPasswordsComponent
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
        FeatureTabPasswordsComponent.get().inject(this)
    }

    fun onImagePicked(bitmap: Bitmap) {
        Log.d("aaaa", "aaaaa")
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