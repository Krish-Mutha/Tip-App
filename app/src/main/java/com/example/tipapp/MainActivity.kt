package com.example.tipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipapp.components.InputField
import com.example.tipapp.ui.theme.TipAppTheme
import com.example.tipapp.util.calculateTotalPerPerson
import com.example.tipapp.util.calculateTotalTip
import com.example.tipapp.widgets.RoundIconButtons

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp{
                    MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    TipAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 134.0){
    Surface(
        Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFFF2D7F7)
    ) {
        Column(
            Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainContent(){
    BillForm(){}

}


@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun BillForm(
    modifier: Modifier =   Modifier,
             onValChange: (String) -> Unit ={}
             ) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val splitByState = remember {
        mutableStateOf(1)
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    Column(
        modifier = Modifier.padding(6.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        TopHeader(totalPerPersonState.value)
        Surface(
            Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {
            Column(
                Modifier
                    .padding(6.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                InputField(
                    Modifier
                        .padding(2.dp)
                        .fillMaxWidth(),
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())

                        keyboardController?.hide()
                        totalPerPersonState.value =
                            calculateTotalPerPerson(totalBillState.value.toDouble(), splitByState.value, tipPercentage)
                    }
                )
                if (validState) {
                    Row(
                        Modifier
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Split", Modifier.align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(120.dp))

                        Row(
                            Modifier
                                .padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            RoundIconButtons(
                                imageVector = Icons.Default.Remove,
                                onClick = {
                                    splitByState.value =
                                        if (splitByState.value > 1) splitByState.value - 1 else 1
                                    totalPerPersonState.value =
                                        calculateTotalPerPerson(
                                            totalBillState.value.toDouble(),
                                            splitByState.value,
                                            tipPercentage
                                        )
                                })

                            Text(
                                text = "${splitByState.value}",
                                Modifier
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(start = 9.dp, end = 9.dp)
                            )

                            RoundIconButtons(
                                imageVector = Icons.Default.Add,
                                onClick = {
                                    splitByState.value += 1
                                    totalPerPersonState.value =
                                        calculateTotalPerPerson(
                                            totalBillState.value.toDouble(),
                                            splitByState.value,
                                            tipPercentage
                                        )
                                })
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 3.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Tip",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(200.dp))
                        Text(
                            text = "${tipAmountState.value}",
                            Modifier.align(alignment = Alignment.CenterVertically)
                        )

                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$tipPercentage %")

                        Spacer(modifier = Modifier.width(200.dp))

                        Slider(
                            value = sliderPositionState.value,
                            onValueChange = { newVal ->
                                val tipPercentage = (newVal * 100).toInt()

                                val tipAmount = calculateTotalTip(totalBillState.value.toDouble(), tipPercentage)
                                val totalPerPerson = calculateTotalPerPerson(
                                    totalBillState.value.toDouble(),
                                    splitByState.value,
                                    tipPercentage
                                )
                                tipAmountState.value = tipAmount
                                totalPerPersonState.value = totalPerPerson

                                sliderPositionState.value = newVal
                            },
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            steps = 5
                        )                    }
                } else {
                    Box() {}
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipAppTheme {
        MyApp{
            Text(text = "Hello Again" )
        }
    }
}