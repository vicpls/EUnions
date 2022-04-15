package netdesigntool.com.eunions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class Util {

    final public static String LTAG = "EUnions";

    /** Get only integer part of number in String.
     *
     * @param value String that can parsing to floating
     * @return Integer part of value
     */
    public static String getIntegerPart(String value){

        int delimiterPos = value.indexOf('.');
        if (delimiterPos <0) return value;
        if (delimiterPos ==0) return "";

        return value.substring(0, delimiterPos);
    }

    /**
     * Determines whether the Internet connection.
     * @param context Context
     * @return true if connected
     */
    public static boolean isConnected(Context context){

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            NetworkCapabilities netCap = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return netCap != null && netCap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo !=null && netInfo.isConnected();

    }


    /** Create url to a travel guide for the country.
      *   Assumed that name of country in particular language included in url. It may be not true.
      */
    public static Spanned getTravelGuideUrl(Context context, String countryISO, String cName){

        int idForCountryName = context.getResources().getIdentifier(countryISO, "string", context.getPackageName());

        String countryName = (idForCountryName >0) ?
                context.getResources().getString(idForCountryName)
                : cName;

        String url = "http://wikitravel.org/"+ Locale.getDefault().getLanguage() +"/"+ countryName;

        String html = " <a href=\""+ url +"\">" + context.getResources().getString(R.string.trv_guide);

        if (Build.VERSION.SDK_INT >= 24)
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        else
            //noinspection deprecation
            return Html.fromHtml(html);
    }



    /** Trim number (as String) and place current system delimiter
     *
     * @param number number by string, exp."736539"
     * @param accuracy sign quantity after delimiter point, exp. 2
     * @return string with rounded to nearest billion\thousand number, exp. "736.53"
     */
    public static String roundNumber(String number, int accuracy){

        int len = number.length();

        if (len <4) return number;

        int th3 = (len %3 ==0) ? len/3-1 : len/3;
        int decPos =  len - th3*3;


        String result = number.substring(0, decPos);

        // trim last zeros
        boolean doit=true;
        while (doit && accuracy >0){
            if (number.charAt(decPos-1 + accuracy) =='0') accuracy--;
            else doit=false;
        }

        if (accuracy >0)
            result += DecimalFormatSymbols.getInstance().getDecimalSeparator() + number.substring(decPos, decPos + accuracy);

        return result;
    }


    /**
     * Abbreviate for given number in shoted human readable style.
     * np. 1234 = ths, 1234567 = m
     *
     * @param number number in string
     * @param context context
     * @return Abbreviate
     */
    public static String getPrefixForNumber(String number, Context context){

        int len = number.length();

        if (len <=3) return "";

        String result;
        if (len <=6)
            result= context.getResources().getString(R.string.thousand);
        else if (len <=9)
            result= context.getResources().getString(R.string.million);
        else
            result= context.getResources().getString(R.string.billion);

        return result;
    }

}
