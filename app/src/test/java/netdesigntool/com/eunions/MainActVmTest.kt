package netdesigntool.com.eunions

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import netdesigntool.com.eunions.model.Country
import netdesigntool.com.eunions.model.toCountry
import netdesigntool.com.eunions.repo.local_db.AppDatabase
import netdesigntool.com.eunions.repo.local_db.CountriesDao
import netdesigntool.com.eunions.repo.local_db.entities.ParticipialCountries
import netdesigntool.com.eunions.ui.main.MainActVM
import org.awaitility.Awaitility
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainActVmTest {

    @Mock
    lateinit var app: Application

    @Mock
    lateinit var dataRep: AppDatabase

    @Mock
    lateinit var cntDAO: CountriesDao

    private var _model: MainActVM? = null

    private val nothing = ParticipialCountries(1, "nothing",1,"nothing")
    private val both = ParticipialCountries(0, "both", 0, "both")
    private val shen = ParticipialCountries(0, "shen", 1, "shen")
    private val eu = ParticipialCountries(1, "eu", 0, "eu")

    private var ld: LiveData<List<Country>>? = null
    private var lstCountry: List<Country>? = null


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun testInit() {

        _model = MainActVM(dataRep, app)

        //TODO("намокать обращение к ресурсам")

        `when`(dataRep.countriesDao()).thenReturn(cntDAO)

        runTest{
            `when`(dataRep.countriesDao().getMemberCountries())
                .thenReturn(listOf(nothing, both, shen, eu))
        }
    }

    @After
    fun endTest() {
        ld = null
    }

    @Rule @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

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

        val cnt = lstCountry?.get(0)
        Assert.assertEquals(expected.iso, cnt?.iso)
    }

    @Test
    fun getShen_Test(){
        ld = _model?.ldSchen
        ldSubscribeAndFetch()
        assertLdValue("Only one Shengen-member must be in array.", shen.toCountry())
    }

    @Test
    fun eu_Test(){
        ld = _model?.ldEu
        ldSubscribeAndFetch()
        assertLdValue("Only one EU-member must be in array.", eu.toCountry())
    }

    @Test
    fun getBoth_Test() {
        ld = _model?.ldSchAndEu
        ldSubscribeAndFetch()
        assertLdValue("Only one EU and Shengen-member must be in array.", both.toCountry())
    }
}