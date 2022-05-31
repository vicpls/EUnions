package com.hh.data.repo.local_db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.hh.data.repo.local_db.entities.OtherCountry
import com.hh.data.repo.local_db.entities.ParticipialCountries

@Dao
interface CountriesDao {

    @Query("SELECT * FROM participial_countries")
    suspend fun getMemberCountries() : List<ParticipialCountries>

    @Query("SELECT * FROM other_countries")
    fun getOtherCountries() : PagingSource<Int, OtherCountry>
    //suspend fun getOtherCountries() : PagingSource<Int, OtherCountry>
}