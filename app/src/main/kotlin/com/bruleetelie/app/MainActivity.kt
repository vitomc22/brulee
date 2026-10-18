package com.bruleetelie.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.bruleetelie.app.ui.BruleeApp
import com.bruleetelie.app.ui.theme.BruleeTheme
import com.bruleetelie.app.backup.AutomaticBackupWorker

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AutomaticBackupWorker.schedule(applicationContext)
        enableEdgeToEdge()
        setContent { BruleeTheme { BruleeApp() } }
    }
}
