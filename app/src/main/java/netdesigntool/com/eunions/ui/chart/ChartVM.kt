package netdesigntool.com.eunions.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import netdesigntool.com.eunions.repo.firebase.FirebaseDataProvider
import javax.inject.Inject

@HiltViewModel
class ChartVM @Inject constructor(
    private val fbProv: FirebaseDataProvider
    ) : ViewModel() {

    /**
     * Values of World Happiness Index
     * Map of <year, value> where year as String
     */
    val ldWHI: LiveData<Map<String, Number>> by this::_ldWHI
    private val _ldWHI : MutableLiveData<Map<String, Number>> = MutableLiveData(HashMap())

    /**
     * Rank of country in the list of World Happiness Index.
     * Map of <year, value> where year as String
     */
    val ldRankWHI: LiveData<Map<String, Number>> by this::_ldRankWHI
    private val _ldRankWHI : MutableLiveData<Map<String, Number>> = MutableLiveData(HashMap())

    fun requestWHI(isoCountryCode: String, title: String ="WHI"){
        fbProv.requestWHI(isoCountryCode, title, _ldWHI)
    }

    fun requestRankWHI(isoCountryCode: String, title: String = "Rank of country in the WHI"){
        fbProv.requestRankWHI(isoCountryCode, title, _ldRankWHI)
    }

}