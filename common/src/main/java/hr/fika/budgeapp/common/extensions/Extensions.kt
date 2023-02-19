package hr.fika.budgeapp.common.extensions

import java.math.RoundingMode

 fun Double.roundDouble() = this
    .toBigDecimal()
    .setScale(2, RoundingMode.DOWN)
    .toDouble()