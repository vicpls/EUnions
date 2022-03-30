package netdesigntool.com.eunions.local_db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import netdesigntool.com.eunions.local_db.entities.OtherCountry

@Dao
interface CountriesDao {

    @Query("SELECT * FROM participial_countries")
    fun getMemberCountries()

    @Deprecated("This old solution", ReplaceWith("getOtherCountries() : PagingSource"))
    @Query("SELECT * FROM other_countries LIMIT :limit OFFSET :offset")
    fun getOtherCountries(offset: Int, limit: Int)

    @Query("SELECT * FROM other_countries")
    fun getOtherCountries() : PagingSource<Int, OtherCountry>
}