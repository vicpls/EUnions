package netdesigntool.com.eunions.ui.othcountries

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import netdesigntool.com.eunions.Country
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.ui.country.CountryAct
import netdesigntool.com.eunions.ui.country.CountryAct.COUNTRY_ISO
import netdesigntool.com.eunions.ui.country.CountryAct.COUNTRY_NAME

/**
 *      Activity show a list of the Countries not included in EU or Schengen.
 */

class ActOtherCountries : ComponentActivity() {

    private lateinit var lazyPagingItems : LazyPagingItems<Country>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pConfig = PagingConfig(18, 2, false)

        val pager : Pager<Int, Country> = Pager(pConfig, null, ::OthCountryPagingSource)

        setContent {
                    lazyPagingItems = pager.flow.collectAsLazyPagingItems()
                    OthCountryLayout(lazyPagingItems)
        }
    }

    private fun onCountryClick(iso: String, cName: String){
        startActivity(
            Intent(this
                , CountryAct::class.java)
                    .putExtra(COUNTRY_ISO, iso)
                    .putExtra(COUNTRY_NAME, cName)
        )
    }

    @Composable
    fun OthCountryLayout(items: LazyPagingItems<Country>) {

        Card( modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
            , shape = RoundedCornerShape(15.dp)
        ) {
            CountryList(items)
        }
    }

    @Composable
    fun CountryList(items: LazyPagingItems<Country>) {

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

                    val imgFlagId = resources.getIdentifier("flg_" + iso
                        , "drawable", packageName)

                    Image(painter = painterResource(id = imgFlagId)
                        , contentDescription = null
                        , modifier = flgModifier
                    )

                    Text("    ${country.name}"
                        , fontSize = 16.sp
                        , modifier =Modifier
                            .padding(start = 20.dp)
                            .align(CenterVertically)
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

        val cntModifier = Modifier
            .padding(start = 20.dp)


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
                                    .align(CenterVertically)
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
                                    .align(CenterVertically)
                            )
                        }
                    }
                }
            }

    }



}