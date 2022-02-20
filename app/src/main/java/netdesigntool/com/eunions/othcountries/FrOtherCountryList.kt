package netdesigntool.com.eunions.othcountries

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import netdesigntool.com.eunions.Country
import netdesigntool.com.eunions.country.CountryAct

/**
 *      Fragment show a list of the Countries not included in EU or Schengen.
 *      The same as [ActOtherCountries]
 */
class FrOtherCountryList : Fragment(){

    private lateinit var pager : Pager<Int, Country>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pConfig = PagingConfig(18, 2, false)

        pager = Pager(pConfig, null, ::OthCountryPagingSource)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
                // Dispose the Composition when viewLifecycleOwner is destroyed
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
                )
                setContent {
                    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()
                    OthCountryLayout(lazyPagingItems, ::onCountryClick, context)
                }
            }
    }

    private fun onCountryClick(iso: String, cName: String){
        startActivity(
            Intent(this.context, CountryAct::class.java)
                .putExtra(CountryAct.COUNTRY_ISO, iso)
                .putExtra(CountryAct.COUNTRY_NAME, cName)
        )
    }

}