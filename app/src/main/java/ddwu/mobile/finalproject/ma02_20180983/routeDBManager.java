package ddwu.mobile.finalproject.ma02_20180983;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class routeDBManager {
    RouteDBHelper routeDBHelper = null;

    public routeDBManager(Context context){
        routeDBHelper = new RouteDBHelper(context);
    }

    //DB에 새로운 일정 추가
    public boolean addNewRoute(route newRoute){
        SQLiteDatabase db = routeDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(RouteDBHelper.COL_ADDRESS, newRoute.getAddress());
        value.put(RouteDBHelper.COL_LATITUDE, newRoute.getLatitude());
        value.put(RouteDBHelper.COL_LONGITUDE, newRoute.getLongitude());
        long count = db.insert(RouteDBHelper.TABLE_NAME, null, value);
        routeDBHelper.close();
        if(count > 0)
            return true;
        return false;
    }

    //DB에 일정 삭제
    public boolean removeRoute(long id){
        SQLiteDatabase db = routeDBHelper.getWritableDatabase();
        String whereClause = RouteDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[]{ String.valueOf(id) };
        int result = db.delete(RouteDBHelper.TABLE_NAME, whereClause, whereArgs);
        routeDBHelper.close();
        if(result > 0)
            return true;
        return false;
    }

    //id로 route객체 검색
    public route getRouteById(long id){
        SQLiteDatabase db = routeDBHelper.getReadableDatabase();
        route searchRoute = null;

        String selection = RouteDBHelper.COL_ID + "=?";
        String[] selectArgs = new String[]{String.valueOf(id)};
        Cursor cursor=  db.query(RouteDBHelper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);
        while(cursor.moveToNext()){
            String address = cursor.getString(cursor.getColumnIndex(RouteDBHelper.COL_ADDRESS));
            Double latitude = cursor.getDouble(cursor.getColumnIndex(RouteDBHelper.COL_LATITUDE));
            Double longitude = cursor.getDouble(cursor.getColumnIndex(RouteDBHelper.COL_LONGITUDE));
            searchRoute = new route(address, latitude, longitude);
        }
        cursor.close();
        routeDBHelper.close();
        return searchRoute;
    }

}
