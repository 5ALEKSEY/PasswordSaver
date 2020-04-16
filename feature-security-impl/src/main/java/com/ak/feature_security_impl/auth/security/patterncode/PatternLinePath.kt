package com.ak.feature_security_impl.auth.security.patterncode

import android.graphics.Path
import androidx.annotation.ColorInt

data class PatternLinePath(
    @field:ColorInt
    var color: Int,
    var path: Path
)