package hr.fika.budgeapp.design_system.ui.button

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import hr.fika.budgeapp.design_system.theme.BudgeRoundedCorner

@Composable
fun BudgeButton(text: String, onClick : () -> Unit) {
    Button(
        shape = BudgeRoundedCorner,
        onClick =  onClick
    ) {
        Text(text = text)
    }
}