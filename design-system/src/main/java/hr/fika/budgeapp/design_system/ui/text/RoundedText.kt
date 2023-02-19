package hr.fika.budgeapp.design_system.ui.text

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import hr.fika.budgeapp.design_system.theme.BudgeRoundedCorner
import hr.fika.budgeapp.design_system.theme.budgeBlue

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