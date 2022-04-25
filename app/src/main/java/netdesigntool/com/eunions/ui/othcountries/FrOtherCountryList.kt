package netdesigntool.com.eunions.ui.othcountries

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.compose.collectAsLazyPagingItems
import dagger.hilt.android.AndroidEntryPoint
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.model.BaseCountry
import netdesigntool.com.eunions.repo.local_db.AppDatabase
import netdesigntool.com.eunions.ui.country.CountryAct
import javax.inject.Inject

/**
 *      Fragment show a list of the Countries not included in EU or Schengen.
 *
 */
@AndroidEntryPoint
class FrOtherCountryList : Fragment(){

    @Inject lateinit var appDB : AppDatabase
    private lateinit var pager : Pager<Int, BaseCountry>

    private var actTitle : CharSequence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pConfig = PagingConfig(20, 3, false)

        pager = Pager(pConfig, null) {
            appDB.countriesDao().getOtherCountries() as PagingSource<Int, BaseCountry>
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.let { act->
            actTitle = act.title
            act.setTitle(R.string.other_countries)
        }

        return ComposeView(requireContext()).apply {
                // Dispose the Composition when viewLifecycleOwner is destroyed
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
                )
                setContent {
                    val lazyPagingItems = pager.flow.collectAsLazyPagingItems()
                    ShowContent(lazyPagingItems, ::onCountryClick, context)
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

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.title = actTitle
    }

}