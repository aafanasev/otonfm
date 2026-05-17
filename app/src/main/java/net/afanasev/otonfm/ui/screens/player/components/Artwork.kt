package net.afanasev.otonfm.ui.screens.player.components

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import net.afanasev.otonfm.R
import net.afanasev.otonfm.ui.theme.LocalCustomColorsPalette

@Composable
fun Artwork(
    artworkUri: String,
    modifier: Modifier,
) {
    val previewShape = RoundedCornerShape(12.dp)
    val (rotX, rotY) = rememberParallaxRotation()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .graphicsLayer {
                rotationX = rotX
                rotationY = rotY
                cameraDistance = 12f * density
            }
            .then(
                Modifier
                    .aspectRatio(1f)
                    .shadow(
                        elevation = 6.dp,
                        shape = previewShape,
                        ambientColor = Color.DarkGray,
                        spotColor = Color.DarkGray,
                    ),
            )
            .clip(previewShape)
            .background(LocalCustomColorsPalette.current.previewBackground),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(artworkUri)
                .crossfade(true)
                .build(),
            contentDescription = "Preview",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun rememberParallaxRotation(maxDegrees: Float = 15f): Pair<Float, Float> {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val sensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    var rawRotX by remember { mutableFloatStateOf(0f) }
    var rawRotY by remember { mutableFloatStateOf(0f) }
    val baseline = remember { floatArrayOf(Float.NaN, Float.NaN) }

    DisposableEffect(sensor) {
        if (sensor == null) return@DisposableEffect onDispose {}

        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)
        val maxRadians = Math.toRadians(maxDegrees.toDouble()).toFloat()

        val listener = object : SensorEventListener {
            override fun onAccuracyChanged(s: Sensor, accuracy: Int) = Unit
            override fun onSensorChanged(event: SensorEvent) {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                SensorManager.getOrientation(rotationMatrix, orientation)
                val pitch = orientation[1]
                val roll = orientation[2]
                if (baseline[0].isNaN()) {
                    baseline[0] = pitch
                    baseline[1] = roll
                }
                val deltaPitch = (pitch - baseline[0]).coerceIn(-maxRadians, maxRadians)
                val deltaRoll = (roll - baseline[1]).coerceIn(-maxRadians, maxRadians)
                rawRotX = Math.toDegrees(deltaPitch.toDouble()).toFloat()
                rawRotY = -Math.toDegrees(deltaRoll.toDouble()).toFloat()
            }
        }

        sensorManager.registerListener(
            listener,
            sensor,
            SensorManager.SENSOR_DELAY_GAME,
            Handler(Looper.getMainLooper()),
        )

        onDispose { sensorManager.unregisterListener(listener) }
    }

    val smoothRotX by animateFloatAsState(
        targetValue = rawRotX,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "parallaxRotX",
    )
    val smoothRotY by animateFloatAsState(
        targetValue = rawRotY,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "parallaxRotY",
    )

    return Pair(smoothRotX, smoothRotY)
}

@Preview
@Composable
fun ArtworkNoImagePreview() {
    Artwork(
        artworkUri = stringResource(R.string.default_artwork_uri),
        modifier = Modifier.width(60.dp),
    )
}
