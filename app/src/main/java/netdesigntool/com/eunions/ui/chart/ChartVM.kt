package netdesigntool.com.eunions.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hh.data.repo.firebase.IFirebaseDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartVM @Inject constructor(
    private val fbProv: IFirebaseDataProvider
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
    //override fun requestWHI(isoCountryCode: String, title: String){
        fbProv.requestWHI(isoCountryCode, title){ whi->_ldWHI.postValue(whi) }
    }

    fun requestRankWHI(isoCountryCode: String, title: String = "Rank of country in the WHI"){
    //override fun requestRankWHI(isoCountryCode: String, title: String){
        fbProv.requestRankWHI(isoCountryCode, title){ rankWhi -> _ldRankWHI.postValue(rankWhi) }
    }
}