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

    val ldLastYear: LiveData<Float> by this::_ldLY
    private val _ldLY: MutableLiveData<Float> = MutableLiveData(2022f)

    /**
     * Rank of country in the list of World Happiness Index.
     * Map of <year, value> where year as String
     */
    val ldRankWHI: LiveData<Map<String, Number>> by this::_ldRankWHI
    private val _ldRankWHI : MutableLiveData<Map<String, Number>> = MutableLiveData(HashMap())

    fun requestWHI(isoCountryCode: String, title: String ="WHI"){
        fbProv.requestWHI(isoCountryCode, title){ whi->
                getLY(whi)?.let{ _ldLY.postValue(it) }
                _ldWHI.postValue(whi) }
    }

    fun requestRankWHI(isoCountryCode: String, title: String = "Rank of country in the WHI"){
        fbProv.requestRankWHI(isoCountryCode, title){ rankWhi -> _ldRankWHI.postValue(rankWhi) }
    }

    private fun getLY(whi: Map<String, Number>): Float? =
        whi.maxOf { it.key }.toFloatOrNull()

}