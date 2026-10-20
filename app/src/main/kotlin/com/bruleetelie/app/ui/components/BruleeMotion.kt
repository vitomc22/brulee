package com.bruleetelie.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

val LocalReducedMotion = staticCompositionLocalOf { false }

/** Finite transitions keep the interface responsive and respect reduced motion. */
@Composable
fun <T> BruleeTransition(target: T, modifier: Modifier = Modifier, order: ((T) -> Int)? = null, content: @Composable (T) -> Unit) {
    val reduced = LocalReducedMotion.current
    AnimatedContent(
        targetState = target,
        modifier = modifier,
        transitionSpec = {
            val direction = if (order != null && order(targetState) < order(initialState)) -1 else 1
            if (reduced) EnterTransition.None togetherWith ExitTransition.None
            else (fadeIn(tween(180)) + slideInHorizontally(tween(240, easing = FastOutSlowInEasing)) { direction * it / 8 })
                .togetherWith(fadeOut(tween(90)) + slideOutHorizontally(tween(220)) { -direction * it / 10 })
        },
        label = "Brulee page transition"
    ) { content(it) }
}

/** Short amount changes retain the form's focus and composition state. */
@Composable
fun BruleeAnimatedAmount(
    value: String,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = androidx.compose.material3.MaterialTheme.typography.titleMedium,
    fontWeight: androidx.compose.ui.text.font.FontWeight? = null,
    color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified
) {
    val reduced = LocalReducedMotion.current
    AnimatedContent(value, transitionSpec = {
        if (reduced) EnterTransition.None togetherWith ExitTransition.None
        else fadeIn(tween(160)).togetherWith(fadeOut(tween(100)))
    }, label = "Quote amount") { amount ->
        androidx.compose.material3.Text(amount, modifier, color = color, style = style, fontWeight = fontWeight)
    }
}
