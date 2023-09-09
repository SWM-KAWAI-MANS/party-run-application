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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import online.partyrun.partyrunapplication.core.designsystem.icon.PartyRunIcons
import online.partyrun.partyrunapplication.core.model.running_result.ui.RunnerRecordUiModel

@Composable
fun MapWidget(
    targetDistance: Int?,
    targetDistanceFormatted: String,
    records: List<RunnerRecordUiModel>?,
) {
    val points = records?.map { LatLng(it.latitude, it.longitude) } ?: listOf()
    val centerLatLng = getCenterLatLng(points)

    /*
     * centerLatLng 사용하여 카메라의 초기 위치 및 zoom 설정. -> 1000M면 14.5f https://ai-programmer.tistory.com/2
     * centerLatLng의 변화를 감지하여 cameraPositionState 업데이트
     */
    val zoomValue = getZoomValueForDistance(targetDistance)

    val cameraPositionState: CameraPositionState = rememberCameraPositionState()
    LaunchedEffect(centerLatLng) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(centerLatLng, zoomValue)
    }

    GoogleMap(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = true
        ),
        cameraPositionState = cameraPositionState
    ) {
        Polyline(
            points = points,
            color = Color.Red
        )
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

/**
 * targetDistance 값에 따라 zoom 값 결정
 */
@Composable
private fun getZoomValueForDistance(targetDistance: Int?): Float {
    val zoomValue = when (targetDistance) {
        1000 -> 14.5f
        3000 -> 13.5f
        5000 -> 12.8f
        10000 -> 12f
        else -> 12f  // 기본값 혹은 예기치 않은 값에 대한 대응
    }
    return zoomValue
}

/**
 * cameraPosition를 중앙에 위치시키기 위한 points 중앙 좌표를 구하는 작업 수행
 */
@Composable
private fun getCenterLatLng(points: List<LatLng>): LatLng {
    val startLatLng = points.firstOrNull() ?: LatLng(0.0, 0.0)
    val endLatLng = points.lastOrNull() ?: LatLng(0.0, 0.0)

    val bounds = LatLngBounds.Builder()
        .include(startLatLng)
        .include(endLatLng)
        .build()

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
