package com.example.warre_welvaert_project_android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.warre_welvaert_project_android.model.Food
import com.example.warre_welvaert_project_android.R

@Composable
fun TextInputField(modifier: Modifier = Modifier, label: String, defaultText: String, onValueChanged: (String) -> Unit ) {
    var text by remember { mutableStateOf(defaultText) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = text,
            onValueChange = {
                text = it
                onValueChanged(text)
            },
            modifier = modifier
                .width(330.dp)
                .height(52.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
fun NumberInputField( modifier: Modifier = Modifier, label: String, default: String, onValueChanged: (String) -> Unit ) {
    var number by remember { mutableStateOf(default) }
    Column(
        modifier = modifier.padding(top = 15.dp, bottom = 15.dp)
    ) {
        Text(text = label)
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = number,
            onValueChange = {
                number = it
                onValueChanged(number)
            },
            modifier = modifier
                .width(330.dp)
                .height(52.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
fun SimpleRadioButtonComponent( modifier: Modifier = Modifier, options: List<String>, onSelectionChanged: (String) -> Unit) {
    var selectedOption by remember { mutableStateOf(options[options.lastIndex]) }
    Row {
        options.forEach { text ->
            Row(
                modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            selectedOption = text
                            onSelectionChanged(selectedOption)
                        }
                    )
                    .padding(horizontal = 10.dp)
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    modifier = modifier.padding(all = 0.dp),
                    onClick = {
                        selectedOption = text
                        onSelectionChanged(selectedOption)
                    }
                )
                Spacer(modifier = modifier.width(3.dp))
                Text(
                    text = text,
                    modifier.padding(top = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
            Spacer(modifier = modifier.width(50.dp))
        }
    }
}

@Composable
fun DropDownMenu( modifier: Modifier = Modifier, options: List<Food>, onSelectionChanged: (Food) -> Unit ) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(options[0].name) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Box(modifier = modifier
        .padding(start = 15.dp, end = 15.dp)
        .wrapContentSize(Alignment.TopStart)) {
        OutlinedTextField(
            value = selected,
            onValueChange = { selected = it },
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            trailingIcon = {
                Icon(icon,"")
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier.fillMaxWidth()
        ) {
            options.forEach { item ->
                DropdownMenuItem(onClick = {
                    selected = item.name
                    onSelectionChanged(item)
                    expanded = false
                }) {
                    Text(text = item.name)
                }
            }
        }
    }
}

@Composable
fun SimpleAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: String
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = stringResource(R.string.confirm)) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = stringResource(R.string.cancel)) }
            },
            title = { Text(text = title ) },
            text = { Text(text = text ) }
        )
    }
}

@Composable
fun EmptyScreen(title: String, instructions: String) {
    Column (
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Spacer(Modifier.height(50.dp))
        Text(text = title, fontSize = 24.sp)
        Spacer(Modifier.height(20.dp))
        Icon(Icons.Filled.Search, contentDescription = null, Modifier.size(80.dp))
        Spacer(Modifier.height(20.dp))
        Text(text = instructions, fontSize = 16.sp)
    }
}