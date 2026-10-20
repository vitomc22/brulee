package com.bruleetelie.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class MascotMood { Idle, Processing, Success }

/** Native adaptation of anima: bobbing, blinking, hat squash and shadow pulse. */
@Composable
fun BruleeMascot(milestone: Any = Unit, size: Dp = 88.dp, reducedMotion: Boolean = LocalReducedMotion.current, mood: MascotMood = MascotMood.Idle) {
    val bounce = remember { Animatable(0f) }
    val phase = if (reducedMotion) null else {
        val idle = rememberInfiniteTransition(label = "Mascot idle")
        val progress = idle.animateFloat(0f, 1f, infiniteRepeatable(tween(if (mood == MascotMood.Processing) 2400 else 3800, easing = LinearEasing)), label = "Mascot phase")
        progress
    }
    LaunchedEffect(milestone, reducedMotion, mood) {
        bounce.snapTo(0f)
        if (!reducedMotion) {
            bounce.animateTo(if (mood == MascotMood.Success) -0.14f else -0.07f, tween(160, easing = FastOutSlowInEasing))
            bounce.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
        }
    }
    Canvas(Modifier.size(size).testTag("brulee_mascot").semantics {
        contentDescription = "Telie, sua ajudante de confeitaria"
        stateDescription = if (reducedMotion) "Movimento reduzido" else "Animação habilitada: ${mood.name}"
    }) {
        val progress = phase?.value ?: 0f
        val float = kotlin.math.sin(progress * Math.PI * 2).toFloat() * .018f
        val blink = if (progress > .94f && progress < .98f) .12f else 1f
        val w = this.size.width
        val h = this.size.height
        drawOval(Color(0xFFE2BA73).copy(alpha = .2f), Offset(w * .13f, h * .86f), Size(w * (.74f - kotlin.math.abs(float) * 3), h * .1f))
        translate(top = (bounce.value + float) * h) {
        val cake = Path().apply {
            moveTo(w * .18f, h * .42f); lineTo(w * .82f, h * .42f)
            lineTo(w * .73f, h * .84f); quadraticTo(w * .5f, h * .98f, w * .27f, h * .84f); close()
        }
        drawPath(cake, Color(0xFFE2BA73))
        for (i in 0..3) {
            val x = w * (.31f + .125f * i)
            drawLine(Color(0xFFB88C42).copy(alpha = .35f), Offset(x, h * .53f), Offset(x, h * .81f), w * .025f)
        }
        val hat = Path().apply {
            moveTo(w * .23f, h * .48f)
            lineTo(w * .2f, h * .33f)
            cubicTo(w * .02f, h * .27f, w * .13f, h * .06f, w * .31f, h * .16f)
            cubicTo(w * .34f, h * -.02f, w * .64f, h * -.02f, w * .68f, h * .16f)
            cubicTo(w * .88f, h * .06f, w * .98f, h * .31f, w * .79f, h * .34f)
            lineTo(w * .76f, h * .48f)
            quadraticTo(w * .5f, h * .54f, w * .23f, h * .48f)
            close()
        }
        scale(1f - float * .35f, 1f + float * .7f, pivot = Offset(w * .5f, h * .48f)) {
        drawPath(hat, Color(0xFFFFFDF5))
        drawPath(hat, Color(0xFF3B523E), style = Stroke(w * .018f))
        drawLine(Color(0xFFB88C42), Offset(w * .24f, h * .43f), Offset(w * .75f, h * .43f), w * .018f)
        }
        drawCircle(Color(0xFFFFFDF5), w * .065f, Offset(w * .17f, h * .67f))
        drawCircle(Color(0xFFFFFDF5), w * .065f, Offset(w * .82f, h * .67f))
        scale(1f, blink, pivot = Offset(w * .5f, h * .63f)) {
        drawCircle(Color(0xFF3B523E), w * .035f, Offset(w * .38f, h * .63f))
        drawCircle(Color(0xFF3B523E), w * .035f, Offset(w * .62f, h * .63f))
        drawCircle(Color.White, w * .01f, Offset(w * .37f, h * .62f))
        drawCircle(Color.White, w * .01f, Offset(w * .61f, h * .62f))
        }
        drawCircle(Color(0xFFF0A58A), w * .045f, Offset(w * .29f, h * .7f))
        drawCircle(Color(0xFFF0A58A), w * .045f, Offset(w * .71f, h * .7f))
        drawArc(Color(0xFF3B523E), 15f, 150f, false, Offset(w * .41f, h * .66f), Size(w * .18f, h * .13f), style = Stroke(w * .025f))
        }
    }
}
