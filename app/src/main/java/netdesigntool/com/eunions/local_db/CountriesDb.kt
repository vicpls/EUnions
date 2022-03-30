package netdesigntool.com.eunions.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import netdesigntool.com.eunions.local_db.entities.EuOnly
import netdesigntool.com.eunions.local_db.entities.OtherCountry


@Database(entities = [EuOnly::class, OtherCountry::class], version = 3)

abstract class CountriesDb : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao
}