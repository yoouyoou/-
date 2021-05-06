package ddwu.mobile.finalproject.ma02_20180983;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FoodDBHelper extends SQLiteOpenHelper {
    final static String TAG = "FoodDBHelper";

    final static String DB_NAME = "foods.db";
    public final static String TABLE_NAME = "food_table";

    public final static String COL_ID = "_id";
    public final static String COL_NAME = "name";
    public final static String COL_VOLUME = "volume";
    public final static String COL_KCAL = "kcal";
    public final static String COL_CARBO = "carbo";
    public final static String COL_PROTEIN = "protein";
    public final static String COL_FAT = "fat";
    public final static String COL_SUGAR = "sugar";
    public final static String COL_NATRIUM = "natrium";
    public final static String COL_CHOLESTEROL = "cholesterol";
    public final static String COL_FATTYACID = "fattyAcid";
    public final static String COL_TRANSFAT = "transfat";
    public final static String COL_MAKER = "maker";
    public final static String COL_PHOTO = "photo";

    public FoodDBHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " +
                COL_NAME + " TEXT, " + COL_VOLUME+ " DOUBLE, " + COL_KCAL + " DOUBLE, " + COL_CARBO + " DOUBLE, " + COL_PROTEIN + " DOUBLE, "+
                COL_FAT + " DOUBLE, " + COL_SUGAR + " DOUBLE, " + COL_NATRIUM + " DOUBLE, " + COL_CHOLESTEROL + " DOUBLE, " + COL_FATTYACID + " DOUBLE, " +
                COL_TRANSFAT + " DOUBLE, "+ COL_MAKER + " TEXT, " + COL_PHOTO + " TEXT)";
        Log.d(TAG, sql);
        db.execSQL(sql);

        //db.execSQL("insert into " + TABLE_NAME + " values (null, '가자미구이', 200, 314, 3.5, 43.2, 14.2, 0.4, 1331.18, 225.55, 2.3, 0.1, '기본')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
