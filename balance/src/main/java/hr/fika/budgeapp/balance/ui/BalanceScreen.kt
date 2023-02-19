package hr.fika.budgeapp.balance.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import hr.fika.budgeapp.balance.R
import hr.fika.budgeapp.balance.viewmodel.BalanceViewModel
import hr.fika.budgeapp.common.bank.model.Transaction
import hr.fika.budgeapp.common.extensions.roundDouble
import hr.fika.budgeapp.common.utils.DateTimeUtils
import hr.fika.budgeapp.design_system.theme.BudgeRoundedCorner
import hr.fika.budgeapp.design_system.theme.budgeBlue
import hr.fika.budgeapp.design_system.theme.budgeDarkBlue
import hr.fika.budgeapp.design_system.ui.animation.LoadingAnimation3
import hr.fika.budgeapp.design_system.ui.button.BudgeButton
import hr.fika.budgeapp.design_system.ui.error.ErrorScreen
import hr.fika.budgeapp.design_system.ui.text.LabeledRoundedText
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


data class TabItem(val title: String, val type: TransactionType)
enum class TransactionType { TRANSACTIONS, FLOW }

private val tabItems = listOf(
    TabItem("Transactions", TransactionType.TRANSACTIONS),
    TabItem("Flow", TransactionType.FLOW)
)

@Composable
fun BalanceScreen(
    viewModel: BalanceViewModel = viewModel()
) {
    val viewState = viewModel.viewState.observeAsState()
    var selectedTab by remember { mutableStateOf(0) }
    TabRow(modifier = Modifier.padding(top = 24.dp), selectedTabIndex = selectedTab) {
        tabItems.forEachIndexed { index, tabItem ->
            Tab(selected = selectedTab == index, onClick = {
                selectedTab = index
                viewModel.loadData(tabItem.type)
            }) {
                Text(text = tabItem.title)
            }
        }
    }
    when (viewState.value) {
        BalanceUiState.EDITOR -> EditorScreen(viewModel)
        BalanceUiState.ERROR -> ErrorScreen()
        BalanceUiState.DIALOG -> LinkDialog(viewModel)
        is BalanceUiState.INCOME -> FlowScreen(
            viewModel,
            (viewState.value as BalanceUiState.INCOME).transactions
        )
        BalanceUiState.LOADING -> LoadingAnimation3()
        is BalanceUiState.TRANSACTIONS -> TransactionsList(
            (viewState.value as BalanceUiState.TRANSACTIONS).transactions
        )
        null -> {}
    }
}

@Composable
fun LinkDialog(viewModel: BalanceViewModel) {
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
            TextButton(onClick = {  })
            { Text(text = "No") }
        }
    )
}

@Composable
fun EditorScreen(viewModel: BalanceViewModel) {
    val isRepeating = viewModel.isRepeating.observeAsState()
    val date = viewModel.date.observeAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        RoundedTextField(placeholder = "Description", viewModel)
        RoundedTextField(
            placeholder = "Amount",
            viewModel,
            keyboardType = KeyboardType.Decimal
        )
        RoundedDateField(date = date, viewModel = viewModel, isRepeating = isRepeating)
        LabeledCheckBox(
            label = "Repeating monthly",
            isRepeating = isRepeating,
            viewModel = viewModel
        )
        BudgeButton(text = "Save") {
            viewModel.saveTransaction()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FlowScreen(viewModel: BalanceViewModel, flow: List<Transaction>) {
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(top = 48.dp),
        floatingActionButton = {
            FloatingActionButton(
                shape = BudgeRoundedCorner,
                containerColor = budgeDarkBlue,
                onClick = {
                    viewModel.prepEditor()
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
            items(flow) {
                TransactionItem(it)
            }
        }
    }
}

@Composable
fun TransactionsList(transactions: List<Transaction>) {
    TotalItem(transactions.sumOf { it.amount!! })
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.91f)
            .padding(top = 144.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(transactions) {
            TransactionItem(it)
        }

    }
}

@Composable
fun TotalItem(total: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(top = 56.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Total", fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .background(budgeBlue, BudgeRoundedCorner)
                .width(160.dp)
                .height(45.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "${total.roundDouble()} €",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val vector = if (transaction.amount!! > 0) R.drawable.deposit else R.drawable.withdraw
    val color = if (transaction.amount!! > 0) Color.Green else Color.Red
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .clip(BudgeRoundedCorner), color = budgeBlue
    ) {
        ConstraintLayout {
            val (desc, amount, date, icon) = createRefs()
            Text(
                modifier = Modifier.constrainAs(desc) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(parent.top, 8.dp)
                },
                text = transaction.description!!
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .constrainAs(date) {
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(desc.bottom, 4.dp)
                    },
                text = transaction.getFormattedDate()
            )
            Icon(
                modifier = Modifier
                    .size(38.dp)
                    .constrainAs(icon) {
                        top.linkTo(parent.top, 8.dp)
                        end.linkTo(parent.end, 16.dp)
                        bottom.linkTo(parent.bottom, 8.dp)
                    },
                painter = painterResource(id = vector),
                tint = color,
                contentDescription = null
            )
            Text(
                modifier = Modifier.constrainAs(amount) {
                    end.linkTo(icon.start, 8.dp)
                    top.linkTo(icon.top)
                    bottom.linkTo(icon.bottom)
                },
                text = "${transaction.getAbsAmount().roundDouble()} €"
            )
        }
    }
}

@Composable
fun LabeledCheckBox(label: String, isRepeating: State<Boolean?>, viewModel: BalanceViewModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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
    LabeledRoundedText(label = "Transaction date", text = text, onClick = {
        dateState.show()
    })
    MaterialDialog(
        dialogState = dateState,
        buttons = {
            positiveButton("Set")
            negativeButton("Cancel")
        }
    ) {
        datepicker(
            waitForPositiveButton = true
        ) { date ->
            viewModel.updateDate(date)
            if (isRepeating.value == true) {
                viewModel.updateTime(LocalTime.of(0, 0))
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
        timepicker(
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
    transformation: VisualTransformation = VisualTransformation.None,
) {
    var value by remember { mutableStateOf("") }
    TextField(
        modifier = modifier
            .padding(top = 10.dp)
            .clip(BudgeRoundedCorner),
        value = value,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        placeholder = { Text(text = placeholder) },
        visualTransformation = transformation,
        onValueChange = {
            value = it
            viewModel.textFieldValues[placeholder] = it
        }
    )
}