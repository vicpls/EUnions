package netdesigntool.com.eunions.ui.chart

import com.hh.data.repo.firebase.IFirebaseDataProvider

class MockFirebaseDataProvider: IFirebaseDataProvider {

    private val whiData = hashMapOf<String, Number>(
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

    override fun requestWHI(
        isoCountryCode: String,
        title: String,
        onResult: (Map<String, Number>) -> Unit
    ) {
        onResult(whiData)
    }

    override fun requestRankWHI(
        isoCountryCode: String,
        title: String,
        onResult: (Map<String, Number>) -> Unit
    ) {
        onResult(mapOf("2022" to 77))
    }

    val a="fake"
}