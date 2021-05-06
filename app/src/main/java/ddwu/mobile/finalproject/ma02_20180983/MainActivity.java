package ddwu.mobile.finalproject.ma02_20180983;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final static int REQ_ROUTE = 100;

    EditText et_targetKg;
    EditText et_currentKg;
    ListView listView;
    Cursor cursor;
    RouteCursorAdapter adapter;
    RouteDBHelper helper;
    SQLiteDatabase db;
    ArrayList<route> routeList;
    routeDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("current", "시작");

        listView = findViewById(R.id.lv_main);
        helper = new RouteDBHelper(this);
        db = helper.getWritableDatabase();
        dbManager = new routeDBManager(this);
        et_targetKg = findViewById(R.id.et_targetKg);
        et_currentKg = findViewById(R.id.et_currentKg);

        SharedPreferences pref = getSharedPreferences("saveState", MODE_PRIVATE);
        et_targetKg.setText(pref.getString("saveTargetKg",""));
        et_currentKg.setText(pref.getString("saveCurrentKg",""));

        cursor = db.rawQuery("SELECT * FROM route_table;", null);

        adapter = new RouteCursorAdapter(this, R.layout.listview_route, cursor);
//        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor,
//                new String[]{"_id", "address"},
//                new int[]{android.R.id.text1, android.R.id.text2},
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);

        //일정 클릭시 주변정보
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                route searchRoute = dbManager.getRouteById(id);
                Intent intent = new Intent(MainActivity.this, routeInfoActivity.class);
                intent.putExtra("searchRoute", searchRoute);
                startActivity(intent);
            }
        });

        //일정 롱클릭시 삭제 다이얼로그
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("일정 삭제")
                        .setMessage("선택한 일정을 삭제하시겠습니까?")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //DB에서 일정 삭제하기
                                if(dbManager.removeRoute(id)){
                                    Toast.makeText(MainActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                                    onResume();
                                } else{
                                    Toast.makeText(MainActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        db = helper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM route_table;", null);
        adapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences("saveState", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("saveCurrentKg", et_currentKg.getText().toString());
        edit.putString("saveTargetKg", et_targetKg.getText().toString());
        edit.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences pref = getSharedPreferences("saveState", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("saveCurrentKg", et_currentKg.getText().toString());
        edit.putString("saveTargetKg", et_targetKg.getText().toString());
        edit.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor != null) cursor.close();
        SharedPreferences pref = getSharedPreferences("saveState", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("saveCurrentKg", et_currentKg.getText().toString());
        edit.putString("saveTargetKg", et_targetKg.getText().toString());
        edit.commit();

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_route:
                Intent intent = new Intent(this, addRouteActivity.class);
                startActivityForResult(intent, REQ_ROUTE);
                break;
            case R.id.btn_food:
                Intent intent2 = new Intent(this, myFoodActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_ROUTE:
                if(resultCode == RESULT_OK){
                    routeList = (ArrayList<route>) data.getSerializableExtra("routeList");
                    for(int i = 0; i < routeList.size(); i++){
                        if(dbManager.addNewRoute(routeList.get(i)))
                            Toast.makeText(this, "일정 추가 완료", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(this, "일정 추가 실패", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}