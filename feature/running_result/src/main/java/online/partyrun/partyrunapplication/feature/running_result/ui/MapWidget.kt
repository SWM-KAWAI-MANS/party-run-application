package online.partyrun.partyrunapplication.feature.running_result.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerRecordUiModel
import online.partyrun.partyrunapplication.feature.running_result.R

@Composable
fun MapWidget(
    targetDistanceFormatted: String,
    records: List<RunnerRecordUiModel>?,
) {
    val points = records?.map { LatLng(it.latitude, it.longitude) } ?: listOf()
    val centerLatLng = getCenterLatLng(points)

    val cameraPositionState: CameraPositionState = rememberCameraPositionState()
    LaunchedEffect(centerLatLng) {
        val zoomValue = getZoomValueForDistance(records?.lastOrNull()?.distance)
        cameraPositionState.position = CameraPosition.fromLatLngZoom(centerLatLng, zoomValue)
    }

    GoogleMap(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
        properties = MapProperties(
            isBuildingEnabled = true,
            isMyLocationEnabled = true
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = true,
            rotationGesturesEnabled = true,
            zoomGesturesEnabled = true,
            tiltGesturesEnabled = true,
            scrollGesturesEnabled = true,
            scrollGesturesEnabledDuringRotateOrZoom = true
        ),
        cameraPositionState = cameraPositionState
    ) {
        RenderStartAndEndMarkers(points)
        RenderPolyline(points)
    }
    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        DistanceBox(targetDistanceFormatted)
    }
}

@Composable
private fun RenderPolyline(points: List<LatLng>) {
    for (i in 0 until points.size - 1) {
        val color = getPolylineColor(i)
        Polyline(
            points = listOf(points[i], points[i + 1]),
            color = color,
            geodesic = true,
            width = 12f,
            pattern = listOf(Dash(10f))
        )
    }
}

@Composable
private fun getPolylineColor(index: Int): Color {
    return when (index % 3) {
        0 -> MaterialTheme.colorScheme.surfaceVariant
        1 -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.outline
    }
}

@Composable
private fun RenderStartAndEndMarkers(points: List<LatLng>) {
    points.firstOrNull()?.let {
        Marker(
            state = MarkerState(it),
            icon = getMarkerIcon(R.drawable.start_marker)
        )
    }

    points.lastOrNull()?.let {
        Marker(
            state = MarkerState(it),
            icon = getMarkerIcon(R.drawable.finish_marker)
        )
    }
}

@Composable
private fun getMarkerIcon(resId: Int): BitmapDescriptor {
    val imageBitmap = ImageBitmap.imageResource(id = resId)
    return BitmapDescriptorFactory.fromBitmap(imageBitmap.asAndroidBitmap())
}

private fun getZoomValueForDistance(currentDistance: Double?): Float {
    val distances = arrayOf(0, 1000, 3000, 5000, 10000, 15000, 20000)
    val zoomValues = arrayOf(17.5f, 14.6f, 13.4f, 12.7f, 12f, 11f, 9f)

    val actualDistance = currentDistance ?: 0.0

    for (i in 0 until distances.size - 1) {
        if (actualDistance >= distances[i] && actualDistance < distances[i + 1]) {
            val proportion =
                (actualDistance - distances[i]).toFloat() / (distances[i + 1] - distances[i])
            return zoomValues[i] + proportion * (zoomValues[i + 1] - zoomValues[i])
        }
    }

    return 8f // 기본값
}

/**
 * cameraPosition를 중앙에 위치시키기 위한 points 중앙 좌표를 구하는 작업 수행
 */
@Composable
private fun getCenterLatLng(points: List<LatLng>): LatLng {
    if (points.isEmpty()) return LatLng(0.0, 0.0)

    val builder = LatLngBounds.builder()
    points.forEach { builder.include(it) }
    val bounds = builder.build()

    // bounds의 중심점을 구하기
    return bounds.center
}

@Composable
private fun DistanceBox(targetDistance: String) {
    Row(
        modifier = Modifier.padding(10.dp)
    ) {
        Image(
            painter = painterResource(id = PartyRunIcons.DistanceIcon),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .padding(top = 3.dp),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
        )
        Text(
            text = targetDistance, // "X,xxx m" 형식
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
