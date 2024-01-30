package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ak.base.viewmodel.BasePSViewModel
import com.ak.core_repo_api.intefaces.IPSInternalStorageManager
import com.ak.core_repo_api.intefaces.IResourceManager
import com.ak.feature_tabpasswords_impl.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CameraPickImageViewModel @Inject constructor(
    private val internalStorageManager: IPSInternalStorageManager,
    private val resourceManager: IResourceManager
) : BasePSViewModel() {

    private var mPickedImageBitmap: Bitmap? = null

    private val previewImageLiveData = MutableLiveData<Bitmap>()
    private val takeImageLiveData = MutableLiveData<Unit?>()
    private val imagePickedResultPathLiveData = MutableLiveData<String>()

    fun subscribeToPreviewImageLiveData(): LiveData<Bitmap> = previewImageLiveData
    fun subscribeToTakeImageLiveData(): LiveData<Unit?> = takeImageLiveData
    fun subscribeToImagePickedResultPathLiveData(): LiveData<String> = imagePickedResultPathLiveData

    fun onImagePicked(bitmap: Bitmap) {
        mPickedImageBitmap = bitmap.also {
            previewImageLiveData.value = it
        }
    }

    fun onPickedImageRemoved() {
        mPickedImageBitmap = null
        takeImageLiveData.value = Unit
    }

    fun savePickedImageAndFinish() {
        if (mPickedImageBitmap == null) {
            return
        }

        Single.fromCallable { internalStorageManager.saveBitmapImage(mPickedImageBitmap!!) ?: "" }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { path ->
                    val resultPath = if (path.isNullOrEmpty()) "" else path
                    imagePickedResultPathLiveData.value = resultPath
                },
                { throwable ->
                    shortTimeMessageLiveData.value = resourceManager.getString(R.string.unknown_error_message)
                }
            )
            .let(this::bindDisposable)
    }
}