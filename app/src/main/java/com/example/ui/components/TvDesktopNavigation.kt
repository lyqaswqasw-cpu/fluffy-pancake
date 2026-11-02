package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulTeamAwayCyan

/**
 * Universal modifier for Android TV, TV Box (D-Pad / Remote Control), PC (Mouse Hover + Keyboard Enter/Space),
 * and Mobile Touch.
 *
 * - Provides full D-Pad & Keyboard focusability
 * - Highlights focused or hovered elements with a high-contrast neon Cyan-Purple border and subtle scale
 * - Triggers [onClick] on Remote OK / DPAD_CENTER / Enter / NumPadEnter / Spacebar as well as touch/mouse click
 */
fun Modifier.tvFocusableClickable(
    shape: Shape = RoundedCornerShape(12.dp),
    scaleOnFocus: Float = 1.025f,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    var hasDirectFocus by remember { mutableStateOf(false) }

    val highlighted = enabled && (isFocused || isHovered || hasDirectFocus)
    val scale by animateFloatAsState(
        targetValue = if (highlighted) scaleOnFocus else 1f,
        animationSpec = tween(durationMillis = 140),
        label = "tv_focus_scale"
    )

    this
        .scale(scale)
        .onFocusChanged { state ->
            hasDirectFocus = state.isFocused
        }
        .hoverable(interactionSource = interactionSource, enabled = enabled)
        .onKeyEvent { event ->
            if (!enabled) return@onKeyEvent false
            if (event.type == KeyEventType.KeyDown && event.key.isConfirmKey()) {
                onClick()
                true
            } else {
                false
            }
        }
        .then(
            if (highlighted) {
                Modifier.border(
                    width = 2.2.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(FoulTeamAwayCyan, Color.White, FoulPurpleBright)
                    ),
                    shape = shape
                )
            } else {
                Modifier
            }
        )
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
}

/**
 * Adds a high-visibility TV Remote D-Pad & PC Hover highlight ring to standard Compose Buttons / IconButtons
 * that already manage their own click handling.
 */
fun Modifier.tvFocusHighlight(
    shape: Shape = RoundedCornerShape(12.dp),
    scaleOnFocus: Float = 1.04f
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var isFocused by remember { mutableStateOf(false) }

    val highlighted = isFocused || isHovered
    val scale by animateFloatAsState(
        targetValue = if (highlighted) scaleOnFocus else 1f,
        animationSpec = tween(durationMillis = 140),
        label = "tv_btn_focus_scale"
    )

    this
        .scale(scale)
        .onFocusChanged { isFocused = it.isFocused || it.hasFocus }
        .hoverable(interactionSource = interactionSource)
        .then(
            if (highlighted) {
                Modifier.border(
                    width = 2.2.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(FoulTeamAwayCyan, Color.White, FoulPurpleBright)
                    ),
                    shape = shape
                )
            } else {
                Modifier
            }
        )
}

/**
 * Checks whether a hardware key corresponds to TV Remote OK / Center / Gamepad A / PC Enter / Space.
 */
fun Key.isConfirmKey(): Boolean {
    return this == Key.DirectionCenter ||
        this == Key.Enter ||
        this == Key.NumPadEnter ||
        this == Key.Spacebar ||
        this == Key.ButtonA ||
        this == Key.ButtonSelect
}

@Composable
fun rememberIsWideTvOrDesktopScreen(maxWidthDp: Float): Boolean {
    return maxWidthDp >= 680f
}
