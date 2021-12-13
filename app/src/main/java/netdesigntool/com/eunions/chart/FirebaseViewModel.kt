package netdesigntool.com.eunions.chart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import netdesigntool.com.eunions.firebase.FirebaseDataProvider

class FirebaseViewModel(app :Application) : AndroidViewModel(app) {

    private val fbProv: FirebaseDataProvider = FirebaseDataProvider(app)
    var ldWHI: LiveData<Map<String, Float>> = fbProv.ldWHI
    var ldRankWHI: LiveData<Map<String, Float>> = fbProv.ldRankWHI

    fun requestWHI(isoCountryCode: String, title: String ="WHI"){
        fbProv.requestWHI(isoCountryCode, title)
    }

    fun requestRankWHI(isoCountryCode: String, title: String = "Rank of country in the WHI"){
        fbProv.requestRankWHI(isoCountryCode, title)
    }

}