package com.ak.passwordsaver.presentation.screens.addnew.camera.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView
import com.ak.passwordsaver.presentation.base.managers.bitmapdecoder.IBitmapDecoderManager
import com.ak.passwordsaver.presentation.screens.addnew.camera.CameraCalculatorHelper
import javax.inject.Inject


class PSCameraManagerImpl @Inject constructor(
    val context: Context,
    val cameraManager: CameraManager,
    val bitmapDecoderManager: IBitmapDecoderManager
) : IPSCameraManager {

    private var mOnImageCreatedListener: ((imageBitmap: Bitmap) -> Unit)? = null

    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null

    private var mCameraDevice: CameraDevice? = null
    private lateinit var mPreviewSurface: Surface
    private var mCaptureSession: CameraCaptureSession? = null
    private var mImageReader: ImageReader? = null

    private var mCurrentCameraId: String = ""
    private var mFacingBackCameraId: String = ""
    private var mFacingFrontCameraId: String = ""

    private var mIsPreviewOnly: Boolean = true
    private var mPreviewImageView: TextureView? = null
    private var mIsManagerInitialized = false

    private val mOnImageAvailableListener =
        ImageReader.OnImageAvailableListener {
            val image = it.acquireNextImage()
            val bitmap = bitmapDecoderManager.decodeBitmap(image)
            if (bitmap == null) {
                Log.d("dd", "ded")
                return@OnImageAvailableListener
            }
            mOnImageCreatedListener?.invoke(bitmap)
            image.close()
        }

    override fun initCameraManager(isPreviewOnly: Boolean, previewImageView: TextureView) {
        this.mIsPreviewOnly = isPreviewOnly
        this.mPreviewImageView = previewImageView
        try {
            cameraManager.cameraIdList.forEach {
                val characteristics = cameraManager.getCameraCharacteristics(it)
                when (characteristics.get(CameraCharacteristics.LENS_FACING)) {
                    CameraCharacteristics.LENS_FACING_BACK -> mFacingBackCameraId = it
                    CameraCharacteristics.LENS_FACING_FRONT -> mFacingFrontCameraId = it
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        mIsManagerInitialized = true
    }

    override fun openCamera() {
        if (!mIsManagerInitialized) {
            return
        }

        if (mCurrentCameraId.isNotEmpty()) {
            openCamera(mCurrentCameraId)
        } else {
            openCamera(mFacingBackCameraId)
            mCurrentCameraId = mFacingBackCameraId
        }
    }

    override fun closeCamera() {
        if (!mIsManagerInitialized) {
            return
        }

        stopBackgroundThread()
        mCameraDevice?.close()
        mCameraDevice = null
        mImageReader = null
        mCaptureSession = null
        mPreviewSurface.release()
    }

    override fun switchCamera() {
        if (!mIsManagerInitialized) {
            return
        }

        val cameraIdForOpen = when {
            mCurrentCameraId.equals(mFacingBackCameraId, true) -> {
                mFacingFrontCameraId
            }
            mCurrentCameraId.equals(mFacingFrontCameraId, true) -> {
                mFacingBackCameraId
            }
            else -> mFacingBackCameraId
        }
        mCurrentCameraId = cameraIdForOpen
        closeCamera()
        openCamera(mCurrentCameraId)
    }

    override fun isFacingBackCameraExist() = mFacingBackCameraId.isNotEmpty()

    override fun isFacingFrontCameraExist() = mFacingFrontCameraId.isNotEmpty()

    override fun takeImage(onImageCreatedListener: (imageBitmap: Bitmap) -> Unit) {
        mOnImageCreatedListener = onImageCreatedListener
        if (!mIsManagerInitialized) {
            return
        }

        if (mIsPreviewOnly || mImageReader == null || mCaptureSession == null) {
            return
        }
        try {

            val builder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            builder.addTarget(mImageReader!!.surface)
            mCaptureSession!!.stopRepeating()
            mCaptureSession!!.abortCaptures()
            mCaptureSession!!.capture(
                builder.build(),
                null,
                mBackgroundHandler
            )


        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun openCamera(cameraId: String) {
        startBackgroundThread()
        try {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                    override fun onOpened(cameraDevice: CameraDevice) {
                        mCameraDevice = cameraDevice
                        setupCameraPreview()
                    }

                    override fun onClosed(camera: CameraDevice) {
                        super.onClosed(camera)
                        Log.d("camera_testing", "onClosed")
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        Log.d("camera_testing", "onDisconnected")
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        Log.d("camera_testing", "onError")
                    }
                }, mBackgroundHandler)

            }
        } catch (e: CameraAccessException) {

        }
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("CameraBackground").apply {
            start()
            mBackgroundHandler = Handler(looper)
        }
    }

    private fun stopBackgroundThread() {
        try {
            mBackgroundThread?.apply {
                quitSafely()
                join()
                mBackgroundHandler = null
            }
        } catch (e: InterruptedException) {

        } finally {
            mBackgroundThread = null
        }
    }

    private fun setupCameraPreview() {
        if (mCameraDevice == null || mPreviewImageView == null) {
            return
        }

        if (mPreviewImageView!!.isAvailable) {
            val surfaceTexture = mPreviewImageView!!.surfaceTexture
            val optimalPreviewSize =
                CameraCalculatorHelper.getBestCameraPhotoPreviewSize(
                    context,
                    mCurrentCameraId,
                    cameraManager,
                    mPreviewImageView!!.width, mPreviewImageView!!.height
                )
            createCameraPreviewSession(
                surfaceTexture,
                optimalPreviewSize.width,
                optimalPreviewSize.height
            )
        } else {
            mPreviewImageView!!.surfaceTextureListener =
                object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, w: Int, h: Int) {
                    Log.d("d", "d")
                }

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                    Log.d("d", "ss")
                }

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                    Log.d("d", "ss")
                    return true
                }

                override fun onSurfaceTextureAvailable(
                    surface: SurfaceTexture?,
                    width: Int,
                    height: Int
                ) {
                    if (surface == null) return

                    val optimalPreviewSize =
                        CameraCalculatorHelper.getBestCameraPhotoPreviewSize(
                            context,
                            mCurrentCameraId,
                            cameraManager,
                            width,
                            height
                        )

                    createCameraPreviewSession(
                        surface,
                        optimalPreviewSize.width,
                        optimalPreviewSize.height
                    )
                }
            }
        }
    }

    private fun createCameraPreviewSession(surfaceTexture: SurfaceTexture, w: Int, h: Int) {
        surfaceTexture.setDefaultBufferSize(w, h)
        mPreviewSurface = Surface(surfaceTexture)
        try {
            val sessionSurfaces = arrayListOf<Surface>()
            sessionSurfaces.add(mPreviewSurface)
            if (!mIsPreviewOnly) {
                val imageSize =
                    CameraCalculatorHelper.getBestPhotoSize(
                        context,
                        mCurrentCameraId,
                        cameraManager,
                        w,
                        h
                    )
                mImageReader = ImageReader.newInstance(
                    imageSize.width,
                    imageSize.height,
                    ImageFormat.JPEG,
                    1
                )
                mImageReader!!.setOnImageAvailableListener(
                    mOnImageAvailableListener,
                    mBackgroundHandler
                )
                sessionSurfaces.add(mImageReader!!.surface)
            }
            mCameraDevice!!.createCaptureSession(
                sessionSurfaces,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigureFailed(session: CameraCaptureSession) {

                    }

                    override fun onConfigured(session: CameraCaptureSession) {
                        mCaptureSession = session
                        val builder =
                            mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                        builder.addTarget(mPreviewSurface)
                        mCaptureSession!!.setRepeatingRequest(
                            builder.build(),
                            null,
                            mBackgroundHandler
                        )
                    }
                },
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
}