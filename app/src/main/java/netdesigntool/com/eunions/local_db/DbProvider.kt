package netdesigntool.com.eunions.local_db

import android.content.Context
import androidx.room.Room

private const val DB_FILE_NAME= "eunion.db"

class DbProvider {

    fun getDB(context: Context) : AppDatabase{
        return Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DB_FILE_NAME)
                    .createFromAsset(DB_FILE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

    }
}