package netdesigntool.com.eunions.ui.othcountries

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.hh.data.model.BaseCountry
import com.hh.data.model.Country
import kotlinx.coroutines.flow.flowOf
import netdesigntool.com.eunions.R

const val ComposeTestTag_CountryList = "CountryList"

@Composable
fun ShowContent(items: LazyPagingItems<out BaseCountry>,
                onCountryClick: (String, String)->Unit
                ){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ){
        OthCountryLayout(items, onCountryClick)
    }
}

@Composable
fun OthCountryLayout(items: LazyPagingItems<out BaseCountry>,
                     onCountryClick: (String, String)->Unit
                     ) {

    Card( modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        , shape = RoundedCornerShape(15.dp)
    ) {
        CountryList(items, onCountryClick)
    }
}

private fun getFlagId(iso : String, context: Context) : Int =
    context.resources.getIdentifier(
    "flg_$iso"
    , "drawable", context.packageName)


@Composable
fun CountryList(items: LazyPagingItems<out BaseCountry>,
                onCountryClick: (String, String)->Unit
    ) {

    LazyColumn (verticalArrangement = Arrangement.spacedBy(6.dp)
        , modifier = Modifier
            .background(colorResource(R.color.others), RoundedCornerShape(15.dp))
            .padding(start = 60.dp, top =12.dp
                , end = 5.dp, bottom = 12.dp )
            .testTag(ComposeTestTag_CountryList)
    ){
        items(items.itemCount) {index ->
            val country = items[index]
            CountryItem(country, getFlagId(country!!.iso, LocalContext.current), onCountryClick)
        }
    }
}

@Composable
private fun CountryItem(
    country: BaseCountry?,
    @DrawableRes imgFlagId: Int,
    onCountryClick: (String, String) -> Unit
) {
    Row(Modifier.clickable { onCountryClick(country!!.iso, country.name) })
    {
        Image(
            painter = painterResource(id = imgFlagId),
            contentDescription = null,
            modifier = Modifier
                .height(32.dp)
                .width(32.dp)
        )

        Text(
            "    ${country!!.name}",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterVertically)
        )
    }
}


//@Preview(showBackground = true)
@Composable
fun DefaultPreview_old() {

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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    val countryList = listOf(
        Country("at",1, 1, "This_is_name_of_Country1"),
        Country("de", 1, 1, "This_is_name_of_Country2")
    )

    ShowContent(
        flowOf(PagingData.from(countryList)).collectAsLazyPagingItems()
    ) { _, _ -> }
}
