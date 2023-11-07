package com.code.temperature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.code.temperature.ui.theme.TemperatureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperatureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                    StatefulTemperatureInput()
                    ConverterApp()
                    TwoWayConverterApp()
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatefulTemperatureInput(
    modifier: Modifier = Modifier,
){
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    Column(modifier.padding(16.dp)){
        Text(
            text = stringResource(R.string.stateful_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = input, 
            label = { Text(text = stringResource(id = R.string.enter_celsius))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {newInput ->
                input = newInput
                output = convertToFahrenheit(newInput)
            },
        )
        Text(stringResource(id = R.string.temperature_fahrenheit, output))
    }
}

private fun convertToFahrenheit(celsius: String) =
    celsius.toDoubleOrNull()?.let {
        (it * 9/5) + 32
    }.toString()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatelesTemperatureInput(
    input: String,
    output: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.stateless_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = input,
            label = {Text(stringResource(id = R.string.enter_celsius))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onValueChange
        )
        Text(stringResource(id = R.string.temperature_fahrenheit, output))
    }
}

@Composable
private fun ConverterApp(
    modifier: Modifier = Modifier,
){
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }

    Column(modifier) {
        StatelesTemperatureInput(
            input = input,
            output = output,
            onValueChange = { newInput->
                input  = newInput
                output = convertToFahrenheit(newInput)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GeneralTemperatureInput(
    scale: Scale,
    input:String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier) {
        OutlinedTextField(
            value = input,
            label = {Text(stringResource(id = R.string.enter_temperature, scale.scaleName))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onValueChange,
        )
    }
}

@Composable
private fun TwoWayConverterApp(
    modifier: Modifier = Modifier,
){
    var celcius by remember { mutableStateOf("") }
    var fahrenheit by remember { mutableStateOf("") }
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.two_way_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        GeneralTemperatureInput(
            scale = Scale.CELCIUS,
            input = celcius,
            onValueChange = {newInput ->
                celcius = newInput
                fahrenheit = convertToFahrenheit(newInput)
            }
        )

        GeneralTemperatureInput(
            scale = Scale.FAHRENHEIT,
            input = fahrenheit,
            onValueChange = {newInput ->
                fahrenheit = newInput
                celcius = convertToCelcius(newInput)
            }
        )
    }
}

fun convertToCelcius(fahrenheit: String) =
    fahrenheit.toDoubleOrNull()?.let {
        (it - 32 ) * 5 / 9
    }.toString()
