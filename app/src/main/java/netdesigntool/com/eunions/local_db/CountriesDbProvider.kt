package netdesigntool.com.eunions.local_db

import android.content.Context
import androidx.room.Room

private const val DB_FILE_NAME= "eunion.db"

class CountriesDbProvider {

    fun getDB(context: Context) : CountriesDb{
        return Room.databaseBuilder(
                        context,
                        CountriesDb::class.java,
                        DB_FILE_NAME)
                    .createFromAsset(DB_FILE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

    }
}