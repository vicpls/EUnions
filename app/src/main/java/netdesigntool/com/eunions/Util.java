package netdesigntool.com.eunions;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import java.text.DecimalFormatSymbols;
import java.util.Map;

public final class Util {

    final public static String LTAG = "EUnions";

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

    /**
     * Trim the small remainder and transform number to string
     * @param num Number (e.g. 123.0004)
     * @return String with round number (e.g. "123")
     */
    public static String formatValue(Number num) {
        String result;

        if (num instanceof Float) {
            float f = num.floatValue();

            if (f - (int) f < 0.001) {
                result = Integer.toString((int)f);
            } else {
                result = Float.toString(f);
            }

        } else {
            result = num.toString();
        }

        return result;
    }

    /** Return only one key from keys of Map
     *
     * @param mMap Map
     * @return One of the keys of Map
     */
    public static <T> String getOneKey(Map<String, T> mMap){
        int result=0;

        for (String k : mMap.keySet()) {

            int key;
            try{
                key = Integer.parseUnsignedInt(k);
            } catch (NumberFormatException nfex){ key = 0;}

            result = Math.max(key, result);
        }

        return Integer.toString(result);
    }

    /** Return color for given id regardless of the API version
     *
     * @param colorId id
     * @param context context
     * @param theme theme or null
     * @return color
     */
    public static int getColorAnyWay(int colorId, Context context, Resources.Theme theme)
    {
        return (Build.VERSION.SDK_INT < 23) ?
                context.getResources().getColor(colorId) :
                context.getResources().getColor(colorId, theme);
    }

}
