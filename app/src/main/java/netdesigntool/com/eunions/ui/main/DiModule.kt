package netdesigntool.com.eunions.ui.main

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class DiModule {

    @Provides
    @ActivityScoped
    fun getDescriptionFragmentManager(activity: Activity): DescriptionFragmentManager
    {
        return DescFragmentMang(activity)
    }
}