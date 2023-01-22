package hr.fika.budgeapp.atms.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import hr.fika.budgeapp.atms.viewmodel.AtmsViewModel
import hr.fika.budgeapp.design_system.ui.button.BudgeButton

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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        if (locations.value!!.isNotEmpty()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                locations.value!!.forEach { location ->
                    Marker(
                        state = MarkerState(LatLng(location.latitude!!, location.longitude!!)),
                        title = location.street
                    )
                }
            }
        } else {
            BudgeButton("Get locations") {
                viewModel.getAtmLocations()
            }
        }
    }
}