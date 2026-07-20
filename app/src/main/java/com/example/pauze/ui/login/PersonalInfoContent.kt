package com.example.pauze.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.pauze.R
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.login.component.ModeBasedTextField
import com.example.pauze.ui.login.component.TextFieldMode
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.bodyTextLgMedium
import com.example.pauze.ui.theme.bodyTextMdRegular
import com.example.pauze.ui.theme.bodyTextSmRegular
import com.example.pauze.ui.theme.bodyTextXlRegular
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import java.sql.DriverManager.println

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoContent(){
    var name by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column{
        ModeBasedTextField(
            mode = TextFieldMode.UserName,
            value = name,
            onValueChanged = { name = it },
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text("2자 이상 입력해주세요", style = bodyTextSmRegular, color = AppTheme.palette.gray.getColor(5))
        Spacer(modifier = Modifier.height(12.dp))
        BirthdayPicker(onClick = { showBottomSheet = true })

        if(showBottomSheet){
            val borderShape = RoundedCornerShape(topStart = 56.dp, topEnd = 56.dp)
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                containerColor = AppTheme.palette.gray.getColor(9),
                dragHandle = null,
                shape = borderShape,
                scrimColor = BottomSheetDefaults.ScrimColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = AppTheme.palette.gray.getColor(9),
                            shape = borderShape
                        ).padding(horizontal = 24.dp)
                ){
                    Spacer(modifier = Modifier.height(12.dp))
                    Spacer(modifier = Modifier
                        .width(56.dp)
                        .height(6.dp)
                        .background(color = AppTheme.palette.gray.getColor(4))
                        .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    WheelDatePicker(
                        size = DpSize(width = 312.dp, height = 52.dp),
                        rowCount = 5,
                        textStyle = bodyTextXlRegular,
                        textColor = AppTheme.palette.gray.getColor(7),
                        selectorProperties = WheelPickerDefaults.selectorProperties(
                            enabled = true,
                            shape = RoundedCornerShape(1000.dp),
                            color = AppTheme.palette.gray.getColor(7)
                        )
                    ){ snappedDate ->
                        println("선택된 날짜, $snappedDate")
                    }
                    Button(
                        "선택하기",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@Composable
fun BirthdayPicker(
    onClick: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth().border(
                width = 1.dp,
                color = AppTheme.palette.gray.getColor(6),
                shape = RoundedCornerShape(size = 16.dp)
            ).padding(
                horizontal = 16.dp, vertical = 14.dp
            )
    ){
        Text(
            "생년월일",
            style = bodyTextMdRegular,
            color = AppTheme.palette.gray.getColor(5)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row{
            Text(
                "생년월일을 입력해주세요",
                modifier = Modifier.weight(1f),
                style = bodyTextLgMedium,
                color = AppTheme.palette.gray.getColor(5)
            )
            Image(
                modifier = Modifier.clickable(onClick = onClick),
                painter = painterResource(R.drawable.ic_dropdown),
                contentDescription = "dropdown button"
            )
        }
    }
}