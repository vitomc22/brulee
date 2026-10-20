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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Code-drawn pastry mascot; a short spring celebration follows each real milestone. */
@Composable
fun BruleeMascot(milestone: Any = Unit, size: Dp = 88.dp, reducedMotion: Boolean = LocalReducedMotion.current) {
    val bounce = remember { Animatable(0f) }
    LaunchedEffect(milestone, reducedMotion) {
        bounce.snapTo(0f)
        if (!reducedMotion) {
            bounce.animateTo(-0.12f, tween(160, easing = FastOutSlowInEasing))
            bounce.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
        }
    }
    Canvas(Modifier.size(size).testTag("brulee_mascot").semantics {
        contentDescription = "Telie, sua ajudante de confeitaria"
        stateDescription = if (reducedMotion) "Movimento reduzido" else "Animação habilitada"
    }.graphicsLayer { translationY = bounce.value * size.toPx(); rotationZ = bounce.value * 30f }) {
        val w = this.size.width
        val h = this.size.height
        drawOval(Color(0xFFE2BA73).copy(alpha = .2f), Offset(w * .13f, h * .86f), Size(w * .74f, h * .1f))
        val cake = Path().apply {
            moveTo(w * .18f, h * .42f); lineTo(w * .82f, h * .42f)
            lineTo(w * .73f, h * .84f); quadraticTo(w * .5f, h * .98f, w * .27f, h * .84f); close()
        }
        drawPath(cake, Color(0xFFE2BA73))
        for (i in 0..3) {
            val x = w * (.31f + .125f * i)
            drawLine(Color(0xFFB88C42).copy(alpha = .35f), Offset(x, h * .53f), Offset(x, h * .81f), w * .025f)
        }
        drawOval(Color(0xFF3B523E), Offset(w * .13f, h * .27f), Size(w * .74f, h * .3f))
        drawCircle(Color(0xFFCFEACF), w * .17f, Offset(w * .36f, h * .31f))
        drawCircle(Color(0xFFCFEACF), w * .19f, Offset(w * .57f, h * .26f))
        drawCircle(Color(0xFFCFEACF), w * .14f, Offset(w * .73f, h * .35f))
        drawCircle(Color(0xFF3B523E), w * .035f, Offset(w * .38f, h * .63f))
        drawCircle(Color(0xFF3B523E), w * .035f, Offset(w * .62f, h * .63f))
        drawCircle(Color.White, w * .01f, Offset(w * .37f, h * .62f))
        drawCircle(Color.White, w * .01f, Offset(w * .61f, h * .62f))
        drawCircle(Color(0xFFF0A58A), w * .045f, Offset(w * .29f, h * .7f))
        drawCircle(Color(0xFFF0A58A), w * .045f, Offset(w * .71f, h * .7f))
        drawArc(Color(0xFF3B523E), 15f, 150f, false, Offset(w * .41f, h * .66f), Size(w * .18f, h * .13f), style = Stroke(w * .025f))
    }
}
