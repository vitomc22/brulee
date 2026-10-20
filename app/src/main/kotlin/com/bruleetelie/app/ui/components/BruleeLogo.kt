package com.bruleetelie.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.bruleetelie.app.R
import com.bruleetelie.app.ui.theme.BruleePrimary

/** Official transparent artwork on the brand background, shared by the app header. */
@Composable
fun BruleeLogo(modifier: Modifier = Modifier) {
    Image(painterResource(R.drawable.brulee_logo), contentDescription = "Brûlée Ateliê de Doces",
        modifier = modifier.background(BruleePrimary))
}
