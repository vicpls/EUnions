package netdesigntool.com.eunions.othcountries

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import netdesigntool.com.eunions.App
import netdesigntool.com.eunions.Country
import netdesigntool.com.eunions.DataRepository
import netdesigntool.com.eunions.Util.LTAG

class OthCountryPagingSource : PagingSource<Int, Country>() {

    private val dRepository : DataRepository = DataRepository.getDataRepository()

    override fun getRefreshKey(state: PagingState<Int, Country>): Int? {
        return null
        //TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Country> {

        val pageKey = params.key ?: 0

        val resList = dRepository.loadCountriesWithKey(getContext(), pageKey, params.loadSize)
        Log.d(LTAG, "Call dRepository.loadCountriesWithKey(_, $pageKey, ${params.loadSize})")

        val prevKey = if (pageKey>0) pageKey else null
        val nxtKey = if (resList.size < params.loadSize) null else pageKey.plus(params.loadSize)

        val result : LoadResult<Int, Country> = LoadResult.Page(resList
            , prevKey
            , nxtKey
        )
        Log.d(LTAG, "result: (size=${resList.size}, prevKey=$prevKey, nextKey=$nxtKey)")


        return result
    }

    private fun getContext() : Context? = App.getAppContext()


}