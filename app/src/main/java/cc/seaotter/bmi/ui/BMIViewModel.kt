package cc.seaotter.bmi.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cc.seaotter.bmi.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BMIViewModel(private val application: Application) : AndroidViewModel(application) {

    private var _uiState = MutableStateFlow(BMIUIState())
    val uiState: StateFlow<BMIUIState> = _uiState.asStateFlow()

    fun calculateBMI(): Boolean {
        if (uiState.value.height.isEmpty() || _uiState.value.weight.isEmpty()) return false
        val height: Double
        val weight: Double
        try {
            height = _uiState.value.height.toDouble()
            weight = _uiState.value.weight.toDouble()
        } catch (e: NumberFormatException) {
            return false
        }
        if (height <= 0 || weight <= 0) return false
        val bmi = weight / (0.0001 * height * height)
        updateImageResource(bmi)
        _uiState.update { currentState ->
            currentState.copy(
                result = String.format(
                    "%.1f",
                    bmi
                ) + " (" + getBMIResult(bmi) + ")" + "\n" + getWaistlineCheckResult(),
                isResultDialogOpen = true
            )
        }
        return true
    }

    private fun getBMIResult(bmi: Double): String {
        return when {
            bmi < 18.5 -> application.resources.getString(R.string.tooLight)
            bmi < 24 -> application.resources.getString(R.string.normal)
            bmi < 27 -> application.resources.getString(R.string.tooHeavy)
            bmi < 30 -> application.resources.getString(R.string.mildObesity)
            bmi < 35 -> application.resources.getString(R.string.moderatelyObese)
            else -> application.resources.getString(R.string.severeObesity)
        }
    }

    private fun getWaistlineCheckResult(): String {
        return when {
            _uiState.value.gender == 0 && _uiState.value.waistline.toDouble() > 90 -> application.resources.getString(
                R.string.waistlineUnNormal
            )

            _uiState.value.gender == 1 && _uiState.value.waistline.toDouble() > 80 -> application.resources.getString(
                R.string.waistlineUnNormal
            )

            else -> application.resources.getString(R.string.waistlineNormal)
        }
    }

    private fun updateImageResource(bmi: Double) {
        _uiState.update { currentState ->
            currentState.copy(
                imageResource = when {
                    bmi >= 24 || getWaistlineCheckResult() == application.resources.getString(R.string.waistlineUnNormal) -> R.drawable.fat
                    _uiState.value.gender == 0 -> R.drawable.boy
                    _uiState.value.gender == 1 -> R.drawable.girl
                    else -> R.drawable.boy
                }
            )
        }
    }

    fun onHeightChanged(height: String) {
        _uiState.update { currentState ->
            currentState.copy(height = height)
        }
    }

    fun onWeightChanged(weight: String) {
        _uiState.update { currentState ->
            currentState.copy(weight = weight)
        }
    }

    fun onWaistlineChanged(waistline: String) {
        _uiState.update { currentState ->
            currentState.copy(waistline = waistline)
        }
    }

    fun onGenderChanged(selectedIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                gender = selectedIndex
            )
        }
    }

    fun onDismissRequest() {
        _uiState.update { currentState ->
            currentState.copy(isResultDialogOpen = false)
        }
    }
}