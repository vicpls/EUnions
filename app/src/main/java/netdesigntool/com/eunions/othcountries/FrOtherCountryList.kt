package netdesigntool.com.eunions.othcountries

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
import androidx.paging.compose.collectAsLazyPagingItems
import netdesigntool.com.eunions.Country

@Deprecated("Exist new solution.", ReplaceWith("OtherCountries"))
class FrOtherCountryList : Fragment(){

    private lateinit var pager : Pager<Int, Country>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pConfig = PagingConfig(20)

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
                    //List()
                    OthCountryLayout(pager, , )
                }
            }
    }

    @Preview
    @Composable
    fun List(){


                Card(backgroundColor = Color.DarkGray) {

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        item { Text("This is item of list") }
                        item { Text("Item2") }
                    }
                }
    }

    @Composable
    fun MessageList(pager: Pager<Int, Country>) {
        val lazyPagingItems = pager.flow.collectAsLazyPagingItems()

        Card(backgroundColor = Color.DarkGray) {
            LazyColumn {
                items(lazyPagingItems.itemCount) { country ->
                    if (country != null) {
                        MessageRow(country)
                    } else {
                        MessagePlaceholder()
                    }
                }
            }
        }
    }

    @Composable
    private fun MessagePlaceholder() {
        Text("still loading")
    }

    @Composable
    private fun MessageRow(country: Int) {
        Row {
            Text("flag")
            Text("countryName: $country")
        }
    }




}