package com.test.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.SubcomposeAsyncImage
import com.test.design_system.models.CustomImage
import com.test.design_system.theme.Blue

@Composable
fun CustomImageComponent(
    modifier: Modifier = Modifier,
    image: CustomImage,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
) {
    when (image) {
        is CustomImage.Resource -> Image(
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            painter = painterResource(id = image.resourceId),
            contentDescription = image.description
        )

        is CustomImage.Vector -> Image(
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            imageVector = image.vector,
            contentDescription = image.description
        )

        is CustomImage.Url -> SubcomposeAsyncImage(
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            loading = {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Blue
                    )
                }
            },
            model = image.url,
            contentDescription = image.description
        )
    }
}