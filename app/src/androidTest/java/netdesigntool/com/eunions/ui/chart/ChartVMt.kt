package netdesigntool.com.eunions.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//@HiltViewModel
class ChartVM /*@Inject*/ constructor() : ViewModel() {
/*@HiltViewModel
class ChartVMt @Inject constructor() : ChartViewModel() {*/

    val whiData = hashMapOf<String, Number>(
        Pair("2010", 5),
        Pair("2011", 5.5),
        Pair("2012", 6),
        Pair("2013", 6.3),
        Pair("2014", 6.4),
        Pair("2015", 6.5),
        Pair("2016", 6.2),
        Pair("2017", 5.9),
        Pair("2018", 5.5),
        Pair("2019", 5),
        Pair("2020", 4.5),
        Pair("2021", 4.3),
        Pair("2022", 4.1)
    )

    val ldWHI: LiveData<Map<String, Number>> = MutableLiveData(whiData)

    val whiSize = ldWHI.value

    val ldRankWHI: LiveData<Map<String, Number>>
        get() = MutableLiveData(hashMapOf("2022" to 77))

    fun requestWHI(isoCountryCode: String, title: String) {
        (ldWHI as MutableLiveData).value = whiData
    }

    fun requestRankWHI(isoCountryCode: String, title: String) {
        //"Not  implemented"
    }


}