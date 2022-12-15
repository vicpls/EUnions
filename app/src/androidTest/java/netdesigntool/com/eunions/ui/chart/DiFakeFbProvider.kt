package netdesigntool.com.eunions.ui.chart

import com.hh.data.repo.firebase.IFirebaseDataProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiFakeFbProvider {

    @Singleton
    @Provides
    fun getFbProvider() : IFirebaseDataProvider =
        FakeFirebaseDataProvider()
}