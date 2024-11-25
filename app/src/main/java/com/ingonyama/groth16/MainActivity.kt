package com.ingonyama.groth16
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingonyama.groth16.ui.theme.HelloRustAndroidTheme
import java.io.File

class MainActivity : ComponentActivity() {
    companion object {
        init {
            System.loadLibrary("groth16")
        }
        external fun initLogger()
        external fun Groth16(filename1: String, filename2: String): String

    }

    private var selectedFilePath by mutableStateOf("/data/data/com.ingonyama.groth16/witness.wts")
    private var selectedFilePath2 by mutableStateOf("/data/data/com.ingonyama.groth16/prover_key.zkey")
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var filePickerLauncher2: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLogger()
        enableEdgeToEdge()

        filePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                val path = uri.path?.replace("document/raw:", "") ?: "/data/data/com.ingonyama.groth16/witness.wts"
                selectedFilePath = path
                Log.d("MainActivity", "Witness file: $selectedFilePath")
            }
        }

        filePickerLauncher2 = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                val path = uri.path?.replace("document/raw:", "") ?: "/data/data/com.ingonyama.groth16/prover_key.zkey"
                selectedFilePath2 = path
                Log.d("MainActivity", "ZKey file: $selectedFilePath2")
            }
        }

        setContent {
            HelloRustAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        ProjectTitle()
                        FileSelectorButton()
                        FileSelectorButton2()
                        StartComputationButton()
                        ComputationOutput(output = "Output from Groth16")
                    }
                }
            }
        }
    }

    @Composable
    fun ProjectTitle() {
        Text(
            text = "Groth16 on Icicle",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }

    @Composable
    fun FileSelectorButton() {
        Button(onClick = {
            filePickerLauncher.launch(arrayOf("*/*"))
        }, modifier = Modifier.padding(8.dp)) {
            Text(text = "Select Witness File: $selectedFilePath")
        }
    }

    @Composable
    fun FileSelectorButton2() {
        Button(onClick = {
            filePickerLauncher2.launch(arrayOf("*/*"))
        }, modifier = Modifier.padding(8.dp)) {
            Text(text = "Select ZKey File: $selectedFilePath2")
        }
    }

    @Composable
    fun StartComputationButton() {
        var output by remember { mutableStateOf("") }
        val selectedFile1 = selectedFilePath
        val selectedFile2 = selectedFilePath2

        Button(onClick = {
            output = Groth16(selectedFile1, selectedFile2)
            Log.d("MainActivity", "Computation result: $output")
        }, modifier = Modifier.padding(8.dp)) {
            Text(text = "Start Computation")
        }

        ComputationOutput(output = output)
    }

    @Composable
    fun ComputationOutput(output: String = "") {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(8.dp)
        ) {
            Text(
                text = " Output",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.DarkGray)
                    .fillMaxSize()
            )
            BasicTextField(
                value = output,
                onValueChange = {},
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.LightGray)
                    .fillMaxSize(),
                readOnly = true
            )
        }
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

