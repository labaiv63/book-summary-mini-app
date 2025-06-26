package com.test.design_system.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
sealed class CustomImage(val description: String? = null) {
    data class Resource(val resourceId: Int) : CustomImage()
    data class Url(val url: String) : CustomImage()
    data class Vector(val vector: ImageVector) : CustomImage()
}