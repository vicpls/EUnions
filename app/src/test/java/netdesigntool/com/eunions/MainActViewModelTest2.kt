package netdesigntool.com.eunions

import android.app.Application
import android.content.Context
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mock
import netdesigntool.com.eunions.local_db.AppDatabase
import netdesigntool.com.eunions.ui.main.MainActVM
import netdesigntool.com.eunions.model.Country
import androidx.lifecycle.LiveData
import org.mockito.Mockito
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.awaitility.Awaitility
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainActViewModelTest2 {

    @Mock
    var dataRep: AppDatabase? = null

    @Mock
    var app: Application? = null

    @Mock
    var context: Context? = null

    @Mock
    var _model: MainActVM? = null

    val nothing = Country("nothing", 1, 1, "nothing")
    val both = Country("both", 0, 0, "both")
    val shen = Country("shen", 1, 0, "shen")
    val eu = Country("eu", 0, 1, "eu")

    private var ld: LiveData<List<Country>>? = null
    private var lstCountry: List<Country>? = null


    @Before
    fun testInit() {
        Mockito.`when`(app!!.applicationContext).thenReturn(context)

        runTest{
            `when`<List<Any>>(dataRep!!.countriesDao().getMemberCountries())
                .thenReturn(listOf(nothing, both, shen, eu))
        }

        _model = MainActVM(dataRep!!)
    }

    @After
    fun endTest() {
        ld = null
    }

    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // helper method to allow us to get the value from a LiveData
    // LiveData won't publish a result until there is at least one observer
    private fun observeForTesting() {
        ld!!.observeForever { o: List<Country>? -> println(o)}
    }

    // Subscribe and put value of LiveData 'ld' to arrCountry.
    private fun ldSubscribeAndFetch() {
        observeForTesting()
        Awaitility.await().atMost(3, TimeUnit.SECONDS).until { ld!!.value != null }
        lstCountry = ld!!.value
    }

    // Assert LD value for only one, expected value.
    private fun assertLdValue(assertMessage: String, expected: Country) {

        Assert.assertEquals(assertMessage, lstCountry?.size, 1)

        val iso = lstCountry?.get(0)
        Assert.assertEquals(expected.iso, iso)
    }

    @Test
    fun getShen_Test() {
        ld = _model?.ldSchen
        ldSubscribeAndFetch()
        assertLdValue("Only one Shengen-member must be in array.", shen)
    }

    @Test
    fun getEu_Test() {
        ld = _model?.ldEu
        ldSubscribeAndFetch()
        assertLdValue("Only one EU-member must be in array.", eu)
    }

    @Test
    fun getBoth_Test() {
        ld = _model?.ldSchAndEu
        ldSubscribeAndFetch()
        assertLdValue("Only one EU and Shengen-member must be in array.", both)
    }
}