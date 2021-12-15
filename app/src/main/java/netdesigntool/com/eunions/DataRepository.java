package netdesigntool.com.eunions;

import static netdesigntool.com.eunions.Util.LTAG;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


class DataRepository {

    final private static String dbFileName = "eunion.db";
    final static String TAB_EUNIONS = "eunions";
    final static String COL_NAME = "member";
    final static String COL_SHE = "schengen";
    final static String COL_EU = "eu";

    private static final String SQL_ALL_COUNTRY = "SELECT * FROM all_country";

    private static DataRepository me;

    SQLiteDatabase dbase;

    private DataRepository() {}

    static DataRepository getDataRepository(){
        if (me ==null) me = new DataRepository();
        return me;
    }

    Cursor loadData(Context context){

        dbase = new Db(dbFileName).openBase(context);
        if (dbase != null) return dbase.rawQuery(SQL_ALL_COUNTRY, null);
        else
            return null;
    }


    Country[] loadCountries(Context context){
        Cursor cursor = loadData(context);

        if (cursor ==null || cursor.getCount() <1) return new Country[0];   //no data - return empty array

        int qty = cursor.getCount();
        cursor.moveToFirst();
        Country[] result = new Country[qty];

        int indISO, indEuni, indSchen;

        try {
            indISO = cursor.getColumnIndexOrThrow(COL_NAME);
            indEuni = cursor.getColumnIndexOrThrow(COL_EU);
            indSchen = cursor.getColumnIndexOrThrow(COL_SHE);
        }catch (IllegalArgumentException e){
            Log.e(LTAG, "Can't find column in the database. Exception in: "+ this.getClass().getSimpleName());
            return new Country[0];
        }

        for(int i=0; i<qty; i++){
            Country country = new Country(
                    cursor.getString(indISO)
                    , cursor.getInt(indEuni)
                    , cursor.getInt(indSchen)
            );
            result[i] = country;
            cursor.moveToNext();
        }
        cursor.close();
        if (dbase !=null) dbase.close();

        return result;
    }
}
