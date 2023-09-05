package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun RenderAsyncImage(
    image: Int,
    size: Int,
    contentDescription: String?,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .size(size)
            .crossfade(true)
            .build(),
    )
    if (painter.state is AsyncImagePainter.State.Loading ||
        painter.state is AsyncImagePainter.State.Error
    ) {
        CircularProgressIndicator()
    }
    Image(
        painter = painter,
        contentDescription = contentDescription
    )
}

@Composable
fun RenderAsyncUrlImage(
    modifier: Modifier = Modifier,
    imageUrl: String, // URL 문자열.
    contentDescription: String?,
) {
    var retryHash by remember { mutableStateOf(0) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl) // URL을 data 메서드에 전달
            .setParameter("retry_hash", retryHash, memoryCacheKey = null)
            .crossfade(true)
            .build()
    )
    if (painter.state is AsyncImagePainter.State.Error) {
        retryHash += 1
    }
    if (painter.state is AsyncImagePainter.State.Loading) {
        CircularProgressIndicator()
    }
    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = contentDescription
    )
}
