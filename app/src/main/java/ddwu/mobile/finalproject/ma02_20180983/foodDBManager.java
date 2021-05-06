package ddwu.mobile.finalproject.ma02_20180983;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class foodDBManager {
    FoodDBHelper foodDBHelper = null;
    Cursor cursor = null;

    public foodDBManager(Context context){
        foodDBHelper = new FoodDBHelper(context);
    }

//    //DB의 모든 음식 반환
//    public ArrayList<food> getAllFoods(){
//        ArrayList foodList = new ArrayList();
//        SQLiteDatabase db = foodDBHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + FoodDBHelper.TABLE_NAME, null);
//
//        while(cursor.moveToNext()){
//            int id = cursor.getInt(cursor.getColumnIndex(FoodDBHelper.COL_ID));
//
//        }
//    }

    //DB에 있는 전체 칼로리값 반환
    public double getTotalKcal(){
//        ArrayList<Double> kcalList = new ArrayList();
        double totalKcal = 0;
        SQLiteDatabase db = foodDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FoodDBHelper.TABLE_NAME, null);

        while(cursor.moveToNext()){
            double kcal = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_KCAL));
            totalKcal += kcal;
        }
        return totalKcal;
    }

    //DB에 새로운 음식 추가
    public boolean addNewFood(food newFood){
        SQLiteDatabase db = foodDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FoodDBHelper.COL_NAME, newFood.getName());
        value.put(FoodDBHelper.COL_VOLUME, newFood.getVolume());
        value.put(FoodDBHelper.COL_KCAL, newFood.getKcal());
        value.put(FoodDBHelper.COL_CARBO, newFood.getCarbohydrate());
        value.put(FoodDBHelper.COL_PROTEIN, newFood.getProtein());
        value.put(FoodDBHelper.COL_FAT, newFood.getFat());
        value.put(FoodDBHelper.COL_SUGAR, newFood.getSugar());
        value.put(FoodDBHelper.COL_NATRIUM, newFood.getNatrium());
        value.put(FoodDBHelper.COL_CHOLESTEROL, newFood.getCholesterol());
        value.put(FoodDBHelper.COL_FATTYACID, newFood.getFattyAcid());
        value.put(FoodDBHelper.COL_TRANSFAT, newFood.getTransfat());
        value.put(FoodDBHelper.COL_MAKER, newFood.getMaker());
        value.put(FoodDBHelper.COL_PHOTO, "0");

        long count = db.insert(FoodDBHelper.TABLE_NAME, null, value);
        foodDBHelper.close();
        if(count > 0)
            return true;
        return false;
    }

    //DB에 음식 삭제
    public boolean removeFood(int id){
        SQLiteDatabase db = foodDBHelper.getWritableDatabase();
        String whereClause = FoodDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        int result = db.delete(FoodDBHelper.TABLE_NAME, whereClause, whereArgs);
        foodDBHelper.close();
        if(result > 0)
            return true;
        return false;
    }

    //id로 food객체의 사진경로 저장
    public boolean setPhotoById(long id, String path){
        Log.d("foodDBManager", "food객체의 사진경로 저장 내부: " + path + ", id값: " + id);
        SQLiteDatabase db = foodDBHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(FoodDBHelper.COL_PHOTO, path);
        String whereClause = FoodDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        int count = db.update(FoodDBHelper.TABLE_NAME, row, whereClause, whereArgs);
        Log.d("foodDBManager", "저장결과: " + count);
        foodDBHelper.close();
        if(count > 0) {
            Log.d("foodDBManager", "food객체 사진경로 저장 완료");
            return true;
        } else {
            Log.d("foodDBManager", "food객체 사진경로 저장 실패");
            return false;
        }
    }

    //id로 음식DB 검색
    public food getFoodById(long id){
        SQLiteDatabase db = foodDBHelper.getReadableDatabase();
        food searchFood = null;

        String selection = FoodDBHelper.COL_ID + "=?";
        String[] selectArgs = new String[] {String.valueOf(id)};
        Cursor cursor = db.query(FoodDBHelper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);
        while(cursor.moveToNext()){
            //추가
            long _id = cursor.getLong(cursor.getColumnIndex(FoodDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_NAME));
            Double volume = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_VOLUME));
            Double kcal = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_KCAL));
            Double carbo = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_CARBO));
            Double protein = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_PROTEIN));
            Double fat = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_FAT));
            Double sugar = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_SUGAR));
            Double natrium = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_NATRIUM));
            Double chole = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_CHOLESTEROL));
            Double fattyAcid = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_FATTYACID));
            Double transFat = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COL_TRANSFAT));
            String maker = cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_MAKER));
            String photo = cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_PHOTO));
                                //추가
            searchFood = new food(_id, name,volume,kcal,carbo,protein,fat,sugar,natrium,chole,fattyAcid,transFat,maker,photo);
        }
        cursor.close();
        foodDBHelper.close();
        return searchFood;
    }

}
