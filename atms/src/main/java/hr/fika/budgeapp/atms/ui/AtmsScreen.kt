package hr.fika.budgeapp.atms.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import hr.fika.budgeapp.atms.viewmodel.AtmsViewModel
import hr.fika.budgeapp.design_system.ui.text.RoundedText

@Preview
@Composable
fun AtmsScreen(
    viewModel: AtmsViewModel = viewModel()
) {

    val zagreb = LatLng(45.80866074335285, 15.977959188461936)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(zagreb, 10f)
    }
    val locations = viewModel.locations.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.91f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        LabeledDropDown(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp),
            label = "Bank",
            text = "Bank",
            viewModel = viewModel
        )
        if (locations.value!!.isNotEmpty()) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f),
                cameraPositionState = cameraPositionState
            ) {
                locations.value!!.forEach { location ->
                    Marker(
                        state = MarkerState(LatLng(location.latitude!!, location.longitude!!)),
                        title = location.street
                    )
                }
            }
        }
    }
}

@Composable
fun LabeledDropDown(
    label: String,
    text: String,
    viewModel: AtmsViewModel,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val menuText = viewModel.bankName.value
    Column(modifier = modifier) {
        Text(modifier = Modifier.padding(start = 18.dp), text = label)
        RoundedText(text = menuText!!, onClick = { isExpanded = true })
        DropdownMenu(
            modifier = Modifier.padding(start = 18.dp),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            viewModel.banks.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        viewModel.getAtmLocations(it)
                        isExpanded = false
                    }
                )
            }
        }
    }
}