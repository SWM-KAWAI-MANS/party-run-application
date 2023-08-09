package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
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
    imageUrl: String, // URL 문자열.
    contentDescription: String?,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl) // URL을 data 메서드에 전달
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
