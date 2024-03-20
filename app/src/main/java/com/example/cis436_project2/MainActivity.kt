package com.example.cis436_project2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cis436_project2.ui.theme.Cis436_project2Theme
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sqrt

@Composable
fun Button(number: String, onNumberClick: (String) -> Unit) {
    Button(
        onClick = { onNumberClick(number) },
        modifier = Modifier.width(64.dp).height(64.dp)
    ) {
        Text(text = number)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Cis436_project2Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CalculatorLayout()
                }
            }
        }
    }
}

@Composable
fun CalculatorLayout() {
    // State variables
    var currentInput by remember { mutableStateOf("") }
    var previousInput by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf<((Double, Double) -> Double)?>(null) }

    // Function to append a number to the current input
    fun onNumberClick(number: String) {
        currentInput += number
    }

    // Function to set the current operation
    fun onOperationClick(operationFunc: (Double, Double) -> Double) {
        previousInput = currentInput
        currentInput = ""
        operation = operationFunc
    }

    // Function to calculate the result
    fun onEqualsClick() {
        val result = operation?.invoke(previousInput.toDoubleOrNull() ?: 0.0, currentInput.toDoubleOrNull() ?: 0.0)
        currentInput = result.toString()
        previousInput = ""
        operation = null
    }

    // Function to clear the input
    fun onClear() {
        currentInput = ""
        previousInput = ""
        operation = null
    }

    // Function to toggle the sign of the current input
    fun onToggleSign() {
        currentInput = if (currentInput.startsWith("-")) {
            currentInput.drop(1) // Remove the first character, which is the minus sign
        } else {
            "-$currentInput" // Prepend a minus sign
        }
    }

    Column {
        DisplayFragment(currentInput)
        ButtonsFragment(
            OnNumberClick = { number -> onNumberClick(number) },
            OnOperationClick = { operationFunc -> onOperationClick(operationFunc) },
            OnEqualsClick = { onEqualsClick() },
            OnClearClick = { onClear() },
            OnToggleSignClick = { onToggleSign() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayFragment(displayValue: String) {
    OutlinedTextField(
        value = displayValue, // Directly use displayValue here
        onValueChange = { /* read-only, no action needed */ },
        readOnly = true, // Makes the TextField non-editable
        singleLine = true, // Single line input
        keyboardActions = KeyboardActions.Default,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent, // Hides the underline when focused
            unfocusedBorderColor = Color.Transparent, // Hides the underline when not focused
            disabledBorderColor = Color.Transparent // Hides the underline always
        ),
        textStyle = TextStyle(color = Color.Black, fontSize = 24.sp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}


@Composable
fun ButtonsFragment(OnNumberClick: (String) -> Unit,
                    OnOperationClick: ((Double, Double) -> Double) -> Unit,
                    OnEqualsClick: () -> Unit,
                    OnClearClick: () -> Unit,
                    OnToggleSignClick: () -> Unit) {
    // TODO: Implement the calculator buttons
    // Numbers 0 - 9
    Column {
        Row {
            Button("%", onNumberClick = { OnOperationClick { a, b -> a % b } })
            Button("7", onNumberClick = { OnNumberClick("7") })
            Button("8", onNumberClick = { OnNumberClick("8") })
            Button("9", onNumberClick = { OnNumberClick("9") })
            Button("/", onNumberClick = { OnOperationClick { a, b -> a / b }})
        }
        Row {
            Button("Sqrt", onNumberClick = { OnOperationClick { a, b -> sqrt(a) } })
            Button("4", onNumberClick = { OnNumberClick("4") })
            Button("5", onNumberClick = { OnNumberClick("5") })
            Button("6", onNumberClick = { OnNumberClick("6") })
            Button("*", onNumberClick = { OnOperationClick { a, b -> a * b }})
        }
        Row {
            Button("CE", onNumberClick = { OnClearClick() })
            Button("1", onNumberClick = { OnNumberClick("1") })
            Button("2", onNumberClick = { OnNumberClick("2") })
            Button("3", onNumberClick = { OnNumberClick("3") })
            Button("-", onNumberClick = { OnOperationClick { a, b -> a - b}})
        }
        Row {
            Button("C", onNumberClick = { OnClearClick() })
            Button("0", onNumberClick = { OnNumberClick("0") })
            Button(".", onNumberClick = { OnNumberClick(".") })
            Button("+/-", onNumberClick = { OnToggleSignClick() })
            Button("+", onNumberClick = { OnOperationClick { a, b -> a + b}})
        }
        Row {
            Button("=", onNumberClick = { OnEqualsClick() })
        }
    }
}
