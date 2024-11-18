

package com.ingonyama.hellorustandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingonyama.hellorustandroid.ui.theme.HelloRustAndroidTheme
import java.io.File

class MainActivity : ComponentActivity() {
    companion object {
        init {
            System.loadLibrary("groth16")
        }
        external fun initLogger()
        external fun helloFromRust(): String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLogger()
        enableEdgeToEdge()
        setContent {
            HelloRustAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Greeting(name = "Welcome to Groth16 on Icicle!")
                        ProveWithIcicleButton()
                    }
                }
            }
        }
    }

    @Composable
    fun ProveWithIcicleButton() {
        var rustMessage by remember { mutableStateOf("") }

        Button(onClick = {
            rustMessage = helloFromRust()
            Log.d("MainActivity", "Groth16 benchmarks: $rustMessage")
        }) {
            Text(text = "Prove with Icicle")
        }

        Text(text = rustMessage, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloRustAndroidTheme {
        Greeting("Android")
    }
}

