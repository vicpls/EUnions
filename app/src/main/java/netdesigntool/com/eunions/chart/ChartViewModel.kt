package netdesigntool.com.eunions.chart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import netdesigntool.com.eunions.firebase.FirebaseDataProvider
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    app :Application,
    private val fbProv: FirebaseDataProvider
    ) : AndroidViewModel(app) {

    var ldWHI: LiveData<Map<String, Float>> = fbProv.ldWHI
    var ldRankWHI: LiveData<Map<String, Number>> = fbProv.ldRankWHI

    fun requestWHI(isoCountryCode: String, title: String ="WHI"){
        fbProv.requestWHI(isoCountryCode, title)
    }

    fun requestRankWHI(isoCountryCode: String, title: String = "Rank of country in the WHI"){
        fbProv.requestRankWHI(isoCountryCode, title)
    }

}