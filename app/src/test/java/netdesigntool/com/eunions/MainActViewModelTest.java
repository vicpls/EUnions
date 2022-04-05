package netdesigntool.com.eunions;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static java.util.concurrent.TimeUnit.SECONDS;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import netdesigntool.com.eunions.local_db.AppDatabase;
import netdesigntool.com.eunions.model.Country;
import netdesigntool.com.eunions.ui.main.MainActVM;


@RunWith(MockitoJUnitRunner.class)
public class MainActViewModelTest {

    @Mock
    AppDatabase dataRep;
    @Mock
    Application app;
    @Mock
    Context context;

    @Mock
    MainActVM _model;


    Country nothing = new Country("nothing", 1,1, "nothing");
    Country both = new Country("both", 0,0, "both");
    Country shen = new Country("shen", 1, 0, "shen");
    Country eu = new Country("eu", 0, 1, "eu");

    private LiveData<List<Country>> ld;
    private List<Country> arrCountry;

    @Before
    public void testInit(){
        
        when(app.getApplicationContext()).thenReturn(context);

        /*when(dataRep.countriesDao().getMemberCountries()).thenReturn(
                new Country[]{ nothing, both, shen, eu});*/

        _model = new MainActVM(dataRep);
    }

    @After
    public void endTest(){
        ld=null;
    }

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();


    // helper method to allow us to get the value from a LiveData
    // LiveData won't publish a result until there is at least one observer
    private void observeForTesting() {

        ld.observeForever(o->{
            // At this place maybe test assumptions...
            //System.out.println(o.toString());
        });
    }


    // Subscribe and put value of LiveData 'ld' to arrCountry.
    private void ldSubscribeAndFetch() {

        observeForTesting();

        await().atMost(3, SECONDS).until(() -> ld.getValue() != null);

        arrCountry = ld.getValue();
    }

    // Assert LD value for only one, expected value.
    private void assertLdValue(String assertMessage, Country expected){

        assertEquals(assertMessage, arrCountry.size(), 1);

        Country result = arrCountry.get(0);
        assertEquals(expected.getIso(), result.getIso());
    }


    @Test
    public void getShen_Test() {

        ld = _model.getLdSchen();

        ldSubscribeAndFetch();

        assertLdValue("Only one Shengen-member must be in array.", shen);
    }

    @Test
    public void getEu_Test() {

        ld = _model.getLdEu();

        ldSubscribeAndFetch();

        assertLdValue("Only one EU-member must be in array.", eu);
    }

    @Test
    public void getBoth_Test() {

        ld = _model.getLdSchAndEu();

        ldSubscribeAndFetch();

        assertLdValue("Only one EU and Shengen-member must be in array.", both);
    }

}