package com.example.pauze.ui.login

import android.os.Bundle
import android.util.Pair
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.mutableObjectListOf
import androidx.collection.objectListOf
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pauze.ui.component.Button
import com.example.pauze.ui.component.TopBar
import com.example.pauze.ui.theme.AppTheme
import com.example.pauze.ui.theme.MainPaletteTheme
import com.example.pauze.ui.theme.bodyTextLgMedium
import com.example.pauze.ui.theme.bodyTextMdRegular

class PrivacyPolicyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPaletteTheme {
                PrivacyPolicyScreen()
            }
        }
    }
}

@Composable
fun PrivacyPolicyScreen(){
    // todo: 뷰모델로 이동
    val policies = objectListOf<Pair<String, String>>(
        Pair(
            "제1조 (목적)",
            "이 약관은 PAUZE(이하 \"회사\")가 제공하는 서비스의 이용과 관련하여 회사와 이용자 간의 권리, 의무 및 책임 사항을 규정함을 목적으로 합니다. "
        ),
        Pair(
            "제2조 (개인정보 수집 항목)",
            "회사는 서비스 제공을 위해 다음의 개인정보를 수집합니다.\n" +
                    "- 필수: 이름, 생년월일, 이메일 주소, 비밀번호\n" +
                    "- 선택: 예민함 측정 데이터, 앱 사용 기록"
        ),
        Pair(
            "제3조 (개인정보 이용 목적)",
            "수집한 개인정보는 서비스 제공, 예민함 패턴 분석, 맞춤형 콘텐츠 추천에 활용됩니다. 제3자에게 제공되지 않으며, 이용자의 동의 없이 목적 외 이용하지 않습니다."
        ),
        Pair(
            "제4조 (개인정보 보유 기간)",
            "개인정보는 회원 탈퇴 시까지 보유하며, 탈퇴 후 6개월 이내 파기합니다. 단, 관계 법령에 따라 일정 기간 보존이 필요한 경우 해당 기간 동안 보관합니다."
        ),
        Pair(
            "제5조 (이용자의 권리)",
            "이용자는 언제든지 자신의 개인정보를 조회, 수정, 삭제할 수 있으며, 개인정보 처리에 대한 동의를 철회할 수 있습니다."
        ),
        Pair(
            "제6조 (개인정보 보호 책임자)",
            "개인정보 관련 문의는 privacy@pauze.kr로 연락 바랍니다."
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.palette.gray.getColor(9))
            .padding(horizontal = 24.dp)
    ){
        TopBar("개인정보 처리방침")
        Spacer(modifier = Modifier.height(16.dp))
        policies.forEach { policy ->
            PolicyText(
                policy.first,
                policy.second
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            "동의하고 돌아가기",
            modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
            enabled = true
        )
    }
}

@Composable
fun PolicyText(
    title: String,
    content: String,
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        Text(
            title,
            style = bodyTextLgMedium,
            color = AppTheme.palette.gray.getColor(2)
        )
        Text(
            content,
            style = bodyTextMdRegular,
            color = AppTheme.palette.gray.getColor(4)
        )
    }
}