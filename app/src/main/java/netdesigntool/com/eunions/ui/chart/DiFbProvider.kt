package netdesigntool.com.eunions.ui.chart

import com.hh.data.repo.firebase.FirebaseDataProvider
import com.hh.data.repo.firebase.IFirebaseDataProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiFbProvider {

    @Singleton
    @Binds
    abstract fun getFbProv(fbdp: FirebaseDataProvider) : IFirebaseDataProvider
}