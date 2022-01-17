package netdesigntool.com.eunions;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;

/**     Тест не работает !!!
 *          Не мокаются методы внутри ViewModel.
 */


@RunWith(RobolectricTestRunner.class)
public class MainActViewModelRoboElTest {

    @Mock
    MutableLiveData<Country[]> ldSchAndEu, ldSchen, ldEu;

    @Mock
    DataRepository dataRep;
    @Mock
    Application app;
    @Mock
    Context context;

    MainActViewModel _model;

    Country nothing = new Country("nothing", 0,0);
    Country both = new Country("both", 1,1);
    Country she = new Country("she", 0, 1);
    Country eu = new Country("eu", 1, 0);

    private Country[] country;



    @Before
    public void init(){

        MockitoAnnotations.openMocks(this);

        _model = new MainActViewModel(app, dataRep);

        //when(_model.getApplication()).thenReturn(app);
        when(app.getApplicationContext()).thenReturn(context);

        when(dataRep.loadCountries(any())).thenReturn(
                new Country[]{ nothing, both, she, eu});

        doAnswer(new Answer() {
                     @Override
                     public Object answer(InvocationOnMock invocation) throws Throwable {
                         country = invocation.getArgument(0, Country[].class);
                         return null;
                     }
                 }
        ).when(ldEu).postValue(any(Country[].class));



        when(ldEu.getValue()).thenReturn(country);


    }

    @After
    public void clear(){
        country = null;
    }


    @Test
    public void getEu_Test() {

        Country result = _model
                .getEu()
                .getValue()[0];

        assertEquals(eu, result);
    }
}