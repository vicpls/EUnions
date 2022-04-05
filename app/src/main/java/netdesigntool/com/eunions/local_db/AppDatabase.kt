package netdesigntool.com.eunions.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import netdesigntool.com.eunions.local_db.entities.ParticipialCountries
import netdesigntool.com.eunions.local_db.entities.Eunions
import netdesigntool.com.eunions.local_db.entities.OtherCountry


@Database(entities = [Eunions::class],
    views = [ParticipialCountries::class, OtherCountry::class],
    version = 4,
    )

abstract class AppDatabase : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao
}