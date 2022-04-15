package netdesigntool.com.eunions.repo.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import netdesigntool.com.eunions.repo.local_db.entities.Eunions
import netdesigntool.com.eunions.repo.local_db.entities.OtherCountry
import netdesigntool.com.eunions.repo.local_db.entities.ParticipialCountries


@Database(entities = [Eunions::class],
    views = [ParticipialCountries::class, OtherCountry::class],
    version = 4,
    )

abstract class AppDatabase : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao
}