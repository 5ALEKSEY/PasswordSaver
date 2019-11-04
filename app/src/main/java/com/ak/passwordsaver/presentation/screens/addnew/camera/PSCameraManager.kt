package com.ak.passwordsaver.presentation.screens.addnew.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView


class PSCameraManager constructor(
    private val context: Context,
    private val isPreviewOnly: Boolean,
    private val previewImageView: TextureView
) {

    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null

    private var mCameraManager: CameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var mCameraDevice: CameraDevice? = null
    private lateinit var mCaptureSession: CameraCaptureSession
    private lateinit var mPreviewSurface: Surface

    private var mCurrentCameraId: String = ""
    private var mFacingBackCameraId: String = ""
    private var mFacingFrontCameraId: String = ""

    init {
        try {
            mCameraManager.cameraIdList.forEach {
                val characteristics = mCameraManager.getCameraCharacteristics(it)
                when (characteristics.get(CameraCharacteristics.LENS_FACING)) {
                    CameraCharacteristics.LENS_FACING_BACK -> mFacingBackCameraId = it
                    CameraCharacteristics.LENS_FACING_FRONT -> mFacingFrontCameraId = it
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun openCamera() {
        if (mCurrentCameraId.isNotEmpty()) {
            openCamera(mCurrentCameraId)
        } else {
            openCamera(mFacingBackCameraId)
            mCurrentCameraId = mFacingBackCameraId
        }
    }

    fun closeCamera() {
        stopBackgroundThread()
        mCameraDevice?.close()
        mCameraDevice = null
        mPreviewSurface.release()
    }

    fun switchCamera() {
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

    private fun openCamera(cameraId: String) {
        startBackgroundThread()
        try {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                mCameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
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
        if (mCameraDevice == null) {
            return
        }

        if (previewImageView.isAvailable) {
            val surfaceTexture = previewImageView.surfaceTexture
            val optimalPreviewSize = CameraCalculatorHelper.getBestCameraPhotoPreviewSize(
                context,
                mCurrentCameraId,
                mCameraManager,
                previewImageView.width, previewImageView.height
            )
            surfaceTexture.setDefaultBufferSize(optimalPreviewSize.width, optimalPreviewSize.height)
            createCameraPreviewSession(surfaceTexture)
        } else {
            previewImageView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, w: Int, h: Int) {

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

                    val optimalPreviewSize = CameraCalculatorHelper.getBestCameraPhotoPreviewSize(
                        context,
                        mCurrentCameraId,
                        mCameraManager,
                        width,
                        height
                    )
                    surface.setDefaultBufferSize(
                        optimalPreviewSize.width,
                        optimalPreviewSize.height
                    )

                    createCameraPreviewSession(surface)
                }
            }
        }
    }

    private fun createCameraPreviewSession(surfaceTexture: SurfaceTexture) {
        mPreviewSurface = Surface(surfaceTexture)
        try {
            val sessionSurfaces = arrayListOf<Surface>()
            if (isPreviewOnly) {
                sessionSurfaces.add(mPreviewSurface)
            } else {
                // TODO: create image reader surface for capture session
            }
            mCameraDevice!!.createCaptureSession(
                sessionSurfaces,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigureFailed(session: CameraCaptureSession) {

                    }

                    override fun onConfigured(session: CameraCaptureSession) {
                        if (isPreviewOnly) {
                            mCaptureSession = session
                            val builder =
                                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                            builder.addTarget(mPreviewSurface)
                            mCaptureSession.setRepeatingRequest(
                                builder.build(),
                                null,
                                mBackgroundHandler
                            )
                        } else {
                            // TODO: create request with listener for making photo
                        }
                    }
                },
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
}