package cc.seaotter.bmi.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cc.seaotter.bmi.R

@Composable
fun BMIScreen(
    bmiViewModel: BMIViewModel = viewModel()
) {
    val uiState = bmiViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BMICardLayout(
            onHeightChanged = { bmiViewModel.onHeightChanged(it) },
            onWeightChanged = { bmiViewModel.onWeightChanged(it) },
            onWaistlineChanged = { bmiViewModel.onWaistlineChanged(it) },
            onGenderChanged = { bmiViewModel.onGenderChanged(it) },
            height = uiState.value.height,
            weight = uiState.value.weight,
            waistline = uiState.value.waistline,
            onButtonClick = { bmiViewModel.calculateBMI() }
        )

        when {
            uiState.value.isResultDialogOpen -> {
                ResultDialog(
                    onDismissRequest = { bmiViewModel.onDismissRequest() },
                    onConfirmation = {
                        bmiViewModel.onDismissRequest()
                    },
                    dialogTitle = stringResource(id = R.string.resultTitle),
                    dialogText = "BMI: ${uiState.value.result}",
                    icon = Icons.Default.Info,
                    image = uiState.value.imageResource
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICardLayout(
    onHeightChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onWaistlineChanged: (String) -> Unit,
    onGenderChanged: (Int) -> Unit,
    height: String,
    weight: String,
    waistline: String,
    onButtonClick: () -> Boolean
) {
    var genderSelectedIndex by remember { mutableIntStateOf(0) }
    val genderOptions =
        listOf(stringResource(id = R.string.boy), stringResource(id = R.string.girl))

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column {

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                genderOptions.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = genderOptions.size
                        ),
                        onClick = {
                            genderSelectedIndex = index
                            onGenderChanged(index)
                        },
                        selected = index == genderSelectedIndex
                    ) {
                        Text(label)
                    }
                }
            }

            MyOutlinedTextField(
                value = height,
                onValueChange = onHeightChanged,
                label = stringResource(id = R.string.height)
            )

            MyOutlinedTextField(
                value = weight,
                onValueChange = onWeightChanged,
                label = stringResource(id = R.string.weight)
            )

            MyOutlinedTextField(
                value = waistline,
                onValueChange = onWaistlineChanged,
                label = stringResource(id = R.string.waistline)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                val context = LocalContext.current
                var isInputValid by remember { mutableStateOf(true) }
                Button(onClick = {
                    isInputValid = onButtonClick()
                }) {
                    Text(text = stringResource(id = R.string.calculate))
                }
                when {
                    !isInputValid -> {
                        isInputValid = true
                        Toast.makeText(
                            context,
                            stringResource(id = R.string.inputNotValid),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }

            }
        }
    }
}

@Composable
fun MyOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        )
    )
}


@Composable
fun ResultDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    image: Int = R.drawable.boy,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = stringResource(id = R.string.imageDescription),
                    contentScale = ContentScale.Fit
                )
                Text(text = dialogText)
            }

        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
        }
    )
}