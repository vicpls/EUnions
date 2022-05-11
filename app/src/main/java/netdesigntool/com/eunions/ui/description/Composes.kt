package netdesigntool.com.eunions.ui.description

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import netdesigntool.com.eunions.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowContent(@StringRes descId: Int, bgColor: Color, txtColor: Color, onClick: ()->Unit) {

    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(15.dp),
        backgroundColor = bgColor,      // colorResource(id = R.color.euroUnionNoA),
        onClick = onClick,
    ) {
        Text(
            LocalContext.current.resources.getString(descId),
            fontSize = 18.sp,
            color = txtColor,       //Color.White,
            modifier = Modifier
                .padding(all = 20.dp)
        )
    }
}


@Preview
@Composable
fun MyPreView(){
    ShowContent(R.string.eu_desc, Color.Blue, Color.White) {}
}