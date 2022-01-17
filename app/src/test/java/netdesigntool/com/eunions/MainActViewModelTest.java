package netdesigntool.com.eunions;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**     Тест не работает !!!
 *          Не мокаются методы внутри ViewModel.
 */


@RunWith(MockitoJUnitRunner.class)
public class MainActViewModelTest {

    /*@Mock
    MutableLiveData<Country[]> ldSchAndEu, ldSchen, ldEu;*/

    @Mock
    DataRepository dataRep;
    @Mock
    Application app;
    @Mock
    Context context;

    @Mock
    MainActViewModel _model;


    Country nothing = new Country("nothing", 0,0);
    Country both = new Country("both", 1,1);
    Country she = new Country("she", 0, 1);
    Country eu = new Country("eu", 1, 0);

    private Country[] country;

    @Before
    public void testInit(){

        when(_model.getApplication()).thenReturn(app);
        
        when(app.getApplicationContext()).thenReturn(context);

        when(dataRep.loadCountries(context)).thenReturn(
                new Country[]{ nothing, both, she, eu});

        /*doAnswer(new Answer() {
                     @Override
                     public Object answer(InvocationOnMock invocation) throws Throwable {
                         country = invocation.getArgument(0, Country[].class);
                         return null;
                     }
                 }
        ).when(ldEu).postValue(any(Country[].class));

        doAnswer(new Answer() {
                     @Override
                     public Object answer(InvocationOnMock invocation) throws Throwable {
                         country = invocation.getArgument(0, Country[].class);
                         return null;
                     }
                 }
        ).when(ldSchAndEu).postValue(any(Country[].class));

        doAnswer(new Answer() {
                     @Override
                     public Object answer(InvocationOnMock invocation) throws Throwable {
                         country = invocation.getArgument(0, Country[].class);
                         return null;
                     }
                 }
        ).when(ldSchen).postValue(any(Country[].class));*/


        // when(ldEu.getValue()).thenReturn(country);

        _model = new MainActViewModel(app, dataRep);
    }


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    @FunctionalInterface
    interface myLambda {
        void myRun();
    }

    // helper method to allow us to get the value from a LiveData
    // LiveData won't publish a result until there is at least one observer
    private void observeForTesting (MutableLiveData<Country[]> testLD, myLambda block) {

        Observer <Country[]> observer = (Observer) o -> { /* At this place maybe test assumptions... */
            System.out.println(o.toString());
        };


            testLD.observeForever(observer);
            block.myRun();
        /*finally {
            testLD.removeObserver(observer);
        }*/
    }

    @After
    public void clear(){
        country = null;
    }


    @Test
    public void getEu_Test() {

        MutableLiveData<Country[]> ld = _model.getEu();
        observeForTesting(ld, ()->{});

        Country[] arrCountry;

        do {
            arrCountry = ld.getValue();
        }while (arrCountry == null);

        Country result = arrCountry[0];

        assertEquals(eu, result);
    }
}