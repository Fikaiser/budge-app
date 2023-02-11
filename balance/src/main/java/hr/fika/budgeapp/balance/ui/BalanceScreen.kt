package hr.fika.budgeapp.balance.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import hr.fika.budgeapp.balance.viewmodel.BalanceViewModel
import hr.fika.budgeapp.common.utils.DateTimeUtils
import hr.fika.budgeapp.design_system.theme.BudgeRoundedCorner
import hr.fika.budgeapp.design_system.ui.animation.LoadingAnimation3
import hr.fika.budgeapp.design_system.ui.button.BudgeButton
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun BalanceScreen(
    viewModel: BalanceViewModel = viewModel()
) {
    val viewState = viewModel.viewState.observeAsState()
    val date = viewModel.date.observeAsState()
    val isRepeating = viewModel.isRepeating.observeAsState()
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,) {
        when (viewState.value) {
            BalanceUiState.LOADING -> LoadingAnimation3()
            BalanceUiState.INITIAL -> {
                var shouldShowDialog by remember { mutableStateOf(false) }
                BudgeButton(text = "Link bank account") {
                    shouldShowDialog = true
                }
                BudgeButton(text = "Get transactions") {
                    viewModel.getTransactions()
                }
                BudgeButton(text = "Get flow") {
                    viewModel.getFlow()
                }

                if (shouldShowDialog) {
                    AlertDialog(
                        onDismissRequest = { },
                        title = {
                            Text(text = "Link bank account")
                        },
                        text = {
                            Text(text = "Would you like to link your bank account")
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.linkBankAccount()
                            })
                            { Text(text = "Yes") }
                        },
                        dismissButton = {
                            TextButton(onClick = {shouldShowDialog = false})
                            { Text(text = "No") }
                        }
                    )
                }
            }
            is BalanceUiState.TRANSACTIONS -> {
                val transactions = (viewState.value as BalanceUiState.TRANSACTIONS).transactions

                transactions.forEach {
                    Text(text = it.toString())
                }
            }
            is BalanceUiState.INCOME -> {
                val transactions = (viewState.value as BalanceUiState.INCOME).transactions

                transactions.forEach {
                    Text(text = it.toString())
                    IconButton(onClick = { viewModel.deleteTransaction(it.id!!) }) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    FloatingActionButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                                onClick = {
                            viewModel.prepEditor()
                        }) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                    }
                }
            }
            BalanceUiState.EDITOR -> {
                RoundedTextField(placeholder = "Description", viewModel)
                RoundedTextField(
                    placeholder = "Amount",
                    viewModel,
                    keyboardType = KeyboardType.Decimal
                )
                LabeledCheckBox(label = "Repeating monthly", isRepeating = isRepeating, viewModel = viewModel)
                RoundedDateField(date = date, viewModel = viewModel, isRepeating = isRepeating)
                BudgeButton(text = "Save") {
                    viewModel.saveTransaction()
                }
                
            }
            else -> TODO()
        }
    }
}

@Composable
fun LabeledCheckBox(label: String, isRepeating: State<Boolean?>, viewModel: BalanceViewModel) {
    Row (verticalAlignment = Alignment.CenterVertically) {
        Text(text = label)
        Checkbox(
            checked = isRepeating.value!!,
            onCheckedChange = {
                if (isRepeating.value != it) {
                    viewModel.updateRepeating(it)
                }
        })
    }
}

@Composable
fun RoundedDateField(
    date: State<LocalDateTime?>,
    isRepeating: State<Boolean?>,
    viewModel: BalanceViewModel
) {
    val dateState = rememberMaterialDialogState()
    val timeState = rememberMaterialDialogState()
    val text = if (isRepeating.value == true) {
        val day = date.value!!.dayOfMonth
        val suffix = DateTimeUtils.getDateSuffix(day)
        "Repeating every $day$suffix of the month "
    } else {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm")
        date.value!!.format(formatter)
    }
    Text(
        modifier = Modifier.clickable {dateState.show()},
        text = text
    )
    MaterialDialog(
        dialogState = dateState,
        buttons = {
            positiveButton("Set")
            negativeButton("Cancel")
        }
    ) {
        datepicker (
            waitForPositiveButton = true
                ) { date ->
            viewModel.updateDate(date)
            if (isRepeating.value == true) {
                viewModel.updateTime(LocalTime.of(0,0))
            } else {
                timeState.show()
            }
        }

    }
    MaterialDialog(
        dialogState = timeState,
        buttons = {
            positiveButton("Set")
            negativeButton("Cancel")
        }
    ) {
        timepicker (
            waitForPositiveButton = true
                ) { time ->
            viewModel.updateTime(time)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedTextField(
    placeholder: String,
    viewModel: BalanceViewModel,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    transformation : VisualTransformation = VisualTransformation.None,
) {
    var value by remember { mutableStateOf("") }
    TextField(
        modifier = modifier
            .padding(top = 10.dp)
            .clip(BudgeRoundedCorner),
        value = value,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        placeholder = {Text(text = placeholder)},
        visualTransformation = transformation,
        onValueChange =  {
            value = it
            viewModel.textFieldValues[placeholder] = it
        }
    )
}