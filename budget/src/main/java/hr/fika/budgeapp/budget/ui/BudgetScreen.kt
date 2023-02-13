package hr.fika.budgeapp.budget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.fika.budgeapp.budget.viewmodel.BudgetViewModel
import hr.fika.budgeapp.design_system.theme.BudgeRoundedCorner
import hr.fika.budgeapp.design_system.theme.CURRENCY
import hr.fika.budgeapp.design_system.theme.budgeBlue
import hr.fika.budgeapp.design_system.ui.animation.LoadingAnimation3
import hr.fika.budgeapp.design_system.ui.button.BudgeButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel = viewModel()
) {
    val viewState = viewModel.viewState.observeAsState()
    val dateState = viewModel.dateState.observeAsState()
    val date = rememberDatePickerState()
    when (viewState.value) {
        is BudgetUiState.EDITOR -> {
            BudgetEditor(viewModel)
        }
        is BudgetUiState.ERROR -> TODO()
        is BudgetUiState.INITIAL -> {
            Column {
                BudgeButton(text = "Add budget") {
                    viewModel.navigateToEditor()
                }
                BudgeButton(text = "Get budgets") {
                    viewModel.getBudgets()
                }
            }
        }
        is BudgetUiState.LOADING -> LoadingAnimation3()
        null -> TODO()
        is BudgetUiState.BUDGETS -> {
            val budgets = (viewState.value as BudgetUiState.BUDGETS).budgets
            budgets.forEach { 
                Text(text = it.toString())
            }
        }
    }
    if (dateState.value is BudgetDatePickerState.SHOWN) {
        DatePickerDialog(onDismissRequest = {
            viewModel.toggleDatePicker()
        },
            confirmButton = {
                Button(onClick = {
                    viewModel.setDate(date.selectedDateMillis)
                }) {
                    Text("Set")
                }
            }
        ) {
            DatePicker(state = date)
        }
    }
}

@Composable
fun BudgetEditor(viewModel: BudgetViewModel) {
    val isOnTarget = viewModel.onTarget.observeAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LabeledRoundedText(
            label = "Current total",
            text = "${viewModel.calculationInfo.total}$CURRENCY"
        )
        LabeledRoundedText(
            label = "Monthly net change",
            text = "${viewModel.calculationInfo.netChange}$CURRENCY"
        )
        LabeledRoundedTextField(label = "Description") {
            RoundedTextField(placeholder = "Description", viewModel = viewModel)
        }
        LabeledRoundedTextField(label = "Amount") {
            RoundedTextField(
                placeholder = "Amount",
                viewModel = viewModel,
                keyboardType = KeyboardType.Decimal
            )
        }
        RoundedDateField(viewModel = viewModel)
        BudgeButton(text = "Add") {
            viewModel.addBudget()
        }
        OnTargetText(isOnTarget = isOnTarget.value!!)
    }
}

@Composable
fun OnTargetText(isOnTarget: Boolean, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = if (isOnTarget) "Budget is on target" else "Budget is being missed",
        color = if (isOnTarget) Color.Green else Color.Red
    )
}

@Composable
fun RoundedDateField(viewModel: BudgetViewModel) {
    val date = viewModel.date.observeAsState()
    LabeledRoundedText(label = "Budget date", text = date.value.toString(), onClick = {
        viewModel.toggleDatePicker()
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedTextField(
    placeholder: String,
    viewModel: BudgetViewModel,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    transformation: VisualTransformation = VisualTransformation.None,
) {
    var value by remember { mutableStateOf("") }
    TextField(
        modifier = modifier
            .clip(BudgeRoundedCorner),
        value = value,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        placeholder = { Text(text = placeholder) },
        visualTransformation = transformation,
        onValueChange = {
            value = it
            viewModel.textFieldValues[placeholder] = it
            if (placeholder == "Amount") {
                viewModel.refreshAmount()
            }
        }
    )
}

@Composable
fun LabeledRoundedText(label: String, text: String, onClick: () -> Unit = {}) {
    Column {
        Text(modifier = Modifier.padding(start = 18.dp), text = label)
        RoundedText(text = text, onClick = onClick)
    }
}

@Composable
fun LabeledRoundedTextField(label: String, roundedTextField: @Composable () -> Unit) {
    Column {
        Text(modifier = Modifier.padding(start = 18.dp), text = label)
        roundedTextField()
    }
}

@Composable
fun RoundedText(text: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .width(280.dp)
            .height(55.dp)
            .background(budgeBlue, BudgeRoundedCorner)
            .clip(BudgeRoundedCorner)
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 18.dp), text = text
        )
    }
}
