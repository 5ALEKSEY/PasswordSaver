package com.ak.passwordsaver.presentation.screens.addnew.ui

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
import java.util.*


class CameraPreviewService constructor(
    private val context: Context,
    private val cameraId: String,
    private val cameraManager: CameraManager,
    private val previewImageView: TextureView
) {
    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null

    private var mCameraDevice: CameraDevice? = null
    private lateinit var mCaptureSession: CameraCaptureSession
    private lateinit var mPreviewSurface: Surface

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

        }finally {
            mBackgroundThread = null
        }
    }

    fun initCameraPreview() {
        startBackgroundThread()
        try {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                    override fun onOpened(cameraDevice: CameraDevice) {
                        mCameraDevice = cameraDevice
                        startCameraPreview()
                    }

                    override fun onClosed(camera: CameraDevice) {
                        super.onClosed(camera)

                    }

                    override fun onDisconnected(camera: CameraDevice) {

                    }

                    override fun onError(camera: CameraDevice, error: Int) {

                    }
                }, mBackgroundHandler)

            }
        } catch (e: CameraAccessException) {

        }
    }

    fun closeCameraPreview() {
        stopBackgroundThread()
        mCameraDevice?.close()
        mCameraDevice = null
        mPreviewSurface.release()
    }

    private fun startCameraPreview() {
        if (mCameraDevice == null) {
            return
        }

        previewImageView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, w: Int, h: Int) {

            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.d("d", "ss")
                return true
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, w: Int, h: Int) {
                surface?.let {
                    createCameraPreviewSession(it)
                }
            }

        }
    }

    private fun createCameraPreviewSession(surfaceTexture: SurfaceTexture) {
        mPreviewSurface = Surface(surfaceTexture)
        try {
            mCameraDevice!!.createCaptureSession(
                listOf(mPreviewSurface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigureFailed(session: CameraCaptureSession) {

                    }

                    override fun onConfigured(session: CameraCaptureSession) {
                        mCaptureSession = session
                        val builder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                        builder.addTarget(mPreviewSurface)
                        mCaptureSession.setRepeatingRequest(builder.build(), null, mBackgroundHandler)
                    }
                },
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
}