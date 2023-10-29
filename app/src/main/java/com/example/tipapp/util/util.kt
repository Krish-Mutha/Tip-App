package com.example.tipapp.util

fun calculateTotalTip(value: Double, tipPercentage: Int): Double {
    return if(value > 1)
        ((value * tipPercentage) / 100)
    else
        0.0
}

fun calculateTotalPerPerson(totalBill: Double, splitB: Int, tipPercentage: Int): Double{
    val bill = calculateTotalTip(totalBill,tipPercentage) + totalBill
    return  bill / splitB
}