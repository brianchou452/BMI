package cc.seaotter.bmi.ui

import cc.seaotter.bmi.R

data class BMIUIState (
    val height: String = "",
    val weight: String = "",
    val gender: Int = 0,
    val waistline: String = "",
    val result: String = "",
    val imageResource : Int = R.drawable.boy,
    val isResultDialogOpen: Boolean = false
)