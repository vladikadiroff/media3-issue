package com.example.media3issue.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import kotlin.math.abs

@Composable
internal fun AspectRatioLayout(
    modifier: Modifier,
    aspectRatio: Float,
    resizeMode: AspectRatioResizeMode,
    maxAspectRationDeformationFraction: Float = MAX_ASPECT_RATIO_DEFORMATION_FRACTION,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        var width = constraints.maxWidth
        var height = constraints.maxHeight
        val viewAspectRatio = width.toFloat() / height
        val aspectDeformation = aspectRatio / viewAspectRatio - 1
        if (aspectRatio > 0 && abs(aspectDeformation) > maxAspectRationDeformationFraction) {
            when (resizeMode) {
                AspectRatioResizeMode.FILL -> Unit

                AspectRatioResizeMode.FIT -> if (aspectDeformation > 0) {
                    height = (width / aspectRatio).toInt()
                } else {
                    width = (height * aspectRatio).toInt()
                }

                AspectRatioResizeMode.ZOOM -> if (aspectDeformation > 0) {
                    width = (height * aspectRatio).toInt()
                } else {
                    height = (width / aspectRatio).toInt()
                }

                AspectRatioResizeMode.FIXED_WIDTH -> height = (width / aspectRatio).toInt()

                AspectRatioResizeMode.FIXED_HEIGHT -> width = (height * aspectRatio).toInt()
            }
        }

        layout(width, height) {
            val childConstraints = constraints.copy(
                minWidth = 0,
                minHeight = 0,
                maxWidth = width,
                maxHeight = height
            )
            measurables.forEach { measurable ->
                val placeable = measurable.measure(childConstraints)
                placeable.placeRelative(0, 0)
            }
        }
    }
}

/**
 * Resize modes for [PlayerSurfaceV2]. One of [FIT], [FILL],
 * [ZOOM], [FIXED_WIDTH] or [FIXED_HEIGHT].
 */
enum class AspectRatioResizeMode {
    /** Either the width or height is decreased
     * to obtain the desired aspect ratio. */
    FIT,

    /** The specified aspect ratio is ignored. */
    FILL,

    /** Either the width or height is increased
     * to obtain the desired aspect ratio.
     */
    ZOOM,

    /**
     * The width is fixed and the height is increased or decreased
     * to obtain the desired aspect ratio.
     */
    FIXED_WIDTH,

    /**
     * The height is fixed and the width is increased or decreased
     * to obtain the desired aspect ratio.
     */
    FIXED_HEIGHT,
}


private const val MAX_ASPECT_RATIO_DEFORMATION_FRACTION = 0.01f
