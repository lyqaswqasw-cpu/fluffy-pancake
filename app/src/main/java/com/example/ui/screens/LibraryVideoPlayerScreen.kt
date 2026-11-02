package com.example.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.media3.extractor.ts.DefaultTsPayloadReaderFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.data.model.ChannelEngagementStats
import com.example.data.model.PlaybackRequest
import com.example.ui.components.OrderedColorfulDivider
import com.example.ui.components.isConfirmKey
import com.example.ui.components.tvFocusHighlight
import com.example.ui.components.tvFocusableClickable
import com.example.ui.theme.FoulBlackBg
import com.example.ui.theme.FoulBottomBarBlack
import com.example.ui.theme.FoulCardDark
import com.example.ui.theme.FoulCardElevated
import com.example.ui.theme.FoulDivider
import com.example.ui.theme.FoulGoldStar
import com.example.ui.theme.FoulLiveGreen
import com.example.ui.theme.FoulLiveRed
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulPurpleDeep
import com.example.ui.theme.FoulTeamAwayCyan
import com.example.ui.theme.FoulTextSecondary
import kotlinx.coroutines.delay
import java.io.File
import java.util.Locale

@OptIn(UnstableApi::class)
private enum class PlayerAspectMode(val resizeMode: Int, val labelAr: String) {
    FIT(AspectRatioFrameLayout.RESIZE_MODE_FIT, "الأبعاد الأصلية"),
    ZOOM(AspectRatioFrameLayout.RESIZE_MODE_ZOOM, "تكبير الأطراف"),
    FILL(AspectRatioFrameLayout.RESIZE_MODE_FILL, "ملء الشاشة")
}

/**
 * Real Video Quality Track inspected directly from the active ExoPlayer Media3 stream
 */
@Immutable
private data class RealVideoQualityOption(
    val id: String,
    val trackGroup: TrackGroup,
    val trackIndex: Int,
    val width: Int,
    val height: Int,
    val bitrateBps: Int,
    val frameRate: Float,
    val shortBadge: String,
    val detailSubtitle: String
)

/**
 * Real Audio or Subtitle Track inspected from the active ExoPlayer Media3 stream
 */
@Immutable
private data class RealMediaTrackOption(
    val id: String,
    val trackGroup: TrackGroup,
    val trackIndex: Int,
    val label: String,
    val language: String,
    val isSelected: Boolean
)

private fun formatResolutionShortBadge(width: Int, height: Int): String {
    val effectiveHeight = if (height > 0) height else (width * 9) / 16
    return when {
        effectiveHeight >= 2160 || width >= 3840 -> "4K UHD"
        effectiveHeight >= 1440 || width >= 2560 -> "2K QHD"
        effectiveHeight >= 1080 || width >= 1920 -> "1080p FHD"
        effectiveHeight >= 720 || width >= 1280 -> "720p HD"
        effectiveHeight >= 540 -> "576p SD"
        effectiveHeight >= 480 -> "480p SD"
        effectiveHeight >= 360 -> "360p"
        effectiveHeight > 0 -> "${effectiveHeight}p"
        else -> "HD"
    }
}

private fun formatQualityDetailSubtitle(width: Int, height: Int, bitrateBps: Int, frameRate: Float): String {
    val parts = mutableListOf<String>()
    if (width > 0 && height > 0) {
        parts.add("${width}×${height}")
    } else if (height > 0) {
        parts.add("${height}p")
    }
    if (frameRate >= 48f) {
        parts.add("${frameRate.toInt()}fps")
    }
    if (bitrateBps > 0) {
        val mbps = bitrateBps / 1_000_000f
        if (mbps >= 1f) {
            parts.add(String.format(Locale.US, "%.1f Mbps", mbps))
        } else {
            val kbps = bitrateBps / 1_000
            if (kbps > 0) parts.add("$kbps Kbps")
        }
    }
    return parts.joinToString(" • ").ifBlank { "بث مباشر أصلي" }
}

