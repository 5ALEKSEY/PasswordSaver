package com.ak.passwordsaver.auth.security.patterncode

import android.graphics.Path
import androidx.annotation.ColorInt

data class PatternLinePath(
    @field:ColorInt
    var color: Int,
    var path: Path
)