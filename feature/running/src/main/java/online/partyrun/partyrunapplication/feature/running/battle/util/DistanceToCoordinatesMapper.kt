package online.partyrun.partyrunapplication.feature.running.battle.util

/**
 * 비율 기반 거리 매핑 함수
 */
fun distanceToCoordinatesMapper(
    totalTrackDistance: Double,
    distance: Double,
    trackWidth: Double,
    trackHeight: Double
): Pair<Double, Double> {
    // 끝이 둥근 타원의 가로 반지름과 세로 반지름
    val horizontalRadius = trackWidth / 2
    val verticalRadius = trackHeight / 2

    // 거리를 총 트랙 거리에 대한 비율로 변환
    val ratio = distance / totalTrackDistance

    // 타원형 트랙의 x축, y축 좌표 계산
    val x = horizontalRadius * kotlin.math.cos(2 * kotlin.math.PI * ratio + kotlin.math.PI)
    val y = -verticalRadius * kotlin.math.sin(2 * kotlin.math.PI * ratio)

    // 그림의 크기에 따라 좌표를 스케일링
    val scaledX = scaleCoordinate(x, trackWidth, horizontalRadius)
    val scaledY = scaleCoordinate(y, trackHeight, verticalRadius)

    val currentX = adjustXCoordinate(scaledX, trackWidth)
    val currentY = adjustYCoordinate(scaledY, trackHeight)

    return Pair(currentX, currentY)
}

/**
 * 러너의 현재 달린 거리가 0m 혹은 목표거리에 도달한 경우(Ex. 1000m)이면
 * 좌표 값이 0으로 떨어지는게 아니라 약간의 근소한 마이너스 값을 가지므로
 * 이를 추가로 보정해주기 위한 handleSmallNumbers 함수가 들어감.
 */
fun scaleCoordinate(coordinate: Double, trackSize: Double, radius: Double): Double {
    return handleSmallNumbers(coordinate * (trackSize / (radius * 2)))
}

/**
 * 거리를 총 트랙 거리에 대한 비율로 변환하고 트랙 그림에 따라 좌표를 스케일링했을 경우 나오는 좌표 기준으로 오차보정 수행.
 */
fun adjustXCoordinate(x: Double, trackWidth: Double): Double {
    return when {
        x < 0 -> x + 0.02 * trackWidth
        x > 0 -> x - 0.02 * trackWidth
        else -> x - 0.045 * trackWidth
    }
}

fun adjustYCoordinate(y: Double, trackHeight: Double): Double {
    return when {
        y < 0 -> y - 0.15 * trackHeight
        y > 0 -> y - 0.2 * trackHeight
        else -> y - 0.17 * trackHeight
    }
}

/**
 * 수치가 특정 임계값 이하일 경우 0으로 처리
 * @param threshold: 원하는 임계값. 이 값보다 작은 절대값을 가진 수는 0으로 처리
 */
fun handleSmallNumbers(value: Double, threshold: Double = 1e-10): Double {
    return if (kotlin.math.abs(value) < threshold) 0.0 else value
}
