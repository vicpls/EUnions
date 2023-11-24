package netdesigntool.com.eunions.ui.chart

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.ui.util.launchHiltFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
 График без кривой, т.к. в приложении ViewModel инициализируется из вызывающей фрагнмент активити,
 а здесь вызов фрагмента происходит из пустой активити:
 app/src/debug/java/netdesigntool/com/eunions/ui/util/TestFragmentActivity.kt
 Поэтому LiveData во ViewModel возвращает пустую Map<String,Number> и кривая не строится.
 Используется фэйковый провайдер из DiFakeFbProvider.
 */


@RunWith(AndroidJUnit4::class)
@UninstallModules(DiFbProvider::class)
@HiltAndroidTest
class ChartTestFakeProv {


    //--------------------------------------------------------
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()

        launchHiltFragment { ChartFragment() }
    }


    @Test
    fun testChartFragment() {
        println( "vm.whiSize == 1234567 ")
        Espresso.onView(withId(R.id.lChart))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}