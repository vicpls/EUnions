package com.hh.data.repo.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hh.data.repo.local_db.entities.Eunions
import com.hh.data.repo.local_db.entities.OtherCountry
import com.hh.data.repo.local_db.entities.ParticipialCountries


@Database(entities = [Eunions::class],
    views = [ParticipialCountries::class, OtherCountry::class],
    version = 5,
    )

abstract class AppDatabase : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao
}