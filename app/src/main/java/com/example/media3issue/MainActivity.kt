package com.example.media3issue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import com.example.media3issue.data.demoMediaItems
import com.example.media3issue.ui.player.AspectRatioLayout
import com.example.media3issue.ui.player.AspectRatioResizeMode
import com.example.media3issue.ui.player.AvailableH264DecodersWidget
import com.example.media3issue.ui.player.MinimalControls
import com.example.media3issue.ui.player.PlayerSurface
import com.example.media3issue.ui.player.SURFACE_TYPE_SURFACE_VIEW
import com.example.media3issue.ui.player.rememberPlayerAspectRatioState
import com.example.media3issue.ui.theme.Media3IssueTheme

@OptIn(UnstableApi::class)
class MainActivity : ComponentActivity() {
    private val player by lazy {
        val rendersFactory = DefaultRenderersFactory(this)
            .setMediaCodecSelector(ProblematicH264MediaCodecSelector)

        ExoPlayer.Builder(this, rendersFactory).build().apply {
            setMediaItems(demoMediaItems)
            prepare()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Media3IssueTheme {
                MediaPlayerScreen(
                    player = player,
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun MediaPlayerScreen(player: Player, modifier: Modifier = Modifier) {
    Box(modifier) {
        val playerAspectRatioState = rememberPlayerAspectRatioState(player)
        AspectRatioLayout(
            modifier = modifier
                .background(Color.Black)
                .fillMaxSize(),
            resizeMode = AspectRatioResizeMode.FIT,
            aspectRatio = playerAspectRatioState.ratio,
            content = {
                PlayerSurface(
                    player = player,
                    surfaceType = SURFACE_TYPE_SURFACE_VIEW,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        )
        MinimalControls(player, Modifier.align(Alignment.Center))
        AvailableH264DecodersWidget(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .align(Alignment.TopEnd)
        )
    }
}