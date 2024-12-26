package com.example.media3issue.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil

@OptIn(UnstableApi::class)
@Composable
fun AvailableH264DecodersWidget(
    modifier: Modifier = Modifier
) {
    val availableH264Decoders = remember {
        MediaCodecUtil.getDecoderInfos(MimeTypes.VIDEO_H264, false, false)
            .map { it.name }
            .toSet()
    }

    Column(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Available H264 decoders:", color = Color.White, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.size(4.dp))
        availableH264Decoders.forEach { decoder ->
            Text(text = decoder, color = Color.White, fontSize = 13.sp)
        }
    }
}