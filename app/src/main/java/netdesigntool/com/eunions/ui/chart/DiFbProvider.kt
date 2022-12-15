package netdesigntool.com.eunions.ui.chart

import android.content.Context
import com.hh.data.repo.firebase.FirebaseAttribute
import com.hh.data.repo.firebase.FirebaseDataProvider
import com.hh.data.repo.firebase.IFirebaseDataProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiFbProvider {

    @Singleton
    @Provides
    fun getFbProvider(
        @ApplicationContext context: Context,
        attr: FirebaseAttribute) : IFirebaseDataProvider {

       return FirebaseDataProvider(context, attr)
    }
}