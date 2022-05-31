package netdesigntool.com.eunions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import static netdesigntool.com.eunions.Util.getOneKey;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class UtilTest {

    @Mock
    Context mContext;
    @Mock
    Resources mRes;

    @Before
    public void init_tests(){
        when(mContext.getResources()).thenReturn(mRes);
    }


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

    @Test
    public void test_formatValue(){

        assertEquals("1234", Util.formatValue(1234F));
        assertEquals("1234.01", Util.formatValue(1234.01F));
        assertEquals("123", Util.formatValue(123.0004F));
        assertEquals("1234", Util.formatValue(1234L));
        assertEquals("1234", Util.formatValue(1234));
    }

    @Test
    public void test_getOneKey(){

        Map<String, Integer> mMap = new HashMap<>(3);
        mMap.put("One", 1);
        mMap.put("Two",2);
        mMap.put("Three", 3);

        assertTrue(mMap.containsKey(getOneKey(mMap)));
    }


    /*@Test
    public void test_getTravelGuideUrl(){

        String countryISO ="ee";

        Locale locale = mock(Locale.class);

        MockedStatic<Locale> mocked = mockStatic(Locale.class);
        mocked.when(Locale::getDefault).thenReturn(locale);


        when(locale.getLanguage()).thenReturn("ru");

        when(mContext.getPackageName()).thenReturn("package");

        when(mRes.getIdentifier(
                countryISO
                , "string"
                , "package")).thenReturn(101);

        when(mRes.getString(101)).thenReturn(countryISO);

        SpannableString mockSpannable = mock(SpannableString.class);

        when(Html.fromHtml(anyString(),anyInt())).thenReturn(mockSpannable);

        String expected = " <a href=\""
                +"\"http://wikitravel.org/\""
                +"ru"
                +"/"
                +countryISO
                +"\">"
                + "Travel guide";

        assertEquals(expected, Util.getTravelGuideUrl(mContext,countryISO));
    }*/

    /*@Test
    public void createTravelGuideURL(){

        String pac = "netdesigntool.com.eunions";
        String cISO = "ru";
        String sCountry = "russia";
        final int id = 111;


        when(mContext.getPackageName()).thenReturn(pac);
        when(mContext.getResources()).thenReturn(res);
        when(mRes.getIdentifier(cISO,"string",pac)).thenReturn(id);
        when(mRes.getString(id)).thenReturn(sCountry);

        Html html = mock(Html.class);
        when(html.fromHtml(anyString())).thenReturn("");

        Util.getTravelGuideUrl(mContext, cISO);
    }*/
}