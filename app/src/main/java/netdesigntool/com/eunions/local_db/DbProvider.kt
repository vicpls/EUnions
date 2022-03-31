package netdesigntool.com.eunions.local_db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DB_FILE_NAME= "eunion.db"

@Module
@InstallIn(SingletonComponent::class)
object DbProvider {

    @Provides
    @Singleton
    fun getDB(@ApplicationContext context: Context) : AppDatabase{
        return Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DB_FILE_NAME)
                    .createFromAsset(DB_FILE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

    }
}