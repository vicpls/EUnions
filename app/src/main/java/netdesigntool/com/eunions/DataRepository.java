package netdesigntool.com.eunions;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


class DataRepository {

    final private static String dbFileName = "eunion.db";
    final static String TAB_EUNIONS = "eunions";
    final static String COL_NAME = "member";
    final static String COL_SHE = "schengen";
    final static String COL_EU = "eu";

    private static final String SQL_ALL_COUNTRY = "SELECT * FROM all_country";

    private static DataRepository me;

    private DataRepository() {}

    static DataRepository getDataRepository(){
        if (me==null) me = new DataRepository();
        return me;
    }

    Cursor loadData(Context context){

        SQLiteDatabase dbase = new Db(dbFileName).openBase(context);
        if (dbase != null) return dbase.rawQuery(SQL_ALL_COUNTRY, null);
        else
            return null;
    }
}
