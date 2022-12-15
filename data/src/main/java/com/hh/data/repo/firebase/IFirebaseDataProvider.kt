package com.hh.data.repo.firebase

interface IFirebaseDataProvider {
    /**
     * Launch request to the countries' base for WHI values.
     * @param isoCountryCode Two alphabet ISO code of country
     * @param title Title for result of requested
     * @param onResult Call on result. It can be called not on UI thread.
     */
    fun requestWHI(
        isoCountryCode: String,
        title: String,
        onResult: (Map<String, Number>) -> Unit
    )

    /**
     *  Launch request to the country base for the rank in WHI listing.
     *  @param isoCountryCode Two alphabet ISO code of country
     *  @param title Title for result of requested
     *  @param onResult Call on result. It can be called not on UI thread.
     */
    fun requestRankWHI(
        isoCountryCode: String,
        title: String,
        onResult: (Map<String, Number>) -> Unit
    )
}