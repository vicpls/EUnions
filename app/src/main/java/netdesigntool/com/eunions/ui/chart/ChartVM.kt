package netdesigntool.com.eunions.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import netdesigntool.com.eunions.repo.firebase.FirebaseDataProvider
import javax.inject.Inject

@HiltViewModel
class ChartVM @Inject constructor(
    private val fbProv: FirebaseDataProvider
    ) : ViewModel() {

    val ldWHI: LiveData<Map<String, Number>> = fbProv.ldWHI
    val ldRankWHI: LiveData<Map<String, Number>> = fbProv.ldRankWHI

    fun requestWHI(isoCountryCode: String, title: String ="WHI"){
        fbProv.requestWHI(isoCountryCode, title)
    }

    fun requestRankWHI(isoCountryCode: String, title: String = "Rank of country in the WHI"){
        fbProv.requestRankWHI(isoCountryCode, title)
    }

}