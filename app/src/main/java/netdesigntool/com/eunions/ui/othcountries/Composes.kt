package netdesigntool.com.eunions.ui.othcountries

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import netdesigntool.com.eunions.Country
import netdesigntool.com.eunions.R

@Composable
fun OthCountryLayout(items: LazyPagingItems<Country>, onCountryClick: (String, String)->Unit, context: Context) {

    Card( modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        , shape = RoundedCornerShape(15.dp)
    ) {
        CountryList(items, onCountryClick, context)
    }
}

@Composable
fun CountryList(items: LazyPagingItems<Country>, onCountryClick: (String, String)->Unit, context: Context) {

    val flgModifier = Modifier
        .height(32.dp)
        .width(32.dp)

    LazyColumn (verticalArrangement = Arrangement.spacedBy(6.dp)
        , modifier = Modifier.background(colorResource(R.color.others), RoundedCornerShape(15.dp))
            .padding(start = 60.dp, top =12.dp
                , end = 5.dp, bottom = 12.dp )
    ){
        items(items) { country ->

            val iso = country!!.iso

            Row (Modifier.clickable { onCountryClick(iso, country.name) })
            {

                val imgFlagId = context.resources.getIdentifier("flg_" + iso
                    , "drawable", context.packageName)

                Image(painter = painterResource(id = imgFlagId)
                    , contentDescription = null
                    , modifier = flgModifier
                )

                Text("    ${country.name}"
                    , fontSize = 16.sp
                    , modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    val flgModifier = Modifier
        .height(32.dp)
        .width(32.dp)

    Card( modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()
        , shape = RoundedCornerShape(15.dp)
        //, elevation = 12.dp
        , backgroundColor = colorResource(R.color.others)
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                //.background(Others, RoundedCornerShape(15.dp))
                .padding(25.dp)
                .fillMaxWidth()
        ) {
            item {
                Row {

                    Image(
                        painter = painterResource(R.drawable.flg_at),
                        contentDescription = null,
                        modifier = flgModifier
                    )
                    Text(
                        "This_is_name_of_Country", fontSize = 16.sp, modifier = Modifier
                            .padding(start = 20.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
            item {
                Row {

                    Image(
                        painter = painterResource(R.drawable.flg_de),
                        contentDescription = null,
                        modifier = flgModifier
                    )
                    Text(
                        "This_is_name_of_Country2", fontSize = 16.sp, modifier = Modifier
                            .padding(start = 20.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}