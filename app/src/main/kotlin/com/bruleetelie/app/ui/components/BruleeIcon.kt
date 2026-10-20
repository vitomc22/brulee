package com.bruleetelie.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/** A single 24 dp, rounded stroke family, independent of the device's emoji font. */
enum class BruleeSymbol(val path: String) {
    Calendar("M4 5 L20 5 L20 21 L4 21 Z M8 2 L8 8 M16 2 L16 8 M4 10 L20 10 M8 14 L10 14 M14 14 L16 14 M8 18 L10 18"),
    Production("M4 8 L4 20 L20 20 L20 8 M8 8 L8 4 L16 4 L16 8 M2 8 L22 8 M8 13 L16 13 M8 17 L13 17"),
    Orders("M6 3 L15 3 L20 8 L20 21 L4 21 L4 3 Z M14 3 L14 9 L20 9 M8 13 L16 13 M8 17 L14 17"),
    Plus("M12 5 L12 19 M5 12 L19 12"),
    Finance("M21 7 L5 7 Q3 7 3 5 Q3 3 5 3 L18 3 L18 7 M3 5 L3 19 Q3 21 5 21 L21 21 L21 7 M21 11 L16 11 Q13 11 13 14 Q13 17 16 17 L21 17 M17 14 L18 14"),
    Stock("M3 7 L12 3 L21 7 L21 18 L12 22 L3 18 Z M3 7 L12 11 L21 7 M12 11 L12 22 M7 5 L17 9"),
    Settings("M9 3 L15 3 L16 6 L19 7 L22 10 L20 13 L20 17 L17 19 L14 19 L12 22 L9 19 L6 19 L3 16 L4 12 L2 9 L5 6 L8 6 Z M16 12 A4 4 0 1 0 8 12 A4 4 0 1 0 16 12"),
    Clock("M21 12 A9 9 0 1 0 3 12 A9 9 0 1 0 21 12 M12 7 L12 12 L16 14"),
    Flame("M12 2 Q15 7 14 10 Q18 8 18 6 Q25 16 18 21 Q12 25 6 20 Q1 15 8 8 Q7 13 10 13 Q13 10 12 2 Z"),
    Check("M4 12 L9 17 L20 6"),
    Send("M3 3 L22 12 L3 21 L7 12 Z M7 12 L22 12"),
    Pdf("M6 3 L15 3 L20 8 L20 21 L4 21 L4 3 Z M14 3 L14 9 L20 9 M8 13 L16 13 M8 17 L14 17"),
    Arrow("M4 12 L20 12 M14 6 L20 12 L14 18"),
    Trend("M3 18 L9 12 L13 15 L21 5 M15 5 L21 5 L21 11"),
    Target("M21 12 A9 9 0 1 0 3 12 A9 9 0 1 0 21 12 M17 12 A5 5 0 1 0 7 12 A5 5 0 1 0 17 12 M12 11 L12 13"),
    Percent("M5 20 L19 4 M9 6 A3 3 0 1 0 3 6 A3 3 0 1 0 9 6 M21 18 A3 3 0 1 0 15 18 A3 3 0 1 0 21 18"),
    Search("M17 10 A7 7 0 1 0 3 10 A7 7 0 1 0 17 10 M15 15 L21 21"),
    Download("M12 3 L12 16 M7 11 L12 16 L17 11 M4 16 L4 21 L20 21 L20 16"),
    Warning("M12 3 L22 21 L2 21 Z M12 9 L12 14 M12 17 L12 18")
}

@Composable
fun BruleeIcon(symbol: BruleeSymbol, modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    val path = remember(symbol) { PathParser().parsePathString(symbol.path).toPath() }
    Canvas(modifier.size(24.dp)) {
        scale(size.width / 24f, size.height / 24f, pivot = Offset.Zero) {
            drawPath(path, tint, style = Stroke(1.7f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}
