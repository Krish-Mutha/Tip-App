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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipapp.components.InputField
import com.example.tipapp.ui.theme.TipAppTheme
import com.example.tipapp.widgets.RoundIconButtons
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipAppTheme {
                MyApp{
                    MainContent()
                }

            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        content()
    }
}

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 134.0){
    Surface(
        Modifier
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
                style = MaterialTheme.typography.headlineSmall
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
    BillForm(){ billAmt ->
        Log.d("AMT", "MainContent: $billAmt ")
    }
}


@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun BillForm(
    modifier: Modifier =   Modifier,
             onValChange: (String) -> Unit ={}
             ){
    val totalBillState = remember{
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value){
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp,color = Color.LightGray)
    ) {
        Column (
            Modifier
                .padding(6.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ){
            InputField(
                Modifier
                    .padding(2.dp),
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if(!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())

                    keyboardController?.hide()
                }
            )
            if(validState){
                Row (
                    Modifier
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(text = "Split"
                    ,Modifier.align(Alignment.CenterVertically)
                        )
                    
                    Spacer(modifier = Modifier.width(120.dp))

                    Row (
                        Modifier
                            .padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ){
                        RoundIconButtons(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            onClick = { /*TODO*/ })
                        RoundIconButtons(
                            imageVector = Icons.Default.Add,
                            onClick = { /*TODO*/ })
                    }
                }
            }
            else{
                Box(){}
            }
        }
    }


}


//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipAppTheme {
        MyApp{
            Text(text = "Hello Again" )
        }
    }
}