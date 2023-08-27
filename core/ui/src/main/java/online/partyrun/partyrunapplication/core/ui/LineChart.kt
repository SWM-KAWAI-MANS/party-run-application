package online.partyrun.partyrunapplication.core.ui

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data: List<Pair<String, Double>>? = emptyList()
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp)) // 둥근 테두리 적용
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            LineChartContent(
                modifier = modifier.fillMaxSize(),
                data = data ?: emptyList()
            )
        }
    }
}

@Composable
internal fun LineChartContent(
    modifier: Modifier = Modifier,
    data: List<Pair<String, Double>> = emptyList(),
) {
    val spacing = 100f
    val graphColor = MaterialTheme.colorScheme.primary
    val transparentGraphColor = remember { graphColor.copy(alpha = 0.5f) }
    val upperValue = remember(data) { (data.maxOfOrNull { it.second }?.plus(1))?.roundToInt() ?: 0 }
    val lowerValue = remember(data) { (data.minOfOrNull { it.second }?.toInt() ?: 0) }
    val density = LocalDensity.current

    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier.padding(5.dp)) {
        val spacePerSize = (size.width - spacing) / data.size

        // 전체 데이터셋의 크기를 표시하려는 데이터의 수로 나눠 적절한 step 값 획득
        val stepSize = if (data.size <= 4) 1 else data.size / 4

        (data.indices step stepSize).take(4).forEach { i ->
            val xStep = data[i].first
            // X축에 표시되는 시간 값 그리기
            drawContext.canvas.nativeCanvas.apply {
                // 시작 위치를 오른쪽으로 더 옮기기 위해 x에 오프셋 30f을 더하여 조정
                val x = spacing + i * spacePerSize + 30f
                drawText(
                    xStep,
                    x, // 오프셋을 더해 x축 시작 위치를 오른쪽으로 이동
                    size.height,
                    textPaint
                )
            }

            /*
             * X축 격자선 그리기
             * * start점 = Offset(x, 0f - spacing) 좌표로 설정
             * 여기서 x는 계산된 X 좌표이며, 0f는 Y 좌표로서 화면 상단을 의미 -> spacing을 빼는 이유는 마지막 값까지의 길이를 고려하기 위함
             * end점 = (x, size.height - spacing) 좌표로 설정
             * size.height는 그래프의 전체 높이를 나타내며, spacing을 빼는 이유는 그래프의 아래 여백을 고려하기 위함
             */
            val x = spacing + i * spacePerSize + 30f
            drawLine(
                color = Color.Gray,
                start = Offset(x, 0f - spacing),
                end = Offset(x, size.height - spacing),
                strokeWidth = 1f
            )
        }

        val yStep = (upperValue - lowerValue) / 4f
        (0..4).forEach { i ->
            val y = size.height - spacing - i * size.height / 4f

            /**
             * 텍스트의 높이와 중앙 위치 계산
             * Paint.fontMetrics의 ascent는 baseline에서 텍스트의 최상단까지의 거리 (음수 값),
             * descent는 baseline에서 텍스트의 최하단까지의 거리
             * 이 두 값의 합을 통해 텍스트의 전체 높이를 구함.
             */
            val textHeight = textPaint.fontMetrics.run { descent - ascent }
            val textCenterOffset =
                textHeight / 2 + textPaint.fontMetrics.ascent // ascent는 음수값이므로 더하기로 처리
            val formattedValue = String.format("%.1f", (lowerValue + yStep * i))

            // Y축에 표시되는 숫자 값 그리기
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    formattedValue,
                    55f,
                    y - textCenterOffset, // 텍스트의 중앙 조정
                    textPaint
                )
            }

            // Y축 격자선 그리기
            drawLine(
                color = Color.Gray,
                start = Offset(spacing, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
        }

        val strokePath = Path().apply {
            val height = size.height
            data.indices.forEach { i ->
                val info = data[i]
                val ratio = (info.second - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerSize + 30f
                val y1 = height - spacing - (ratio * height).toFloat()

                if (i == 0) {
                    moveTo(x1, y1)
                }
                lineTo(x1, y1)
            }
        }

        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        )

        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(size.width - spacePerSize + 30f, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )

    }
}


@Preview(showBackground = true)
@Composable
fun TestLineChart() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .height(400.dp)
            .padding(20.dp),
    ) {
        val data = listOf(
            Pair("00:00:01", 1.0),
            Pair("00:00:02", 1.3),
            Pair("00:00:03", 0.2),
            Pair("00:00:10", 2.3),
            Pair("00:00:12", 1.1),
            Pair("00:00:31", 15000.2),
            Pair("00:15:01", 1.0),
            Pair("00:16:01", 22500.0),
            Pair("00:17:01", 1.0),
            Pair("00:28:01", 8300.0),
            Pair("00:39:01", 3.0),
            Pair("00:40:01", 0.0),
        )
        Text(text = "테스트", color = Color.Red)
        LineChart(
            data = data
        )
    }
}
