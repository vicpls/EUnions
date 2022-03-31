package netdesigntool.com.eunions;

import static netdesigntool.com.eunions.Util.LTAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class DataRepository {

    final private static String dbFileName = "eunion.db";
    @SuppressWarnings("NeverUsed")
    final static String TAB_EUNIONS = "eunions";
    final static String COL_ALP2 = "member";
    final static String COL_SHE = "schengen";
    final static String COL_EU = "eu";
    final static String COL_NAME = "name";

    // SQL requests:
    // Only members of EU or Schengen.
    private static final String PARTICIPIAL_COUNTRIES = "SELECT * FROM participial_countries";
    // Countries exclude EU or Schengen.
    private static final String OTHER_COUNTRIES = "SELECT * FROM other_countries LIMIT %d OFFSET %d";

    private static DataRepository me;

    private SQLiteDatabase dbase;

    private DataRepository() {}

    public static DataRepository getDataRepository(){
        if (me ==null) me = new DataRepository();
        return me;
    }

    private Cursor loadData(Context context, String sqlRequest){

        dbase = new Db(dbFileName).openBase(context);
        if (dbase != null) return dbase.rawQuery(sqlRequest, null);
        else
            return null;
    }


    Country[] loadCountries(Context context){
        Cursor cursor = loadData(context, PARTICIPIAL_COUNTRIES);

        if (cursor ==null || cursor.getCount() <1) return new Country[0];   //no data - return empty array

        int qty = cursor.getCount();
        cursor.moveToFirst();
        Country[] result = new Country[qty];

        int indISO, indEuni, indSchen, indName;

        try {
            indISO = cursor.getColumnIndexOrThrow(COL_ALP2);
            indEuni = cursor.getColumnIndexOrThrow(COL_EU);
            indSchen = cursor.getColumnIndexOrThrow(COL_SHE);
            indName = cursor.getColumnIndexOrThrow(COL_NAME);
        }catch (IllegalArgumentException e){
            Log.e(LTAG, "Can't find column in the database. Exception in: "+ this.getClass().getSimpleName());
            return new Country[0];
        }

        for(int i=0; i<qty; i++){
            Country country = new Country(
                    cursor.getString(indISO)
                    , cursor.getInt(indEuni)
                    , cursor.getInt(indSchen)
                    , cursor.getString(indName)
            );
            result[i] = country;
            cursor.moveToNext();
        }
        cursor.close();
        if (dbase !=null) dbase.close();

        return result;
    }

    /**
     * Load a page of countries not member EU or Schengen.
     * @param key index of country in a table - begin of a page
     * @param size amount of countries
     */
    public List<Country> loadCountriesWithKey(Context context, int key, int size) {


        @SuppressLint("DefaultLocale")
        String sql = String.format(OTHER_COUNTRIES, size, key);

        Cursor cursor = loadData(context, sql);

        List<Country> result = new ArrayList<>();

        if (cursor ==null || cursor.getCount() <1) return result;   //no data - return empty list

        cursor.moveToFirst();

        int indISO, indName;

        try {
            indISO = cursor.getColumnIndexOrThrow(COL_ALP2);
            indName = cursor.getColumnIndexOrThrow(COL_NAME);
        }catch (IllegalArgumentException e){
            Log.e(LTAG, "Can't find column in the database. Exception in: "+ this.getClass().getSimpleName());
            return result;
        }


        do{
            Country country = new Country(
                    cursor.getString(indISO)
                    ,10                     // non EU
                    , 10                    // non Schengen
                    , cursor.getString(indName)
            );
            result.add(country);

        }while (cursor.moveToNext());

        cursor.close();
        if (dbase !=null) dbase.close();

        return result;
    }
}
