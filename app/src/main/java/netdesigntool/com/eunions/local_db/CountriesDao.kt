package netdesigntool.com.eunions.local_db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import netdesigntool.com.eunions.Country
import netdesigntool.com.eunions.local_db.entities.OtherCountry

@Dao
interface CountriesDao {

    @Query("SELECT * FROM participial_countries")
    suspend fun getMemberCountries() : List<Country>

    /*@Deprecated("This old solution", ReplaceWith("getOtherCountries() : PagingSource"))
    @Query("SELECT * FROM other_countries LIMIT :limit OFFSET :offset")
    fun getOtherCountries(offset: Int, limit: Int)*/

    @Query("SELECT * FROM other_countries")
    suspend fun getOtherCountries() : PagingSource<Int, Country>
}