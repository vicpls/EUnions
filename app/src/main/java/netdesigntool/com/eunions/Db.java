package netdesigntool.com.eunions;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


class Db {

    private File DbFile;
    private final String dbFileName;

    @SuppressWarnings("FieldCanBeLocal")
    private final int DB_VERSION = 3;   // The Actual version of DB for this App.

    /**
     * Create new.
     * @param baseFileName db file name
     */
    Db(String baseFileName){
        dbFileName = baseFileName;
    }


    /* Copy database file from assets to local database storage.
     *
     * @param context context
     * @throws IOException cannot copy file
     */
    private void copyDataBase(Context context) throws IOException {

        InputStream assetDbStream = context.getAssets().open(dbFileName);

        OutputStream localDbStream;

        try {
            localDbStream = new FileOutputStream(DbFile);
        }catch(IOException e){

            File f = DbFile.getParentFile();

            if ( ! f.exists()) {
                if (! f.mkdir()) throw new IOException("Can not create folder for database");
                //noinspection ResultOfMethodCallIgnored
                DbFile.createNewFile();
            }

            localDbStream = new FileOutputStream(DbFile);
        }

        // Just file copy
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = assetDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }

        localDbStream.close();
        assetDbStream.close();
    }



    SQLiteDatabase openBase(Context context) {

        if (DbFile == null) DbFile = context.getDatabasePath(dbFileName);

        if ( ! DbFile.exists() || !baseVersionIsActual ()) {
            try {
                copyDataBase(context);
            } catch (IOException e) {
                Log.e("EUnions", "Can't copy file from assets to data base.");
                return null;
            }
        }

        return SQLiteDatabase.openDatabase(DbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    private boolean baseVersionIsActual() {

        SQLiteDatabase sqlDB = SQLiteDatabase.openDatabase(DbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        int curVer = sqlDB.getVersion();
        sqlDB.close();

        return  curVer >= DB_VERSION;
    }
}
