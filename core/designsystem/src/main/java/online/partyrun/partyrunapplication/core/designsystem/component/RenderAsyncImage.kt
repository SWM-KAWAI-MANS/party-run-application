package online.partyrun.partyrunapplication.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
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
