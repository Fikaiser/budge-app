package hr.fika.budgeapp.budget.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.fika.budgeapp.budget.R
import hr.fika.budgeapp.budget.model.BudgetProjection
import hr.fika.budgeapp.budget.viewmodel.BudgetViewModel
import hr.fika.budgeapp.design_system.theme.BudgeRoundedCorner
import hr.fika.budgeapp.design_system.theme.CURRENCY
import hr.fika.budgeapp.design_system.theme.budgeBlue
import hr.fika.budgeapp.design_system.theme.budgeDarkBlue
import hr.fika.budgeapp.design_system.ui.animation.LoadingAnimation3
import hr.fika.budgeapp.design_system.ui.button.BudgeButton

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
        is BudgetUiState.LOADING -> LoadingAnimation3()
        null -> TODO()
        is BudgetUiState.BUDGETS -> {
            val budgets = (viewState.value as BudgetUiState.BUDGETS).budgets
            Scaffold(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f),
                floatingActionButton = {
                    FloatingActionButton(
                        shape = BudgeRoundedCorner,
                        containerColor = budgeDarkBlue,
                        onClick = {
                            viewModel.navigateToEditor()
                        }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Filled.Add),
                            contentDescription = null
                        )
                    }
                }
            ) { _ ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.91f)
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(budgets) {
                        BudgetItem(budget = it, viewModel)
                    }
                }
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
fun BudgetItem(budget: BudgetProjection, viewModel: BudgetViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .clickable {

            }
            .clip(BudgeRoundedCorner), color = budgeBlue
    ) {
        ConstraintLayout(modifier = Modifier) {
            val statusText = if (budget.isOnTarget) "Budget on target" else "Budget missing target"
            val vector = if (budget.isOnTarget) R.drawable.growth else R.drawable.decrease
            val color = if (budget.isOnTarget) Color.Green else Color.Red
            val (description, amount, date, delete, icon, status, change) = createRefs()
            Text(
                modifier = Modifier.constrainAs(description) {
                    top.linkTo(parent.top, 8.dp)
                    start.linkTo(parent.start, 8.dp)
                },
                text = budget.budget.description!!
            )
            Text(
                modifier = Modifier.constrainAs(amount) {
                    top.linkTo(description.bottom, 6.dp)
                    start.linkTo(parent.start, 8.dp)
                },
                text = "${budget.budget.amount.toString()}€"
            )
            Text(
                modifier = Modifier.constrainAs(date) {
                    top.linkTo(amount.bottom, 6.dp)
                    start.linkTo(parent.start, 8.dp)
                },
                text = budget.getDate()
            )
            Icon(
                modifier = Modifier
                    .clickable {viewModel.deleteBudget(budget.budget.idBudget!!)}
                    .constrainAs(delete) {
                        top.linkTo(date.top)
                        end.linkTo(parent.end, 8.dp)
                    },
                painter = rememberVectorPainter(Icons.Filled.Delete),
                contentDescription = null
            )
            Icon(
                modifier = Modifier
                    .size(38.dp)
                    .constrainAs(icon) {
                        top.linkTo(parent.top, 8.dp)
                        end.linkTo(parent.end, 8.dp)
                    },
                painter = painterResource(id = vector),
                tint = color,
                contentDescription = null
            )
            Text(
                modifier = Modifier.constrainAs(status) {
                    top.linkTo(date.bottom, 6.dp)
                    start.linkTo(parent.start, 8.dp)
                },
                text = statusText,
                color = color
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .constrainAs(change) {
                        top.linkTo(status.bottom, 6.dp)
                        start.linkTo(parent.start, 8.dp)
                    },
                text = "Projected total: ${budget.change}€"
            )
        }
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
