package com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView
import com.ak.feature_tabpasswords_impl.screens.logic.IBitmapDecoderManager
import com.ak.feature_tabpasswords_impl.screens.presentation.passwordmanage.camera.CameraHelper
import javax.inject.Inject

class PSCameraManagerImpl @Inject constructor(
    private val context: Context,
    private val cameraManager: CameraManager,
    private val bitmapDecoderManager: IBitmapDecoderManager
) : IPSCameraManager {

    private var mOnImageCreatedListener: ((imageBitmap: Bitmap) -> Unit)? = null

    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null

    private var mCameraDevice: CameraDevice? = null
    private lateinit var mPreviewSurface: Surface
    private lateinit var mImageReaderSurface: Surface
    private var mCaptureSession: CameraCaptureSession? = null
    private var mImageReader: ImageReader? = null

    private var mCurrentCameraId: String = ""
    private var mFacingBackCameraId: String = ""
    private var mFacingFrontCameraId: String = ""

    private var mIsPreviewOnly: Boolean = true
    private var mPreviewImageView: TextureView? = null
    private var mIsManagerInitialized = false

    private val mIsFacingFrontCurrentCameraId
        get() = mFacingFrontCameraId == mCurrentCameraId

    private val mOnImageAvailableListener =
        ImageReader.OnImageAvailableListener {
            val image = it.acquireNextImage()
            val bitmap = bitmapDecoderManager.decodeBitmap(image)
            if (bitmap == null) {
                Log.d("dd", "ded")
                return@OnImageAvailableListener
            }

            val rotateDegree = CameraHelper.gePhotoOrientation(
                context,
                mCurrentCameraId,
                cameraManager,
                mIsFacingFrontCurrentCameraId
            )
            val rotatedBitmap = Matrix().run {
                postRotate(rotateDegree.toFloat())
                return@run Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    this,
                    true
                )
            }

            mOnImageCreatedListener?.invoke(rotatedBitmap)
            image.close()
        }

    override fun initCameraManager(isPreviewOnly: Boolean, previewImageView: TextureView) {
        this.mIsPreviewOnly = isPreviewOnly
        this.mPreviewImageView = previewImageView
        try {
            for (cameraId in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                if (characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) == null) {
                    continue
                }

                val cap = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
                val isCompatible =
                    cap?.contains(CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE)
                        ?: false

                if (!isCompatible) {
                    continue
                }

                when (characteristics.get(CameraCharacteristics.LENS_FACING)) {
                    CameraCharacteristics.LENS_FACING_BACK -> mFacingBackCameraId = cameraId
                    CameraCharacteristics.LENS_FACING_FRONT -> mFacingFrontCameraId = cameraId
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
        if (this::mImageReaderSurface.isInitialized) {
            mImageReaderSurface.release()
        }
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

        if (mIsPreviewOnly
            || mImageReader == null
            || mCaptureSession == null
            || !this::mImageReaderSurface.isInitialized
        ) {
            return
        }
        try {

            val builder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            builder.addTarget(mImageReaderSurface)
            mCaptureSession!!.stopRepeating()
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
                CameraHelper.getBestCameraPhotoPreviewSize(
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
                    override fun onSurfaceTextureSizeChanged(
                        surface: SurfaceTexture?,
                        w: Int,
                        h: Int
                    ) {
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
                            CameraHelper.getBestCameraPhotoPreviewSize(
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
                mImageReader = ImageReader.newInstance(
                    w,
                    h,
                    ImageFormat.JPEG,
                    1
                )
                mImageReader!!.setOnImageAvailableListener(
                    mOnImageAvailableListener,
                    mBackgroundHandler
                )
                mImageReaderSurface = mImageReader!!.surface
                sessionSurfaces.add(mImageReaderSurface)
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