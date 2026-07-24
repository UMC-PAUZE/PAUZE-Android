package com.example.pauze.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import com.example.pauze.ui.theme.bodyTextXlRegular
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import dev.darkokoa.datetimewheelpicker.core.format.CjkSuffixConfig
import dev.darkokoa.datetimewheelpicker.core.format.DateOrder
import dev.darkokoa.datetimewheelpicker.core.format.MonthDisplayStyle
import dev.darkokoa.datetimewheelpicker.core.format.dateFormatter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
fun PersonalInfoContent(
    viewModel: SignUpViewModel
): Boolean {
    var tempDay by remember { mutableStateOf<LocalDate?>(null)}
    var showBottomSheet by remember { mutableStateOf(false) }

    val customDateFormat = LocalDate.Format{
        year()
        char('-')
        monthNumber()
        char('-')
        day()
    }

    Column{
        ModeBasedTextField(
            mode = TextFieldMode.UserName,
            value = viewModel.name,
            onValueChanged = { viewModel.name = it },
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "2자 이상 입력해주세요",
            style = bodyTextSmRegular,
            color = if(viewModel.name.length == 1) AppTheme.palette.secondary.getColor(4)
                else AppTheme.palette.gray.getColor(5)
        )
        Spacer(modifier = Modifier.height(12.dp))
        SetBirthday(
            birthday = viewModel.birthday?.format(customDateFormat) ?: "생년월일을 입력해주세요",
            onClick = { showBottomSheet = true }
        )

        if(showBottomSheet){
            BirthdayBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                onDateChanged = { tempDay = it },
                onClick = { viewModel.birthday = tempDay; showBottomSheet = false }
            )
        }
    }
    return viewModel.name.length > 1 && viewModel.birthday != null
}

@Composable
fun SetBirthday(
    birthday: String,
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
                birthday,
                modifier = Modifier.weight(1f),
                style = bodyTextLgMedium,
                color = if (birthday == "생년월일을 입력해주세요") AppTheme.palette.gray.getColor(5)
                    else AppTheme.palette.gray.getColor(2)
            )
            Image(
                modifier = Modifier.clickable(onClick = onClick),
                painter = painterResource(R.drawable.ic_dropdown),
                contentDescription = "dropdown button"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayBottomSheet(
    onDismissRequest: () -> Unit,
    onDateChanged: (LocalDate) -> Unit,
    onClick: () -> Unit
){
    val borderShape = RoundedCornerShape(topStart = 56.dp, topEnd = 56.dp)
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = AppTheme.palette.gray.getColor(9),
        dragHandle = null,
        shape = borderShape,
        scrimColor = Color(0xCC2D2E28)
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
            BirthdayPicker(
                onDateChanged = onDateChanged
            )
            Button(
                "선택하기",
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            )
        }
    }
}
@Composable
fun BirthdayPicker(
    onDateChanged: (LocalDate) -> Unit
){
    val width = 360.dp
    val height = 294.dp
    val pickerHeight = 294.dp / 5
    val ymdHeight = 34.dp

    Row(
        modifier = Modifier.size(width = width, height = ymdHeight),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
            YMDText("년")
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
            YMDText("월")
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
            YMDText("일")
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Box(modifier = Modifier.size(width = width, height = height),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pickerHeight)
                .background(
                    color = AppTheme.palette.gray.getColor(7),
                    shape = RoundedCornerShape(1000.dp)
                )
        )
        WheelDatePicker(
            size = DpSize(width = width, height = height),
            rowCount = 5,
            textStyle = bodyTextXlRegular,
            textColor = AppTheme.palette.gray.getColor(2),
            selectorProperties = WheelPickerDefaults.selectorProperties(
                enabled = false
            ),
            dateFormatter = dateFormatter(
                dateOrder = DateOrder.YMD,
                monthDisplayStyle = MonthDisplayStyle.NUMERIC,
                cjkSuffixConfig = CjkSuffixConfig.HideAll,
            )
        ) { snappedDate ->
            onDateChanged(snappedDate)
        }
    }
}

@Composable
fun YMDText(ymd: String){
    Text(
        ymd,
        style = bodyTextMdRegular,
        color = AppTheme.palette.gray.getColor(4)
    )
}

