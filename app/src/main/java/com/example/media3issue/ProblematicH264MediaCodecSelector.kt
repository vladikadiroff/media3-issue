package com.example.media3issue

import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil

/**
 * Selects broken h264 codecs if they are on the device
 */
@UnstableApi
object ProblematicH264MediaCodecSelector : MediaCodecSelector {
    private const val EXYNOS_H264_CODEC_NAME = "c2.exynos.h264.decoder"
    private const val ANDROID_AVC_CODEC_NAME = "c2.android.avc.decoder"

    override fun getDecoderInfos(
        mimeType: String,
        requiresSecureDecoder: Boolean,
        requiresTunnelingDecoder: Boolean
    ): List<MediaCodecInfo> {
        val codecs = MediaCodecUtil.getDecoderInfos(
            mimeType,
            requiresSecureDecoder,
            requiresTunnelingDecoder
        )

        return when {
            mimeType != MimeTypes.VIDEO_H264 -> codecs

            codecs.any { it.isProblematicH264Codec() } ->
                codecs.filter { it.isProblematicH264Codec() }

            else -> codecs
        }
    }

    private fun MediaCodecInfo.isProblematicH264Codec() =
        name == EXYNOS_H264_CODEC_NAME || name == ANDROID_AVC_CODEC_NAME
}