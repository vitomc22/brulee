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
fun <T> BruleeTransition(target: T, modifier: Modifier = Modifier, content: @Composable (T) -> Unit) {
    val reduced = LocalReducedMotion.current
    AnimatedContent(
        targetState = target,
        modifier = modifier,
        transitionSpec = {
            if (reduced) EnterTransition.None togetherWith ExitTransition.None
            else (fadeIn(tween(220, delayMillis = 70)) + slideInHorizontally(tween(300, easing = FastOutSlowInEasing)) { it / 10 })
                .togetherWith(fadeOut(tween(90)) + slideOutHorizontally(tween(220)) { -it / 12 })
        },
        label = "Brulee page transition"
    ) { content(it) }
}
