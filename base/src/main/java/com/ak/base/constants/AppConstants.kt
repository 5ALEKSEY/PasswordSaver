package com.ak.base.constants

object AppConstants {
    // Request codes
    const val AVATAR_PERMISSIONS_REQUEST_CODE = 1
    const val GALLERY_IMAGE_PICK_REQUEST_CODE = AVATAR_PERMISSIONS_REQUEST_CODE + 1
    const val CAMERA_IMAGE_PICK_REQUEST_CODE = GALLERY_IMAGE_PICK_REQUEST_CODE + 1
    const val BACKUP_FILE_PICK_REQUEST_CODE = CAMERA_IMAGE_PICK_REQUEST_CODE + 1

    // create password
    const val PASSWORD_NAME_MAX_LENGTH = 30
    const val PASSWORD_CONTENT_MAX_LENGTH = 45

    // create account
    const val ACCOUNT_NAME_MAX_LENGTH = 30
    const val ACCOUNT_LOGIN_MAX_LENGTH = 45
    const val ACCOUNT_PASSWORD_MAX_LENGTH = 45

    // Other constants
    const val IMAGE_MIME_TYPE = "image/*"
    const val BACKUP_FILE_MIME_TYPE = "text/plain"
    const val TEXT_INPUT_DEBOUNCE = 200L
    const val BLOCK_SECURITY_INPUT_DELAY = 60.toLong() // 60 seconds
    const val BLOCK_SECURITY_INTERVAL = 1.toLong() // 1 second
    const val PASSWORDS_LIST_COLUMN_COUNT = 1
    const val TOOLBAR_SCROLL_MIN_PASSWORDS_SIZE = 6
    const val VIEW_SAFE_CLICK_DELAY_IN_MILLIS = 900L
    const val MAX_LINES_VISIBLE_CONTENT = 3
    const val MAX_LINES_INVISIBLE_CONTENT = 1
}