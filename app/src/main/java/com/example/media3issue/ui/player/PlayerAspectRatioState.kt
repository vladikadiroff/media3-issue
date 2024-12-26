package com.example.media3issue.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.Player
import androidx.media3.common.listen
import androidx.media3.common.util.UnstableApi

@UnstableApi
@Composable
fun rememberPlayerAspectRatioState(player: Player): PlayerAspectRatioState {
    val playerAspectRatioState = remember(player) { PlayerAspectRatioState(player) }
    LaunchedEffect(player) { playerAspectRatioState.observe() }
    return playerAspectRatioState
}

@UnstableApi
class PlayerAspectRatioState(private val player: Player) {
    var ratio by mutableFloatStateOf(player.getCurrentAspectRatio())
        private set

    suspend fun observe(): Nothing = player.listen { events ->
        if (events.containsAny(Player.EVENT_VIDEO_SIZE_CHANGED)) {
            // Return if any of dimensions is 0 to prevent video from
            // changing aspect ratio to 0 on quality change
            if (videoSize.width != 0 && videoSize.height != 0) {
                ratio = getCurrentAspectRatio()
            }
        }
    }

    private fun Player.getCurrentAspectRatio(): Float {
        val width = videoSize.width
        val height = videoSize.height

        return when {
            height == 0 -> 0f
            width == 0 -> 0f
            else -> width * videoSize.pixelWidthHeightRatio / height
        }
    }
}