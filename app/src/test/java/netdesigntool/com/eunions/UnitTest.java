package netdesigntool.com.eunions;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.DecimalFormatSymbols;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class UnitTest {

    // Test of Util.getIntegerPart
    @Test
    public void test_getIntegerPart(){
        assertEquals("123", Util.getIntegerPart("123.456"));
        assertEquals("", Util.getIntegerPart(".123456"));
        assertEquals("123", Util.getIntegerPart("123"));
    }


    /*@Test
    public void createTravelGuideURL(){

        String pac = "netdesigntool.com.eunions";
        String cISO = "ru";
        String sCountry = "russia";
        final int id = 111;

        Context context = Mockito.mock(Context.class);
        Resources res = Mockito.mock(Resources.class);

        when(context.getPackageName()).thenReturn(pac);
        when(context.getResources()).thenReturn(res);
        when(res.getIdentifier(cISO,"string",pac)).thenReturn(id);
        when(res.getString(id)).thenReturn(sCountry);

        Html html = Mockito.mock(Html.class);
        when(html.fromHtml(Mockito.anyString())).thenReturn();

        Util.getTravelGuideUrl(context, cISO);

    }*/

    @Test
    public void test_roundNumber(){

        char delim = DecimalFormatSymbols.getInstance().getDecimalSeparator();

        assertEquals("123"+delim+"4567", Util.roundNumber("123456789", 4));
        assertEquals("123"+delim+"4", Util.roundNumber("123456", 1));
        assertEquals("123", Util.roundNumber("123456", 0));
        assertEquals("12", Util.roundNumber("12", 2));
        assertEquals("123", Util.roundNumber("123000", 2));
        assertEquals("123"+delim+"4", Util.roundNumber("123400", 2));
        assertEquals("12"+delim+"34", Util.roundNumber("12345",2));
    }


    @Mock
    Context mContext;
    @Mock
    Resources mRes;

    @Before
    public void init_tests(){
        mContext = mock(Context.class);
        mRes = mock(Resources.class);

        when(mContext.getResources()).thenReturn(mRes);
    }

    @Test
    public void test_getPrefixForNumber(){

        String ths = "ths";
        String m = "m";
        String b = "b";

        when(mRes.getString(R.string.thousand)).thenReturn(ths);
        when(mRes.getString(R.string.million)).thenReturn(m);
        when(mRes.getString(R.string.billion)).thenReturn(b);

        assertEquals("", Util.getPrefixForNumber("123", mContext));
        assertEquals(ths, Util.getPrefixForNumber("123000", mContext));
        assertEquals(m, Util.getPrefixForNumber("123000000", mContext));
        assertEquals(b, Util.getPrefixForNumber("123000000000", mContext));
    }


}