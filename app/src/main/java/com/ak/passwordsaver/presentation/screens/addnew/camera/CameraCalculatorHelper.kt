package com.ak.passwordsaver.presentation.screens.addnew.camera

import android.content.Context
import android.graphics.ImageFormat
import android.graphics.Point
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.view.Surface
import android.view.WindowManager
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

object CameraCalculatorHelper {

    private const val MAX_PREVIEW_WIDTH = 1920
    private const val MAX_PREVIEW_HEIGHT = 1080
    private const val MAX_PHOTO_WIDTH = 3000
    private const val MAX_PHOTO_RATIO_DIFF = 0.091

    fun getBestCameraPhotoPreviewSize(
        context: Context,
        cameraId: String,
        cameraManager: CameraManager,
        w: Int,
        h: Int
    ): CameraSize {
        val sensorOrientation = getSensorOrientation(cameraId, cameraManager)
        val texturePreviewSize = getPreviewTextureSize(sensorOrientation, CameraSize(w, h), context)
        val maxPreviewSize = getMaxAvailablePreviewSize(sensorOrientation, context)

        val previewSizes = getAndconvertOutputSizes(SurfaceTexture::class.java, cameraId, cameraManager)
        val pictureSizes = getAndconvertOutputSizes(ImageFormat.JPEG, cameraId, cameraManager)
        val bestPhotoSize = getBestPhotoSize(pictureSizes, texturePreviewSize)

        return chooseOptimalPhotoPreviewSize(
            previewSizes,
            texturePreviewSize.width,
            texturePreviewSize.height,
            maxPreviewSize.width,
            maxPreviewSize.width,
            bestPhotoSize
        )
    }

    private val compareSizesByArea = Comparator<CameraSize> { a, b ->
        a.width * a.height - b.width * b.height
    }

    private fun getDefaultDisplay(context: Context) = getWindowManager(context)?.defaultDisplay!!

    private fun getWindowManager(context: Context) =
        context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager

    private fun getCurrentCameraCharacteristic(cameraId: String, cameraManager: CameraManager) =
        cameraManager.getCameraCharacteristics(cameraId)

    private fun getSensorOrientation(cameraId: String, cameraManager: CameraManager) =
        getCurrentCameraCharacteristic(
            cameraId,
            cameraManager
        ).get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0

    private fun getPreviewTextureSize(
        sensorOrientation: Int,
        textureSize: CameraSize,
        context: Context
    ): CameraSize {
        if (isSwappedDimensions(sensorOrientation, context)) {
            return CameraSize(textureSize.height, textureSize.width)
        }

        return CameraSize(textureSize.width, textureSize.height)
    }

    private fun isSwappedDimensions(sensorOrientation: Int, context: Context): Boolean {
        val displayRotation = getDefaultDisplay(context).rotation
        var swappedDimensions = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    swappedDimensions = true
                }
            Surface.ROTATION_90, Surface.ROTATION_270 ->
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    swappedDimensions = true
                }
        }

        return swappedDimensions
    }

    private fun getMaxAvailablePreviewSize(sensorOrientation: Int, context: Context): CameraSize {
        val displaySize = Point()
        getDefaultDisplay(context).getSize(displaySize)

        var maxPreviewWidth = displaySize.x
        var maxPreviewHeight = displaySize.y

        if (isSwappedDimensions(sensorOrientation, context)) {
            maxPreviewWidth = displaySize.y
            maxPreviewHeight = displaySize.x
        }

        if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
            maxPreviewWidth = MAX_PREVIEW_WIDTH
        }

        if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
            maxPreviewHeight = MAX_PREVIEW_HEIGHT
        }

        return CameraSize(maxPreviewWidth, maxPreviewHeight)
    }

    private fun getAndconvertOutputSizes(
        format: Any,
        cameraId: String,
        cameraManager: CameraManager
    ): List<CameraSize> {
        val sizes = ArrayList<CameraSize>()
        val characteristic = getCurrentCameraCharacteristic(cameraId, cameraManager)
        val map = characteristic.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        if (map != null) {
            val outputSizes = when (format) {
                is Int -> map.getOutputSizes(format)
                is Class<*> -> map.getOutputSizes(format)
                else -> null
            }
            if (outputSizes != null) {
                for (size in outputSizes) {
                    sizes.add(CameraSize(size.width, size.height))
                }
            }
        }
        return sizes
    }

    private fun getBestPhotoSize(
        pictureSizes: List<CameraSize>,
        previewSize: CameraSize
    ): CameraSize {

        val bestCandidatesList = ArrayList<CameraSize>()
        val worstCandidatesList = ArrayList<CameraSize>()
        val previewRatio = previewSize.heightRatio

        for (size in pictureSizes) {
            val ratio = size.heightRatio
            val diff = abs(ratio - previewRatio) / previewRatio
            if (diff < MAX_PHOTO_RATIO_DIFF) {
                if (size.width < MAX_PHOTO_WIDTH) {
                    bestCandidatesList.add(size)
                } else {
                    worstCandidatesList.add(size)
                }
            }
        }

        if (bestCandidatesList.isNotEmpty()) {
            return Collections.max(bestCandidatesList, compareSizesByArea)
        } else if (worstCandidatesList.isNotEmpty()) {
            return Collections.max(worstCandidatesList, compareSizesByArea)
        }

        return Collections.max(pictureSizes, compareSizesByArea)
    }

    private fun chooseOptimalPhotoPreviewSize(
        sizes: List<CameraSize>,
        textureViewWidth: Int, textureViewHeight: Int,
        maxWidth: Int, maxHeight: Int, optimalPhotoSize: CameraSize
    ): CameraSize {
        val bigEnough = ArrayList<CameraSize>()
        val notBigEnough = ArrayList<CameraSize>()

        if (textureViewWidth < 300 && textureViewHeight < 300) { // small preview
            return CameraSize(textureViewWidth, textureViewHeight)
        }

        val ratio = optimalPhotoSize.heightRatio
        for (option in sizes) {
            if (option.width <= maxWidth && option.height <= maxHeight &&
                option.height == (option.width * ratio).toInt()
            ) {
                if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                    bigEnough.add(option)
                } else {
                    notBigEnough.add(option)
                }
            }
        }

        if (bigEnough.isNotEmpty()) {
            return Collections.min(bigEnough, compareSizesByArea)
        } else if (notBigEnough.isNotEmpty()) {
            return Collections.max(notBigEnough, compareSizesByArea)
        }

        return optimalPhotoSize
    }
}