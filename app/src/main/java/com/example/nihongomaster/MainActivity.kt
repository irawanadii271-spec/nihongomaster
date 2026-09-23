package com.example.nihongomaster

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.nihongomaster.ui.components.NihongoWebViewScreen
import com.example.nihongomaster.ui.components.TARGET_URL
import com.example.nihongomaster.ui.theme.NihongoMasterTheme

class MainActivity : ComponentActivity() {

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NihongoMasterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                    ) {
                        NihongoWebViewScreen(
                            url = TARGET_URL,
                            onBackPressWhenRoot = {
                                val currentTime = System.currentTimeMillis()
                                if (currentTime - backPressedTime < 2000) {
                                    finish()
                                } else {
                                    backPressedTime = currentTime
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Tekan sekali lagi untuk keluar",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

