package ddwu.mobile.finalproject.ma02_20180983;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RouteDBHelper extends SQLiteOpenHelper {

    final static String DB_NAME = "routes.db";
    public final static String TABLE_NAME = "route_table";

    public final static String COL_ID = "_id";
    public final static String COL_ADDRESS = "address";
    public final static String COL_LATITUDE = "latitude";
    public final static String COL_LONGITUDE = "longitude";

    public RouteDBHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, "
                + COL_ADDRESS + " TEXT, " + COL_LATITUDE + " DOUBLE, " + COL_LONGITUDE + " DOUBLE)";
        Log.d("RouteDBHelper", sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
