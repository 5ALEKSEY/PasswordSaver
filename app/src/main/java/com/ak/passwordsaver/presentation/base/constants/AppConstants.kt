package com.ak.passwordsaver.presentation.base.constants

object AppConstants {
    // Request codes
    const val AVATAR_PERMISSIONS_REQUEST_CODE = 1
    const val SECURITY_REQUEST_CODE = 2
    const val GALLERY_IMAGE_PICK_REQUEST_CODE = 3
    const val CAMERA_IMAGE_PICK_REQUEST_CODE = 4

    // create password
    const val PASSWORD_NAME_MAX_LENGTH = 20
    const val PASSWORD_CONTENT_MAX_LENGTH = 25

    // Other constants
    const val IMAGE_MIME_TYPE = "image/*"
    const val TEXT_INPUT_DEBOUNCE = 200L
    const val BLOCK_SECURITY_INPUT_DELAY = 60.toLong() // 60 seconds
    const val BLOCK_SECURITY_INTERVAL = 1.toLong() // 1 second
    const val PASSWORDS_LIST_COLUMN_COUNT = 2
    const val TOOLBAR_SCROLL_MIN_PASSWORDS_SIZE = 4
}