@OptIn(UnstableApi::class)
@Composable
fun LibraryVideoPlayerScreen(
    playbackRequest: PlaybackRequest,
    channelStats: ChannelEngagementStats? = null,
    showChannelViewsAndLikes: Boolean = true,
    onToggleChannelLike: () -> Unit = {},
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    // Immediately lock into Landscape mode, keep screen awake, & enter immersive fullscreen
    DisposableEffect(activity) {
        val originalOrientation = activity?.requestedOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val insetsController = if (window != null) {
            WindowCompat.getInsetsController(window, window.decorView)
        } else null
        insetsController?.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            activity?.requestedOrientation = originalOrientation
            insetsController?.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    val candidateUrls = playbackRequest.candidateUrls
    var currentUrlIndex by remember(playbackRequest) { mutableIntStateOf(0) }
    var isBuffering by remember(playbackRequest) { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var allExtensionsFailed by remember(playbackRequest) { mutableStateOf(false) }
    var liveAutoRetryCount by remember(playbackRequest, currentUrlIndex) { mutableIntStateOf(0) }

    var durationMs by remember(playbackRequest) { mutableLongStateOf(0L) }
    var positionMs by remember(playbackRequest) { mutableLongStateOf(0L) }
    var bufferedPositionMs by remember(playbackRequest) { mutableLongStateOf(0L) }
    var isScrubbing by remember { mutableStateOf(false) }
    var scrubPositionMs by remember { mutableLongStateOf(0L) }

    var isControlsVisible by remember { mutableStateOf(true) }
    var interactionTick by remember { mutableIntStateOf(0) }
    var isScreenLocked by remember { mutableStateOf(false) }
    var aspectMode by remember { mutableStateOf(PlayerAspectMode.FIT) }
    var showAspectToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }

    // Real Stream Quality State (inspected from active ExoPlayer VideoSize & Tracks)
    var realDecodedWidth by remember(playbackRequest, currentUrlIndex) { mutableIntStateOf(0) }
    var realDecodedHeight by remember(playbackRequest, currentUrlIndex) { mutableIntStateOf(0) }
    var realStreamBitrate by remember(playbackRequest, currentUrlIndex) { mutableIntStateOf(0) }
    var realStreamFps by remember(playbackRequest, currentUrlIndex) { mutableFloatStateOf(0f) }
    var availableQualityTracks by remember(playbackRequest, currentUrlIndex) {
        mutableStateOf<List<RealVideoQualityOption>>(emptyList())
    }
    var availableAudioTracks by remember(playbackRequest, currentUrlIndex) {
        mutableStateOf<List<RealMediaTrackOption>>(emptyList())
    }
    var availableSubtitleTracks by remember(playbackRequest, currentUrlIndex) {
        mutableStateOf<List<RealMediaTrackOption>>(emptyList())
    }
    var isSubtitlesDisabled by remember(playbackRequest, currentUrlIndex) { mutableStateOf(false) }

    // null = Auto quality ("تلقائي"), non-null = locked to specific RealVideoQualityOption.id
    var selectedQualityTrackId by remember(playbackRequest, currentUrlIndex) {
        mutableStateOf<String?>(null)
    }
    var isQualityPopupOpen by remember { mutableStateOf(false) }
    var isAudioSubsPopupOpen by remember { mutableStateOf(false) }
    var isServersPopupOpen by remember { mutableStateOf(false) }

    // Double-tap / Remote / Button seek feedback (-10s or +10s)
    var seekFeedbackText by remember { mutableStateOf<String?>(null) }
    var seekFeedbackIsForward by remember { mutableStateOf(true) }
    var seekFeedbackTrigger by remember { mutableIntStateOf(0) }

    // Smart BackHandler: closes popups first, or exits player cleanly
    BackHandler {
        when {
            isQualityPopupOpen || isAudioSubsPopupOpen || isServersPopupOpen -> {
                isQualityPopupOpen = false
                isAudioSubsPopupOpen = false
                isServersPopupOpen = false
            }
            isScreenLocked && !isControlsVisible -> {
                isControlsVisible = true
                interactionTick++
            }
            else -> onBack()
        }
    }

    // Ultra-Fast, Balanced, Zero-Stutter ExoPlayer Engine tuned separately for:
    // 1. Live Broadcast Channels (400ms ultra-fast start + instant server response)
    // 2. Library VOD Movies & Series (450ms instant start + 95s forward buffer + 30s RAM back-buffer + closest-sync hardware seek)
    val exoPlayer = remember(context, playbackRequest.isLiveChannel) {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("VLC/3.0.20 LibVLC/3.0.20")
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(if (playbackRequest.isLiveChannel) 7_500 else 12_000)
            .setReadTimeoutMs(if (playbackRequest.isLiveChannel) 14_000 else 25_000)
            .setKeepPostFor302Redirects(true)

        val dataSourceFactory = DefaultDataSource.Factory(context, httpDataSourceFactory)

        val extractorsFactory = DefaultExtractorsFactory()
            .setConstantBitrateSeekingEnabled(true)
            .setTsExtractorFlags(
                DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES or
                    DefaultTsPayloadReaderFactory.FLAG_DETECT_ACCESS_UNITS
            )

        val loadControl = if (playbackRequest.isLiveChannel) {
            DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    /* minBufferMs = */ 10_000,
                    /* maxBufferMs = */ 42_000,
                    /* bufferForPlaybackMs = */ 400,
                    /* bufferForPlaybackAfterRebufferMs = */ 900
                )
                .setBackBuffer(
                    /* backBufferDurationMs = */ 10_000,
                    /* retainBackBufferFromKeyframe = */ true
                )
                .setPrioritizeTimeOverSizeThresholds(true)
                .build()
        } else {
            DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    /* minBufferMs = */ 25_000,
                    /* maxBufferMs = */ 95_000,
                    /* bufferForPlaybackMs = */ 450,
                    /* bufferForPlaybackAfterRebufferMs = */ 1_000
                )
                .setBackBuffer(
                    /* backBufferDurationMs = */ 30_000,
                    /* retainBackBufferFromKeyframe = */ true
                )
                .setPrioritizeTimeOverSizeThresholds(true)
                .build()
        }

        val renderersFactory = DefaultRenderersFactory(context)
            .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
            .setEnableDecoderFallback(true)

        ExoPlayer.Builder(context, renderersFactory)
            .setMediaSourceFactory(DefaultMediaSourceFactory(dataSourceFactory, extractorsFactory))
            .setLoadControl(loadControl)
            .setSeekBackIncrementMs(10_000L)
            .setSeekForwardIncrementMs(10_000L)
            .setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT)
            .build()
            .apply {
                if (!playbackRequest.isLiveChannel) {
                    setSeekParameters(SeekParameters.CLOSEST_SYNC)
                }
                playWhenReady = true
            }
    }

    // Load current candidate URL (Live Server or VOD mp4 -> mkv -> m3u8) cleanly and rapidly
    LaunchedEffect(currentUrlIndex, allExtensionsFailed, playbackRequest) {
        if (!allExtensionsFailed && currentUrlIndex in candidateUrls.indices) {
            isBuffering = true
            exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                .buildUpon()
                .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                .build()
            selectedQualityTrackId = null

            val urlToPlay = candidateUrls[currentUrlIndex].trim()
            val parsedUri = when {
                urlToPlay.startsWith("file://") -> Uri.parse(urlToPlay)
                urlToPlay.startsWith("/") -> Uri.fromFile(File(urlToPlay))
                else -> Uri.parse(urlToPlay)
            }
            val mediaItem = if (playbackRequest.isLiveChannel) {
                MediaItem.Builder()
                    .setUri(parsedUri)
                    .setLiveConfiguration(
                        MediaItem.LiveConfiguration.Builder()
                            .setTargetOffsetMs(3_500L)
                            .setMinPlaybackSpeed(0.97f)
                            .setMaxPlaybackSpeed(1.03f)
                            .build()
                    )
                    .build()
            } else {
                MediaItem.fromUri(parsedUri)
            }
            exoPlayer.setMediaItem(mediaItem, /* resetPosition = */ true)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        } else if (candidateUrls.isEmpty()) {
            isBuffering = false
            allExtensionsFailed = true
        }
    }

    // Helper to inspect video, audio, and subtitle tracks & active video format from ExoPlayer
    val updateRealQualityFromPlayer: (Tracks, VideoSize) -> Unit = remember(exoPlayer) {
        { tracks: Tracks, videoSize: VideoSize ->
            val discoveredVideo = mutableListOf<RealVideoQualityOption>()
            val discoveredAudio = mutableListOf<RealMediaTrackOption>()
            val discoveredSubs = mutableListOf<RealMediaTrackOption>()
            var activeFormatHeight = 0
            var activeFormatWidth = 0
            var activeFormatBitrate = 0
            var activeFormatFps = 0f

            for (groupIndex in 0 until tracks.groups.size) {
                val group = tracks.groups[groupIndex]
                val mediaTrackGroup = group.mediaTrackGroup
                when (group.type) {
                    C.TRACK_TYPE_VIDEO -> {
                        for (trackIndex in 0 until group.length) {
                            val format = group.getTrackFormat(trackIndex)
                            val w = if (format.width > 0) format.width else 0
                            val h = if (format.height > 0) format.height else 0
                            val br = if (format.bitrate > 0) format.bitrate else 0
                            val fps = if (format.frameRate > 0f) format.frameRate else 0f

                            if (group.isTrackSelected(trackIndex)) {
                                if (h > 0) activeFormatHeight = h
                                if (w > 0) activeFormatWidth = w
                                if (br > 0) activeFormatBitrate = br
                                if (fps > 0f) activeFormatFps = fps
                            }

                            if (group.isTrackSupported(trackIndex) && (h > 0 || w > 0 || br > 0)) {
                                val badge = formatResolutionShortBadge(w, h)
                                val subtitle = formatQualityDetailSubtitle(w, h, br, fps)
                                val optionId = "${groupIndex}_${trackIndex}_${w}x${h}_$br"
                                discoveredVideo.add(
                                    RealVideoQualityOption(
                                        id = optionId,
                                        trackGroup = mediaTrackGroup,
                                        trackIndex = trackIndex,
                                        width = w,
                                        height = h,
                                        bitrateBps = br,
                                        frameRate = fps,
                                        shortBadge = badge,
                                        detailSubtitle = subtitle
                                    )
                                )
                            }
                        }
                    }

                    C.TRACK_TYPE_AUDIO -> {
                        for (trackIndex in 0 until group.length) {
                            if (!group.isTrackSupported(trackIndex)) continue
                            val format = group.getTrackFormat(trackIndex)
                            val lang = format.language?.uppercase(Locale.US).orEmpty()
                            val label = format.label?.takeIf { it.isNotBlank() }
                                ?: if (lang.isNotBlank()) "مسار صوتي ($lang)" else "مسار صوتي ${discoveredAudio.size + 1}"
                            discoveredAudio.add(
                                RealMediaTrackOption(
                                    id = "aud_${groupIndex}_$trackIndex",
                                    trackGroup = mediaTrackGroup,
                                    trackIndex = trackIndex,
                                    label = label,
                                    language = lang,
                                    isSelected = group.isTrackSelected(trackIndex)
                                )
                            )
                        }
                    }

                    C.TRACK_TYPE_TEXT -> {
                        for (trackIndex in 0 until group.length) {
                            if (!group.isTrackSupported(trackIndex)) continue
                            val format = group.getTrackFormat(trackIndex)
                            val lang = format.language?.uppercase(Locale.US).orEmpty()
                            val label = format.label?.takeIf { it.isNotBlank() }
                                ?: if (lang.isNotBlank()) "ترجمة ($lang)" else "ترجمة ${discoveredSubs.size + 1}"
                            discoveredSubs.add(
                                RealMediaTrackOption(
                                    id = "sub_${groupIndex}_$trackIndex",
                                    trackGroup = mediaTrackGroup,
                                    trackIndex = trackIndex,
                                    label = label,
                                    language = lang,
                                    isSelected = group.isTrackSelected(trackIndex)
                                )
                            )
                        }
                    }
                }
            }

            val sortedDistinctVideo = discoveredVideo
                .distinctBy { "${it.height}_${it.width}_${it.bitrateBps / 50_000}" }
                .sortedWith(
                    compareByDescending<RealVideoQualityOption> { it.height }
                        .thenByDescending { it.width }
                        .thenByDescending { it.bitrateBps }
                )

            availableQualityTracks = sortedDistinctVideo
            availableAudioTracks = discoveredAudio
            availableSubtitleTracks = discoveredSubs

            val finalWidth = if (videoSize.width > 0) videoSize.width else activeFormatWidth
            val finalHeight = if (videoSize.height > 0) videoSize.height else activeFormatHeight
            if (finalWidth > 0) realDecodedWidth = finalWidth
            if (finalHeight > 0) realDecodedHeight = finalHeight
            if (activeFormatBitrate > 0) realStreamBitrate = activeFormatBitrate
            if (activeFormatFps > 0f) realStreamFps = activeFormatFps
        }
    }

    // Attach ExoPlayer listener for playback state, real video resolution, multi-track quality detection, and instant recovery
    DisposableEffect(exoPlayer, candidateUrls) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        isBuffering = true
                    }
                    Player.STATE_READY -> {
                        isBuffering = false
                        allExtensionsFailed = false
                        liveAutoRetryCount = 0
                        durationMs = exoPlayer.duration.coerceAtLeast(0L)
                        updateRealQualityFromPlayer(exoPlayer.currentTracks, exoPlayer.videoSize)
                    }
                    Player.STATE_ENDED -> {
                        isBuffering = false
                        isPlaying = false
                    }
                    Player.STATE_IDLE -> Unit
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onVideoSizeChanged(videoSize: VideoSize) {
                if (videoSize.width > 0 && videoSize.height > 0) {
                    realDecodedWidth = videoSize.width
                    realDecodedHeight = videoSize.height
                }
                updateRealQualityFromPlayer(exoPlayer.currentTracks, videoSize)
            }

            override fun onTracksChanged(tracks: Tracks) {
                updateRealQualityFromPlayer(tracks, exoPlayer.videoSize)
            }

            override fun onPlayerError(error: PlaybackException) {
                if (playbackRequest.isLiveChannel) {
                    // Instant recovery if live stream fell behind live window
                    if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                        isBuffering = true
                        exoPlayer.seekToDefaultPosition()
                        exoPlayer.prepare()
                        exoPlayer.playWhenReady = true
                        return
                    }
                    // Instant auto-reconnect up to 2 times on transient live stream hiccup, then auto-failover to next server
                    if (liveAutoRetryCount < 2) {
                        liveAutoRetryCount += 1
                        isBuffering = true
                        exoPlayer.prepare()
                        exoPlayer.playWhenReady = true
                    } else if (currentUrlIndex + 1 < candidateUrls.size) {
                        liveAutoRetryCount = 0
                        isBuffering = true
                        currentUrlIndex += 1
                    } else {
                        isBuffering = false
                        allExtensionsFailed = true
                    }
                } else {
                    // Silent & rapid fallback to next candidate URL (for VOD preferred ext -> mp4 -> mkv -> m3u8)
                    if (currentUrlIndex + 1 < candidateUrls.size) {
                        isBuffering = true
                        currentUrlIndex += 1
                    } else {
                        isBuffering = false
                        allExtensionsFailed = true
                    }
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // Poll playback position & buffer smoothly for VOD seekbar without interrupting user scrubbing
    LaunchedEffect(exoPlayer, isPlaying, playbackRequest.isLiveChannel, isScrubbing) {
        if (playbackRequest.isLiveChannel) return@LaunchedEffect
        while (true) {
            if (!isScrubbing) {
                positionMs = exoPlayer.currentPosition.coerceAtLeast(0L)
            }
            bufferedPositionMs = exoPlayer.bufferedPosition.coerceAtLeast(0L)
            val dur = exoPlayer.duration
            if (dur > 0L) durationMs = dur
            delay(180L)
        }
    }

    // Auto-hide controls after 4.5 seconds of active playback (resets whenever user interacts via Remote, Mouse, or Touch)
    LaunchedEffect(
        isControlsVisible,
        isPlaying,
        isBuffering,
        isScrubbing,
        isQualityPopupOpen,
        isAudioSubsPopupOpen,
        isServersPopupOpen,
        interactionTick
    ) {
        if (
            isControlsVisible &&
            isPlaying &&
            !isBuffering &&
            !allExtensionsFailed &&
            !isScrubbing &&
            !isQualityPopupOpen &&
            !isAudioSubsPopupOpen &&
            !isServersPopupOpen
        ) {
            delay(4500L)
            isControlsVisible = false
        }
    }

    // Hide seek feedback badge after 700ms
    LaunchedEffect(seekFeedbackTrigger) {
        if (seekFeedbackText != null) {
            delay(700L)
            seekFeedbackText = null
        }
    }

    // Hide aspect ratio / status toast after 1.3s
    LaunchedEffect(showAspectToast, aspectMode, toastMessage) {
        if (showAspectToast) {
            delay(1300L)
            showAspectToast = false
        }
    }

    // Helper for accurate ±10s seeking (Left = -10s Rewind, Right = +10s Forward)
    val seekByDeltaMs: (Long) -> Unit = { deltaMs ->
        val totalDur = exoPlayer.duration.takeIf { it > 0L } ?: durationMs.takeIf { it > 0L } ?: Long.MAX_VALUE
        val targetPos = (exoPlayer.currentPosition + deltaMs).coerceIn(0L, totalDur)
        exoPlayer.seekTo(targetPos)
        positionMs = targetPos
        scrubPositionMs = targetPos
        seekFeedbackIsForward = deltaMs > 0L
        seekFeedbackText = if (deltaMs > 0L) "+10 ثواني تقديم" else "-10 ثواني تأخير"
        seekFeedbackTrigger++
        interactionTick++
    }

    // Helper to toggle Mute/Unmute cleanly
    val toggleMute: () -> Unit = {
        val nextMuted = !isMuted
        isMuted = nextMuted
        exoPlayer.volume = if (nextMuted) 0f else 1f
        toastMessage = if (nextMuted) "تم كتم الصوت" else "تم تفعيل الصوت"
        showAspectToast = true
        interactionTick++
    }

    // Compute the real quality badge text displayed on the Quality Icon Button
    val realActiveResolutionBadge = remember(realDecodedWidth, realDecodedHeight, availableQualityTracks, selectedQualityTrackId, isBuffering) {
        val lockedOption = availableQualityTracks.firstOrNull { it.id == selectedQualityTrackId }
        when {
            lockedOption != null -> lockedOption.shortBadge
            realDecodedHeight > 0 || realDecodedWidth > 0 -> formatResolutionShortBadge(realDecodedWidth, realDecodedHeight)
            availableQualityTracks.isNotEmpty() -> availableQualityTracks.first().shortBadge
            else -> "HD تلقائي"
        }
    }

    val realResolutionDimensionsText = remember(realDecodedWidth, realDecodedHeight, realStreamBitrate, realStreamFps) {
        formatQualityDetailSubtitle(realDecodedWidth, realDecodedHeight, realStreamBitrate, realStreamFps)
    }

    val hasMultipleQualities = availableQualityTracks.size > 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            // Universal TV Remote Control (D-Pad) & PC Keyboard Handler
            .onPreviewKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    interactionTick++
                    when (event.key) {
                        Key.MediaPlayPause -> {
                            if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                            isControlsVisible = true
                            true
                        }
                        Key.MediaPlay -> {
                            exoPlayer.play()
                            isControlsVisible = true
                            true
                        }
                        Key.MediaPause -> {
                            exoPlayer.pause()
                            isControlsVisible = true
                            true
                        }
                        Key.MediaFastForward -> {
                            if (!isScreenLocked && !playbackRequest.isLiveChannel && !allExtensionsFailed) {
                                seekByDeltaMs(10_000L)
                            } else if (playbackRequest.isLiveChannel && playbackRequest.servers.size > 1) {
                                allExtensionsFailed = false
                                liveAutoRetryCount = 0
                                isBuffering = true
                                currentUrlIndex = (currentUrlIndex + 1) % playbackRequest.servers.size
                            }
                            true
                        }
                        Key.MediaRewind -> {
                            if (!isScreenLocked && !playbackRequest.isLiveChannel && !allExtensionsFailed) {
                                seekByDeltaMs(-10_000L)
                            } else if (playbackRequest.isLiveChannel && playbackRequest.servers.size > 1) {
                                allExtensionsFailed = false
                                liveAutoRetryCount = 0
                                isBuffering = true
                                currentUrlIndex = (currentUrlIndex - 1 + playbackRequest.servers.size) % playbackRequest.servers.size
                            }
                            true
                        }
                        Key.ChannelUp, Key.PageUp -> {
                            if (playbackRequest.isLiveChannel && playbackRequest.servers.size > 1 && !isScreenLocked) {
                                allExtensionsFailed = false
                                liveAutoRetryCount = 0
                                isBuffering = true
                                currentUrlIndex = (currentUrlIndex + 1) % playbackRequest.servers.size
                                toastMessage = "تبديل إلى: ${playbackRequest.servers[currentUrlIndex].name}"
                                showAspectToast = true
                                true
                            } else false
                        }
                        Key.ChannelDown, Key.PageDown -> {
                            if (playbackRequest.isLiveChannel && playbackRequest.servers.size > 1 && !isScreenLocked) {
                                allExtensionsFailed = false
                                liveAutoRetryCount = 0
                                isBuffering = true
                                currentUrlIndex = (currentUrlIndex - 1 + playbackRequest.servers.size) % playbackRequest.servers.size
                                toastMessage = "تبديل إلى: ${playbackRequest.servers[currentUrlIndex].name}"
                                showAspectToast = true
                                true
                            } else false
                        }
                        Key.M, Key.VolumeMute -> {
                            toggleMute()
                            true
                        }
                        Key.F -> {
                            if (!isScreenLocked) {
                                aspectMode = when (aspectMode) {
                                    PlayerAspectMode.FIT -> PlayerAspectMode.ZOOM
                                    PlayerAspectMode.ZOOM -> PlayerAspectMode.FILL
                                    PlayerAspectMode.FILL -> PlayerAspectMode.FIT
                                }
                                toastMessage = aspectMode.labelAr
                                showAspectToast = true
                                true
                            } else false
                        }
                        Key.DirectionLeft -> {
                            // If HUD is hidden in VOD, D-Pad Left seeks -10s immediately; otherwise reveals HUD / moves D-Pad focus
                            if (!isControlsVisible && !isScreenLocked && !allExtensionsFailed) {
                                if (!playbackRequest.isLiveChannel) {
                                    seekByDeltaMs(-10_000L)
                                } else {
                                    isControlsVisible = true
                                }
                                true
                            } else {
                                false
                            }
                        }
                        Key.DirectionRight -> {
                            // If HUD is hidden in VOD, D-Pad Right seeks +10s immediately; otherwise reveals HUD / moves D-Pad focus
                            if (!isControlsVisible && !isScreenLocked && !allExtensionsFailed) {
                                if (!playbackRequest.isLiveChannel) {
                                    seekByDeltaMs(10_000L)
                                } else {
                                    isControlsVisible = true
                                }
                                true
                            } else {
                                false
                            }
                        }
                        Key.DirectionUp, Key.DirectionDown -> {
                            if (!isControlsVisible) {
                                isControlsVisible = true
                                true
                            } else {
                                false
                            }
                        }
                        else -> {
                            if (event.key.isConfirmKey() && !isControlsVisible && !allExtensionsFailed) {
                                isControlsVisible = true
                                true
                            } else {
                                false
                            }
                        }
                    }
                } else {
                    false
                }
            }
            // PC Mouse Move Detection: reveals HUD controls automatically when moving the mouse cursor
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.type == PointerEventType.Move) {
                            if (!isControlsVisible) {
                                isControlsVisible = true
                            }
                            interactionTick++
                        }
                    }
                }
            }
            // Touch / Mouse Click & Double-Tap Gestures
            .pointerInput(playbackRequest.isLiveChannel, allExtensionsFailed, isQualityPopupOpen, isAudioSubsPopupOpen, isServersPopupOpen, isScreenLocked) {
                detectTapGestures(
                    onTap = {
                        interactionTick++
                        if (isQualityPopupOpen || isAudioSubsPopupOpen || isServersPopupOpen) {
                            isQualityPopupOpen = false
                            isAudioSubsPopupOpen = false
                            isServersPopupOpen = false
                        } else {
                            isControlsVisible = !isControlsVisible
                        }
                    },
                    onDoubleTap = { offset ->
                        if (!isScreenLocked && !playbackRequest.isLiveChannel && !allExtensionsFailed) {
                            val screenWidth = size.width
                            // Universal Video Timeline Convention:
                            // Right side of screen -> +10s forward (تقديم 10 ثواني)
                            // Left side of screen -> -10s backward (تأخير 10 ثواني)
                            if (offset.x > screenWidth / 2f) {
                                seekByDeltaMs(10_000L)
                            } else {
                                seekByDeltaMs(-10_000L)
                            }
                        }
                    }
                )
            }
    ) {
        // 1. Hardware-Accelerated Video Surface
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    resizeMode = aspectMode.resizeMode
                    keepScreenOn = true
                    isFocusable = false
                    isFocusableInTouchMode = false
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { playerView ->
                if (playerView.player !== exoPlayer) {
                    playerView.player = exoPlayer
                }
                if (playerView.resizeMode != aspectMode.resizeMode) {
                    playerView.resizeMode = aspectMode.resizeMode
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. Pure Circular Buffering Indicator WITHOUT ANY TEXT ("من دون وجود نصوص جاري تجهيز عرض سينما... لا اريد ذلك اريد فقط دائرة ومن دون نصوص")
        if (isBuffering && !allExtensionsFailed) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = FoulBottomBarBlack.copy(alpha = 0.72f),
                    shape = CircleShape,
                    border = BorderStroke(
                        width = 1.2.dp,
                        brush = Brush.sweepGradient(
                            colors = listOf(FoulPurpleBright, FoulTeamAwayCyan, FoulPurpleBright)
                        )
                    ),
                    modifier = Modifier.size(66.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = FoulPurpleBright,
                            trackColor = Color.White.copy(alpha = 0.14f),
                            strokeWidth = 3.5.dp,
                            modifier = Modifier
                                .size(38.dp)
                                .testTag("player_loading_spinner")
                        )
                    }
                }
            }
        }

        // 3. Error State if all extensions / active server failed
        if (allExtensionsFailed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FoulBlackBg.copy(alpha = 0.94f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = FoulCardDark),
                    border = BorderStroke(1.dp, FoulLiveRed.copy(alpha = 0.6f)),
                    modifier = Modifier.widthIn(max = 460.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "خطأ في التشغيل",
                            tint = FoulLiveRed,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "تعذر تشغيل هذا المصدر حالياً",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                        if (playbackRequest.isLiveChannel && playbackRequest.servers.size > 1) {
                            Text(
                                text = "اختر سيرفر بث آخر فوراً من القائمة أو أعد المحاولة",
                                style = MaterialTheme.typography.bodySmall,
                                color = FoulTextSecondary
                            )
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                itemsIndexed(playbackRequest.servers) { idx, srv ->
                                    val isCurrent = idx == currentUrlIndex
                                    Surface(
                                        color = if (isCurrent) FoulPurpleBright else FoulCardElevated,
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .tvFocusableClickable(
                                                shape = RoundedCornerShape(10.dp),
                                                scaleOnFocus = 1.06f,
                                                onClick = {
                                                    allExtensionsFailed = false
                                                    liveAutoRetryCount = 0
                                                    isBuffering = true
                                                    currentUrlIndex = idx
                                                }
                                            )
                                    ) {
                                        Text(
                                            text = srv.name,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    allExtensionsFailed = false
                                    liveAutoRetryCount = 0
                                    isBuffering = true
                                    val urlToRetry = candidateUrls.getOrNull(currentUrlIndex)
                                    if (urlToRetry != null) {
                                        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(urlToRetry)), true)
                                        exoPlayer.prepare()
                                        exoPlayer.playWhenReady = true
                                    } else {
                                        currentUrlIndex = 0
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright),
                                modifier = Modifier
                                    .tvFocusHighlight(shape = RoundedCornerShape(20.dp))
                                    .testTag("player_retry_button")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("إعادة المحاولة", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = onBack,
                                colors = ButtonDefaults.buttonColors(containerColor = FoulCardElevated),
                                modifier = Modifier
                                    .tvFocusHighlight(shape = RoundedCornerShape(20.dp))
                                    .testTag("player_error_back_button")
                            ) {
                                Text("رجوع", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // 4. Double-Tap / Remote / Button Seek Feedback Badge (Pinned to Physical Left for -10s and Physical Right for +10s)
        AnimatedVisibility(
            visible = seekFeedbackText != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(
                if (seekFeedbackIsForward) AbsoluteAlignment.CenterRight else AbsoluteAlignment.CenterLeft
            )
        ) {
            Surface(
                color = FoulPurpleDeep.copy(alpha = 0.92f),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.2.dp, FoulTeamAwayCyan.copy(alpha = 0.65f)),
                modifier = Modifier.padding(horizontal = 42.dp)
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (seekFeedbackIsForward) Icons.Default.Forward10 else Icons.Default.Replay10,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                        Text(
                            text = seekFeedbackText.orEmpty(),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // 5. Aspect Ratio / Action Toast Badge
        AnimatedVisibility(
            visible = showAspectToast,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 74.dp)
        ) {
            Surface(
                color = FoulPurpleDeep.copy(alpha = 0.92f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, FoulPurpleBright)
            ) {
                Text(
                    text = toastMessage.ifBlank { aspectMode.labelAr },
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                )
            }
        }

        // 6.A SCREEN LOCKED FLOATING UNLOCK PILL (Prevents accidental touches during live match or movie)
        AnimatedVisibility(
            visible = isControlsVisible && isScreenLocked && !allExtensionsFailed,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 26.dp)
        ) {
            Surface(
                color = FoulBottomBarBlack.copy(alpha = 0.90f),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.2.dp, FoulPurpleBright),
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .tvFocusableClickable(
                        shape = RoundedCornerShape(24.dp),
                        scaleOnFocus = 1.06f,
                        onClick = {
                            isScreenLocked = false
                            toastMessage = "تم فتح قفل التحكم"
                            showAspectToast = true
                        }
                    )
                    .testTag("player_unlock_screen_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "إلغاء قفل الشاشة",
                        tint = FoulGoldStar,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "الشاشة مقفلة • اضغط لفتح التحكم",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        // 6.B FULL PROFESSIONAL PLAYER CONTROLS OVERLAY (Live Broadcast HUD & Library Cinema HUD)
        AnimatedVisibility(
            visible = isControlsVisible && !isScreenLocked && !allExtensionsFailed,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.86f),
                                Color.Black.copy(alpha = 0.14f),
                                Color.Black.copy(alpha = 0.18f),
                                Color.Black.copy(alpha = 0.92f)
                            )
                        )
                    )
            ) {
                // ==================== TOP BAR ====================
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Title, Subtitle & Back Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(FoulCardDark.copy(alpha = 0.88f))
                                .tvFocusHighlight(shape = CircleShape, scaleOnFocus = 1.08f)
                                .border(1.dp, Color.White.copy(alpha = 0.28f), CircleShape)
                                .testTag("player_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (playbackRequest.isLiveChannel) {
                                    Surface(
                                        color = FoulLiveRed,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.5.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.White)
                                            )
                                            Text(
                                                text = "بث مباشر",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    }
                                } else {
                                    Surface(
                                        color = FoulPurpleDeep.copy(alpha = 0.9f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(0.8.dp, FoulPurpleBright.copy(alpha = 0.6f))
                                    ) {
                                        Text(
                                            text = "CINEMA PRO",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                                            color = FoulTeamAwayCyan,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = playbackRequest.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            if (playbackRequest.subtitle.isNotBlank()) {
                                Text(
                                    text = playbackRequest.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.78f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Spacer(Modifier.width(10.dp))

                    // Top Bar Action Pills: Views & Likes (Live), Mute, Real Stream Quality, Audio/Subs (VOD), Speed (VOD), Aspect Ratio, Screen Lock
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        // Live Channel Engagement Pills (Views Counter + Like Button)
                        if (playbackRequest.isLiveChannel && showChannelViewsAndLikes) {
                            val viewsCount = channelStats?.views ?: 0L
                            val likesCount = channelStats?.likes ?: 0L
                            val isLiked = channelStats?.isLikedByMe == true

                            Surface(
                                color = FoulCardDark.copy(alpha = 0.88f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                    .testTag("player_views_counter_pill")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = "عدد المشاهدات",
                                        tint = FoulTeamAwayCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "$viewsCount",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Surface(
                                color = if (isLiked) FoulLiveRed.copy(alpha = 0.90f) else FoulPurpleDeep.copy(alpha = 0.88f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .tvFocusableClickable(
                                        shape = RoundedCornerShape(12.dp),
                                        scaleOnFocus = 1.06f,
                                        onClick = { onToggleChannelLike() }
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isLiked) Color.White else Color.White.copy(alpha = 0.35f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .testTag("player_like_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "إعجاب بالقناة",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "$likesCount",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // TOD-Style Dedicated Servers Popup Trigger in Top Bar (For Live Channels)
                        if (playbackRequest.isLiveChannel && playbackRequest.servers.isNotEmpty()) {
                            Surface(
                                color = if (isServersPopupOpen) FoulPurpleBright else FoulCardDark.copy(alpha = 0.88f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .tvFocusableClickable(
                                        shape = RoundedCornerShape(12.dp),
                                        scaleOnFocus = 1.06f,
                                        onClick = {
                                            isQualityPopupOpen = false
                                            isAudioSubsPopupOpen = false
                                            isServersPopupOpen = !isServersPopupOpen
                                        }
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isServersPopupOpen) Color.White else FoulPurpleBright.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .testTag("player_servers_popup_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Dns,
                                        contentDescription = "سيرفرات البث",
                                        tint = FoulLiveGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "السيرفرات (${playbackRequest.servers.size})",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }

                        // Real Stream Quality Indicator & Selector Button
                        Surface(
                            color = if (isQualityPopupOpen) FoulPurpleBright else FoulPurpleDeep.copy(alpha = 0.90f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .tvFocusableClickable(
                                    shape = RoundedCornerShape(12.dp),
                                    scaleOnFocus = 1.06f,
                                    onClick = {
                                        isAudioSubsPopupOpen = false
                                        isServersPopupOpen = false
                                        isQualityPopupOpen = !isQualityPopupOpen
                                    }
                                )
                                .border(
                                    width = 1.dp,
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(FoulTeamAwayCyan, FoulPurpleBright, Color.White.copy(alpha = 0.5f))
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .testTag("player_quality_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = if (hasMultipleQualities) Icons.Default.Tune else Icons.Default.HighQuality,
                                    contentDescription = "جودة البث الفعلية",
                                    tint = FoulTeamAwayCyan,
                                    modifier = Modifier.size(17.dp)
                                )
                                Text(
                                    text = if (selectedQualityTrackId == null && hasMultipleQualities) {
                                        "تلقائي • $realActiveResolutionBadge"
                                    } else {
                                        realActiveResolutionBadge
                                    },
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                if (hasMultipleQualities) {
                                    Surface(
                                        color = FoulTeamAwayCyan.copy(alpha = 0.24f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "${availableQualityTracks.size}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = FoulTeamAwayCyan,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Audio & Subtitle Track Button for Library VOD (Always accessible in Library Player)
                        if (!playbackRequest.isLiveChannel) {
                            Surface(
                                color = if (isAudioSubsPopupOpen) FoulPurpleBright else FoulCardDark.copy(alpha = 0.88f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .tvFocusableClickable(
                                        shape = RoundedCornerShape(12.dp),
                                        scaleOnFocus = 1.06f,
                                        onClick = {
                                            isQualityPopupOpen = false
                                            isAudioSubsPopupOpen = !isAudioSubsPopupOpen
                                        }
                                    )
                                    .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                    .testTag("player_audio_subs_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Subtitles,
                                        contentDescription = "الصوت والترجمة",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "الصوت والترجمة",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Playback Speed Pill for Library VOD (0.75x -> 1x -> 1.25x -> 1.5x -> 2x)
                        if (!playbackRequest.isLiveChannel) {
                            Surface(
                                color = FoulCardDark.copy(alpha = 0.88f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .tvFocusableClickable(
                                        shape = RoundedCornerShape(12.dp),
                                        scaleOnFocus = 1.06f,
                                        onClick = {
                                            val nextSpeed = when (playbackSpeed) {
                                                1.0f -> 1.25f
                                                1.25f -> 1.5f
                                                1.5f -> 2.0f
                                                2.0f -> 0.75f
                                                else -> 1.0f
                                            }
                                            playbackSpeed = nextSpeed
                                            exoPlayer.playbackParameters = PlaybackParameters(nextSpeed)
                                            toastMessage = "سرعة التشغيل: ${nextSpeed}x"
                                            showAspectToast = true
                                        }
                                    )
                                    .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                    .testTag("player_speed_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
                                        contentDescription = "سرعة التشغيل",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${playbackSpeed}x",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Mute / Unmute Audio Pill (Helpful on TV Box, PC, and Mobile)
                        Surface(
                            color = if (isMuted) FoulLiveRed.copy(alpha = 0.88f) else FoulCardDark.copy(alpha = 0.88f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .tvFocusableClickable(
                                    shape = RoundedCornerShape(12.dp),
                                    scaleOnFocus = 1.06f,
                                    onClick = toggleMute
                                )
                                .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                .testTag("player_mute_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = if (isMuted) "إلغاء كتم الصوت" else "كتم الصوت",
                                    tint = Color.White,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }

                        // Aspect Ratio / Screen Fit Toggle Button
                        Surface(
                            color = FoulCardDark.copy(alpha = 0.88f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .tvFocusableClickable(
                                    shape = RoundedCornerShape(12.dp),
                                    scaleOnFocus = 1.06f,
                                    onClick = {
                                        aspectMode = when (aspectMode) {
                                            PlayerAspectMode.FIT -> PlayerAspectMode.ZOOM
                                            PlayerAspectMode.ZOOM -> PlayerAspectMode.FILL
                                            PlayerAspectMode.FILL -> PlayerAspectMode.FIT
                                        }
                                        toastMessage = aspectMode.labelAr
                                        showAspectToast = true
                                    }
                                )
                                .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                .testTag("player_aspect_ratio_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = when (aspectMode) {
                                        PlayerAspectMode.FIT -> Icons.Default.AspectRatio
                                        PlayerAspectMode.ZOOM -> Icons.Default.Fullscreen
                                        PlayerAspectMode.FILL -> Icons.Default.FullscreenExit
                                    },
                                    contentDescription = "تغيير أبعاد الشاشة",
                                    tint = Color.White,
                                    modifier = Modifier.size(17.dp)
                                )
                                Text(
                                    text = aspectMode.labelAr,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Screen Lock Button (Prevents accidental touches)
                        Surface(
                            color = FoulCardDark.copy(alpha = 0.88f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .tvFocusableClickable(
                                    shape = RoundedCornerShape(12.dp),
                                    scaleOnFocus = 1.06f,
                                    onClick = {
                                        isQualityPopupOpen = false
                                        isAudioSubsPopupOpen = false
                                        isServersPopupOpen = false
                                        isScreenLocked = true
                                        toastMessage = "تم قفل أزرار المشغل"
                                        showAspectToast = true
                                    }
                                )
                                .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                .testTag("player_lock_screen_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = "قفل الشاشة",
                                    tint = Color.White,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }
                    }
                }

                // ==================== CENTER TRANSPORT CONTROLS (LTR ORDER) ====================
                if (!isBuffering) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(42.dp)
                        ) {
                            if (!playbackRequest.isLiveChannel) {
                                // LEFT: -10s Rewind Button (Glassmorphic Cinema Pill)
                                IconButton(
                                    onClick = { seekByDeltaMs(-10_000L) },
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(FoulBottomBarBlack.copy(alpha = 0.82f))
                                        .tvFocusHighlight(shape = CircleShape, scaleOnFocus = 1.1f)
                                        .border(
                                            width = 1.5.dp,
                                            brush = Brush.linearGradient(
                                                colors = listOf(Color.White.copy(alpha = 0.45f), FoulPurpleBright)
                                            ),
                                            shape = CircleShape
                                        )
                                        .testTag("player_replay_10_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Replay10,
                                        contentDescription = "تأخير 10 ثواني",
                                        tint = Color.White,
                                        modifier = Modifier.size(31.dp)
                                    )
                                }
                            }

                            // CENTER: Play / Pause Button
                            IconButton(
                                onClick = {
                                    interactionTick++
                                    if (exoPlayer.isPlaying) {
                                        exoPlayer.pause()
                                    } else {
                                        exoPlayer.play()
                                    }
                                },
                                modifier = Modifier
                                    .size(74.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(FoulPurpleBright, FoulPurpleDeep)
                                        )
                                    )
                                    .tvFocusHighlight(shape = CircleShape, scaleOnFocus = 1.1f)
                                    .border(2.dp, Color.White, CircleShape)
                                    .testTag("player_center_play_pause_button")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            if (!playbackRequest.isLiveChannel) {
                                // RIGHT: +10s Forward Button (Glassmorphic Cinema Pill)
                                IconButton(
                                    onClick = { seekByDeltaMs(10_000L) },
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(FoulBottomBarBlack.copy(alpha = 0.82f))
                                        .tvFocusHighlight(shape = CircleShape, scaleOnFocus = 1.1f)
                                        .border(
                                            width = 1.5.dp,
                                            brush = Brush.linearGradient(
                                                colors = listOf(FoulTeamAwayCyan, Color.White.copy(alpha = 0.45f))
                                            ),
                                            shape = CircleShape
                                        )
                                        .testTag("player_forward_10_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Forward10,
                                        contentDescription = "تقديم 10 ثواني",
                                        tint = Color.White,
                                        modifier = Modifier.size(31.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // ==================== BOTTOM BAR ====================
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                ) {
                    if (playbackRequest.isLiveChannel) {
                        // ==================== TOD-PRO LIVE BROADCAST DOCK (مشغل الرئيسية الاحترافي بتصميم TOD وتنظيم مثالي للسيرفرات لجميع الأجهزة) ====================
                        val activeServerObj = playbackRequest.servers.getOrNull(currentUrlIndex)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Top Strip of Live Dock: TOD Live Edge Indicator + Active Server Info + Sync & Quality Actions
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Left: TOD-Style LIVE Broadcast Status Pill + Active Server Badge
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    // Live Sync Edge Pill ("LIVE • مباشر مستقر")
                                    Surface(
                                        color = FoulLiveRed.copy(alpha = 0.92f),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.45f)),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .tvFocusableClickable(
                                                shape = RoundedCornerShape(10.dp),
                                                scaleOnFocus = 1.06f,
                                                onClick = {
                                                    interactionTick++
                                                    if (exoPlayer.isCurrentMediaItemLive && exoPlayer.duration > 0) {
                                                        exoPlayer.seekToDefaultPosition()
                                                    } else {
                                                        exoPlayer.prepare()
                                                    }
                                                    exoPlayer.playWhenReady = true
                                                    toastMessage = "تمت مزامنة البث المباشر بأقصى سرعة"
                                                    showAspectToast = true
                                                }
                                            )
                                            .testTag("live_sync_edge_button")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.White)
                                            )
                                            Text(
                                                text = "LIVE • مباشر مستقر",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    }

                                    if (activeServerObj != null) {
                                        Surface(
                                            color = FoulBottomBarBlack.copy(alpha = 0.78f),
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(1.dp, FoulPurpleBright.copy(alpha = 0.6f))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Sensors,
                                                    contentDescription = null,
                                                    tint = FoulLiveGreen,
                                                    modifier = Modifier.size(15.dp)
                                                )
                                                Text(
                                                    text = "السيرفر النشط: ${activeServerObj.name} (${currentUrlIndex + 1}/${playbackRequest.servers.size})",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }

                                // Right: Quick Next Server + All Servers Modal + Real Resolution Badge
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (playbackRequest.servers.size > 1) {
                                        Surface(
                                            color = FoulBottomBarBlack.copy(alpha = 0.82f),
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(1.dp, FoulPurpleBright.copy(alpha = 0.75f)),
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .tvFocusableClickable(
                                                    shape = RoundedCornerShape(10.dp),
                                                    scaleOnFocus = 1.06f,
                                                    onClick = {
                                                        interactionTick++
                                                        allExtensionsFailed = false
                                                        liveAutoRetryCount = 0
                                                        isBuffering = true
                                                        currentUrlIndex = (currentUrlIndex + 1) % playbackRequest.servers.size
                                                        toastMessage = "تم التبديل إلى: ${playbackRequest.servers[currentUrlIndex].name}"
                                                        showAspectToast = true
                                                    }
                                                )
                                                .testTag("live_next_server_button")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.SkipNext,
                                                    contentDescription = "السيرفر التالي",
                                                    tint = FoulTeamAwayCyan,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Text(
                                                    text = "السيرفر التالي",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    // Real Verified Stream Resolution Pill
                                    Surface(
                                        color = FoulBottomBarBlack.copy(alpha = 0.82f),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, FoulTeamAwayCyan.copy(alpha = 0.65f)),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .tvFocusableClickable(
                                                shape = RoundedCornerShape(10.dp),
                                                scaleOnFocus = 1.06f,
                                                onClick = {
                                                    interactionTick++
                                                    isAudioSubsPopupOpen = false
                                                    isServersPopupOpen = false
                                                    isQualityPopupOpen = !isQualityPopupOpen
                                                }
                                            )
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Hd,
                                                contentDescription = null,
                                                tint = FoulTeamAwayCyan,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = if (realDecodedWidth > 0 && realDecodedHeight > 0) {
                                                    "$realActiveResolutionBadge (${realDecodedWidth}×${realDecodedHeight})"
                                                } else {
                                                    realActiveResolutionBadge
                                                },
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            // TOD-Style Dedicated Full-Width colorful progress indicator line for Live Broadcast
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                FoulLiveRed,
                                                FoulPurpleBright,
                                                FoulTeamAwayCyan,
                                                FoulLiveGreen
                                            )
                                        )
                                    )
                            )

                            // Dedicated Full-Width Multi-Server Strip (Never cramped, works seamlessly on Phone, Tablet, TV, TV Box, and PC)
                            if (playbackRequest.servers.isNotEmpty()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        color = FoulBottomBarBlack.copy(alpha = 0.82f),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, FoulDivider)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Dns,
                                                contentDescription = null,
                                                tint = FoulPurpleBright,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Text(
                                                text = "سيرفرات البث (${playbackRequest.servers.size}):",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    }

                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        contentPadding = PaddingValues(horizontal = 2.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        itemsIndexed(
                                            items = playbackRequest.servers,
                                            key = { idx, srv -> "${srv.id}_$idx" }
                                        ) { idx, srv ->
                                            val selected = idx == currentUrlIndex
                                            Surface(
                                                color = if (selected) {
                                                    FoulPurpleBright
                                                } else {
                                                    FoulBottomBarBlack.copy(alpha = 0.85f)
                                                },
                                                shape = RoundedCornerShape(11.dp),
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(11.dp))
                                                    .tvFocusableClickable(
                                                        shape = RoundedCornerShape(11.dp),
                                                        scaleOnFocus = 1.07f,
                                                        onClick = {
                                                            interactionTick++
                                                            if (currentUrlIndex != idx || allExtensionsFailed) {
                                                                allExtensionsFailed = false
                                                                liveAutoRetryCount = 0
                                                                isBuffering = true
                                                                currentUrlIndex = idx
                                                                toastMessage = "جاري التشغيل على: ${srv.name}"
                                                                showAspectToast = true
                                                            }
                                                        }
                                                    )
                                                    .border(
                                                        width = if (selected) 1.5.dp else 1.dp,
                                                        color = if (selected) Color.White else FoulPurpleBright.copy(alpha = 0.45f),
                                                        shape = RoundedCornerShape(11.dp)
                                                    )
                                                    .testTag("live_server_pill_$idx")
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    // Server Number Badge (#1, #2...)
                                                    Surface(
                                                        color = if (selected) Color.Black.copy(alpha = 0.35f) else FoulPurpleDeep,
                                                        shape = RoundedCornerShape(6.dp)
                                                    ) {
                                                        Text(
                                                            text = "#${idx + 1}",
                                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                            color = if (selected) FoulGoldStar else FoulTeamAwayCyan,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp)
                                                        )
                                                    }

                                                    // Active Live Dot
                                                    Box(
                                                        modifier = Modifier
                                                            .size(7.dp)
                                                            .clip(CircleShape)
                                                            .background(
                                                                if (selected) FoulLiveGreen else Color.White.copy(alpha = 0.35f)
                                                            )
                                                    )

                                                    // Server Name
                                                    Text(
                                                        text = srv.name,
                                                        style = MaterialTheme.typography.labelLarge,
                                                        color = Color.White,
                                                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                                        maxLines = 1
                                                    )

                                                    // Stream Type Tag (M3U8 / TS)
                                                    if (srv.streamType.isNotBlank()) {
                                                        Surface(
                                                            color = Color.White.copy(alpha = if (selected) 0.22f else 0.10f),
                                                            shape = RoundedCornerShape(5.dp)
                                                        ) {
                                                            Text(
                                                                text = srv.streamType.uppercase(Locale.US),
                                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                                                color = Color.White,
                                                                fontWeight = FontWeight.Bold,
                                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // ==================== LEGENDARY LIBRARY VOD CINEMA CONSOLE (من دون المستطيل إلي تحت • شريط ملون وخلفية شفافة بالكامل) ====================
                        val activeDisplayPositionMs = if (isScrubbing) scrubPositionMs else positionMs
                        val safeDurationMs = durationMs.coerceAtLeast(1L)
                        val progressFraction = (activeDisplayPositionMs.toFloat() / safeDurationMs.toFloat()).coerceIn(0f, 1f)
                        val bufferedFraction = (bufferedPositionMs.toFloat() / safeDurationMs.toFloat()).coerceIn(0f, 1f)
                        val remainingMs = (durationMs - activeDisplayPositionMs).coerceAtLeast(0L)
                        var isTimelineFocused by remember { mutableStateOf(false) }

                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Transparent)
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                // Frameless Colorful Timeline Bar ("شريط ملون وخلفية يكون بشكل شفاف ومن دون المستطيل إلي تحت")
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(32.dp)
                                        .onFocusChanged { isTimelineFocused = it.isFocused || it.hasFocus }
                                        .onKeyEvent { event ->
                                            if (event.type == KeyEventType.KeyDown) {
                                                when (event.key) {
                                                    Key.DirectionLeft -> {
                                                        seekByDeltaMs(-10_000L)
                                                        true
                                                    }
                                                    Key.DirectionRight -> {
                                                        seekByDeltaMs(10_000L)
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            } else false
                                        }
                                        .focusable(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Background & Buffered Track Layer over Transparent Backdrop
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(if (isTimelineFocused || isScrubbing) 9.dp else 7.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(Color.White.copy(alpha = 0.22f))
                                            .then(
                                                if (isTimelineFocused) {
                                                    Modifier.border(1.dp, Color.White, RoundedCornerShape(50))
                                                } else Modifier
                                            )
                                    ) {
                                        // Buffered Progress Bar
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(bufferedFraction)
                                                .height(if (isTimelineFocused || isScrubbing) 9.dp else 7.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(Color.White.copy(alpha = 0.45f))
                                        )
                                        // Vibrant Multi-Color Progress Bar ("شريط ملون")
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(progressFraction)
                                                .height(if (isTimelineFocused || isScrubbing) 9.dp else 7.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(
                                                    Brush.horizontalGradient(
                                                        colors = listOf(
                                                            FoulPurpleBright,
                                                            Color(0xFFEC4899),
                                                            FoulTeamAwayCyan,
                                                            FoulLiveGreen,
                                                            FoulGoldStar
                                                        )
                                                    )
                                                )
                                        )
                                    }

                                    // Interactive Smooth Slider on top
                                    Slider(
                                        value = activeDisplayPositionMs.toFloat().coerceIn(0f, safeDurationMs.toFloat()),
                                        onValueChange = { newVal ->
                                            interactionTick++
                                            isScrubbing = true
                                            scrubPositionMs = newVal.toLong()
                                        },
                                        onValueChangeFinished = {
                                            val targetSeek = scrubPositionMs.coerceIn(0L, safeDurationMs)
                                            exoPlayer.seekTo(targetSeek)
                                            positionMs = targetSeek
                                            isScrubbing = false
                                        },
                                        valueRange = 0f..safeDurationMs.toFloat(),
                                        colors = SliderDefaults.colors(
                                            thumbColor = if (isTimelineFocused || isScrubbing) FoulGoldStar else Color.White,
                                            activeTrackColor = Color.Transparent,
                                            inactiveTrackColor = Color.Transparent
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("player_timeline_slider")
                                    )
                                }

                                // Frameless Transparent Bottom Info & Quick Transport Row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Transparent),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // Quick -10s / +10s Controls with subtle transparent glass
                                        Surface(
                                            color = Color.White.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .tvFocusableClickable(
                                                    shape = RoundedCornerShape(8.dp),
                                                    scaleOnFocus = 1.08f,
                                                    onClick = { seekByDeltaMs(-10_000L) }
                                                )
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Replay10,
                                                    contentDescription = "-10s",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Text(
                                                    text = "-10s",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Surface(
                                            color = Color.White.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .tvFocusableClickable(
                                                    shape = RoundedCornerShape(8.dp),
                                                    scaleOnFocus = 1.08f,
                                                    onClick = { seekByDeltaMs(10_000L) }
                                                )
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Forward10,
                                                    contentDescription = "+10s",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Text(
                                                    text = "+10s",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Text(
                                                text = formatDurationMs(activeDisplayPositionMs),
                                                style = MaterialTheme.typography.labelLarge,
                                                color = if (isScrubbing) FoulGoldStar else Color.White,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                            Text(
                                                text = "/",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color.White.copy(alpha = 0.6f)
                                            )
                                            Text(
                                                text = formatDurationMs(durationMs),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color.White.copy(alpha = 0.88f),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }

                                    // Center Status Text directly over transparent background
                                    Text(
                                        text = if (isScrubbing) {
                                            "الانتقال السريع إلى ${formatDurationMs(scrubPositionMs)}"
                                        } else if (realDecodedWidth > 0 && realDecodedHeight > 0) {
                                            "$realActiveResolutionBadge • ${realDecodedWidth}×${realDecodedHeight}"
                                        } else {
                                            ""
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isScrubbing) FoulGoldStar else Color.White.copy(alpha = 0.85f),
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Right: Remaining Time directly on transparent background
                                    Text(
                                        text = "-${formatDurationMs(remainingMs)}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = FoulTeamAwayCyan,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 7. COMPACT PHONE/TV-FRIENDLY REAL STREAM QUALITY SELECTOR POPUP
        AnimatedVisibility(
            visible = isQualityPopupOpen && !isScreenLocked,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 64.dp, start = 18.dp, end = 18.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FoulBottomBarBlack.copy(alpha = 0.96f)),
                border = BorderStroke(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(FoulTeamAwayCyan, FoulPurpleBright)
                    )
                ),
                modifier = Modifier
                    .widthIn(min = 230.dp, max = 285.dp)
                    .heightIn(max = 265.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
                    .testTag("player_quality_popup")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Compact Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HighQuality,
                                contentDescription = null,
                                tint = FoulTeamAwayCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text(
                                    text = "جودة بث السيرفر الفعلية",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = if (realDecodedWidth > 0 && realDecodedHeight > 0) {
                                        "نشط الآن: $realActiveResolutionBadge • $realResolutionDimensionsText"
                                    } else {
                                        "نشط الآن: $realActiveResolutionBadge"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = FoulTeamAwayCyan
                                )
                            }
                        }

                        IconButton(
                            onClick = { isQualityPopupOpen = false },
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(FoulCardDark)
                                .tvFocusHighlight(shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    OrderedColorfulDivider(thickness = 1.dp)

                    // Scrollable Compact Quality Options List
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Option 1: Auto Quality ("تلقائي")
                        item(key = "quality_auto") {
                            val isAutoSelected = selectedQualityTrackId == null
                            Surface(
                                color = if (isAutoSelected) FoulPurpleDeep.copy(alpha = 0.85f) else FoulCardDark,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (isAutoSelected) FoulPurpleBright else FoulDivider
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .tvFocusableClickable(
                                        shape = RoundedCornerShape(10.dp),
                                        scaleOnFocus = 1.03f,
                                        onClick = {
                                            selectedQualityTrackId = null
                                            exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                                                .buildUpon()
                                                .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                                                .build()
                                            isQualityPopupOpen = false
                                        }
                                    )
                                    .testTag("quality_option_auto")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = if (isAutoSelected) FoulGoldStar else Color.White.copy(alpha = 0.7f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "تلقائي (موصى به)",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                            Text(
                                                text = if (realDecodedHeight > 0) {
                                                    "يضبط الجودة تلقائياً ($realActiveResolutionBadge)"
                                                } else {
                                                    "أفضل استقرار وسرعة بدون تقطيع"
                                                },
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = FoulTextSecondary
                                            )
                                        }
                                    }
                                    if (isAutoSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = FoulLiveGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Discovered Stream Quality Tracks
                        if (availableQualityTracks.isNotEmpty()) {
                            items(
                                items = availableQualityTracks,
                                key = { it.id }
                            ) { option ->
                                val isSelected = selectedQualityTrackId == option.id
                                Surface(
                                    color = if (isSelected) FoulPurpleDeep.copy(alpha = 0.85f) else FoulCardDark,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (isSelected) FoulTeamAwayCyan else FoulDivider
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .tvFocusableClickable(
                                            shape = RoundedCornerShape(10.dp),
                                            scaleOnFocus = 1.03f,
                                            onClick = {
                                                selectedQualityTrackId = option.id
                                                val override = TrackSelectionOverride(
                                                    option.trackGroup,
                                                    listOf(option.trackIndex)
                                                )
                                                exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                                                    .buildUpon()
                                                    .clearOverridesOfType(C.TRACK_TYPE_VIDEO)
                                                    .addOverride(override)
                                                    .build()
                                                isQualityPopupOpen = false
                                            }
                                        )
                                        .testTag("quality_option_${option.height}")
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = option.shortBadge,
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                            Text(
                                                text = option.detailSubtitle,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = FoulTextSecondary
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = FoulLiveGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        } else if (realDecodedWidth > 0 && realDecodedHeight > 0) {
                            // Single native quality stream info row when stream doesn't expose separate HLS variant tracks
                            item(key = "native_single_quality") {
                                Surface(
                                    color = FoulCardDark,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, FoulDivider),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "$realActiveResolutionBadge (جودة المصدر الأصلية)",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "الدقة الفعلية: ${realDecodedWidth}×${realDecodedHeight}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = FoulTextSecondary
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = FoulTeamAwayCyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 8. COMPACT AUDIO & SUBTITLES SELECTOR POPUP (For Library Movies & Series)
        AnimatedVisibility(
            visible = isAudioSubsPopupOpen && !isScreenLocked,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 64.dp, start = 18.dp, end = 18.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FoulBottomBarBlack.copy(alpha = 0.96f)),
                border = BorderStroke(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(FoulPurpleBright, FoulTeamAwayCyan)
                    )
                ),
                modifier = Modifier
                    .widthIn(min = 235.dp, max = 290.dp)
                    .heightIn(max = 275.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
                    .testTag("player_audio_subs_popup")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Subtitles,
                                contentDescription = null,
                                tint = FoulTeamAwayCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "المسارات الصوتية والترجمة",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        IconButton(
                            onClick = { isAudioSubsPopupOpen = false },
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(FoulCardDark)
                                .tvFocusHighlight(shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    OrderedColorfulDivider(thickness = 1.dp)

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (availableAudioTracks.isNotEmpty()) {
                            item {
                                Text(
                                    text = "الصوت (${availableAudioTracks.size}):",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FoulTeamAwayCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            items(availableAudioTracks, key = { it.id }) { track ->
                                Surface(
                                    color = if (track.isSelected) FoulPurpleDeep.copy(alpha = 0.85f) else FoulCardDark,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (track.isSelected) FoulPurpleBright else FoulDivider
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .tvFocusableClickable(
                                            shape = RoundedCornerShape(10.dp),
                                            scaleOnFocus = 1.03f,
                                            onClick = {
                                                val override = TrackSelectionOverride(
                                                    track.trackGroup,
                                                    listOf(track.trackIndex)
                                                )
                                                exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                                                    .buildUpon()
                                                    .clearOverridesOfType(C.TRACK_TYPE_AUDIO)
                                                    .addOverride(override)
                                                    .build()
                                                isAudioSubsPopupOpen = false
                                            }
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = track.label,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (track.isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = FoulLiveGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            item {
                                Surface(
                                    color = FoulCardDark,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, FoulDivider),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "المسار الصوتي الأصلي (نشط)",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = FoulLiveGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (availableSubtitleTracks.isNotEmpty()) {
                            item {
                                Text(
                                    text = "الترجمة (${availableSubtitleTracks.size}):",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FoulGoldStar,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            item(key = "sub_off") {
                                Surface(
                                    color = if (isSubtitlesDisabled) FoulPurpleDeep.copy(alpha = 0.85f) else FoulCardDark,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, if (isSubtitlesDisabled) FoulPurpleBright else FoulDivider),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .tvFocusableClickable(
                                            shape = RoundedCornerShape(10.dp),
                                            scaleOnFocus = 1.03f,
                                            onClick = {
                                                isSubtitlesDisabled = true
                                                exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                                                    .buildUpon()
                                                    .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
                                                    .build()
                                                isAudioSubsPopupOpen = false
                                            }
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "إيقاف الترجمة",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (isSubtitlesDisabled) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = FoulLiveGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            items(availableSubtitleTracks, key = { it.id }) { subTrack ->
                                val selected = !isSubtitlesDisabled && subTrack.isSelected
                                Surface(
                                    color = if (selected) FoulPurpleDeep.copy(alpha = 0.85f) else FoulCardDark,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, if (selected) FoulTeamAwayCyan else FoulDivider),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .tvFocusableClickable(
                                            shape = RoundedCornerShape(10.dp),
                                            scaleOnFocus = 1.03f,
                                            onClick = {
                                                isSubtitlesDisabled = false
                                                val override = TrackSelectionOverride(
                                                    subTrack.trackGroup,
                                                    listOf(subTrack.trackIndex)
                                                )
                                                exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                                                    .buildUpon()
                                                    .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
                                                    .clearOverridesOfType(C.TRACK_TYPE_TEXT)
                                                    .addOverride(override)
                                                    .build()
                                                isAudioSubsPopupOpen = false
                                            }
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = subTrack.label,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (selected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = FoulLiveGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            item {
                                Surface(
                                    color = FoulCardDark,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, FoulDivider),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "الترجمة مدمجة أو غير منفصلة في المصدر",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = FoulTextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 9. TOD-STYLE MULTI-SERVER SELECTOR POPUP (When user clicks "السيرفرات" in Top Bar)
        AnimatedVisibility(
            visible = isServersPopupOpen && !isScreenLocked && playbackRequest.isLiveChannel,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 64.dp, start = 18.dp, end = 18.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FoulBottomBarBlack.copy(alpha = 0.96f)),
                border = BorderStroke(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(FoulPurpleBright, FoulLiveGreen)
                    )
                ),
                modifier = Modifier
                    .widthIn(min = 240.dp, max = 300.dp)
                    .heightIn(max = 290.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
                    .testTag("player_servers_popup")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dns,
                                contentDescription = null,
                                tint = FoulLiveGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text(
                                    text = "سيرفرات القناة المتاحة (${playbackRequest.servers.size})",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "اختر أي سيرفر للتبديل الفوري بدون خروج",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = FoulTextSecondary
                                )
                            }
                        }

                        IconButton(
                            onClick = { isServersPopupOpen = false },
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(FoulCardDark)
                                .tvFocusHighlight(shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    OrderedColorfulDivider(thickness = 1.dp)

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(
                            items = playbackRequest.servers,
                            key = { idx, srv -> "popup_${srv.id}_$idx" }
                        ) { idx, srv ->
                            val isSelected = idx == currentUrlIndex
                            Surface(
                                color = if (isSelected) FoulPurpleDeep.copy(alpha = 0.90f) else FoulCardDark,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (isSelected) FoulLiveGreen else FoulDivider
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .tvFocusableClickable(
                                        shape = RoundedCornerShape(10.dp),
                                        scaleOnFocus = 1.03f,
                                        onClick = {
                                            interactionTick++
                                            allExtensionsFailed = false
                                            liveAutoRetryCount = 0
                                            isBuffering = true
                                            currentUrlIndex = idx
                                            isServersPopupOpen = false
                                            toastMessage = "تم التبديل إلى: ${srv.name}"
                                            showAspectToast = true
                                        }
                                    )
                                    .testTag("popup_server_option_$idx")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 9.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Surface(
                                            color = if (isSelected) FoulLiveGreen else FoulCardElevated,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "#${idx + 1}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (isSelected) Color.Black else FoulTeamAwayCyan,
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = srv.name,
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                            Text(
                                                text = "صيغة البث: ${srv.streamType.uppercase(Locale.US).ifBlank { "M3U8" }}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                color = FoulTextSecondary
                                            )
                                        }
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = FoulLiveGreen,
                                            modifier = Modifier.size(17.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDurationMs(ms: Long): String {
    val totalSeconds = (ms / 1000L).coerceAtLeast(0L)
    val hours = totalSeconds / 3600L
    val minutes = (totalSeconds % 3600L) / 60L
    val seconds = totalSeconds % 60L
    return if (hours > 0) {
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}

private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
