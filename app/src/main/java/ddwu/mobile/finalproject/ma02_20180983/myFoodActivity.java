package ddwu.mobile.finalproject.ma02_20180983;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class myFoodActivity extends AppCompatActivity {
    final int ADD_FOOD_CODE = 100;

    ListView listView;
    foodDBManager dbManager;
    FoodDBHelper helper;
    SQLiteDatabase db;
    FoodCursorAdapter adapter;
    Cursor cursor;

    private FloatingActionButton fab;
    TextView totalKcal;
    //SimpleCursorAdapter adapter;
    ArrayList<food> myFoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfood);

        fab = findViewById(R.id.fab);
        totalKcal = findViewById(R.id.tv_totalKcal);
        totalKcal.setText("0");
        listView = (ListView) findViewById(R.id.lv_myfood);

        helper = new FoodDBHelper(this);
        db = helper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM food_table;", null);
        myFoodList = new ArrayList<food>();
        dbManager = new foodDBManager(this);

        adapter = new FoodCursorAdapter(myFoodActivity.this, R.layout.listview_food, cursor);

//        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor,
//                new String[]{"name", "kcal"},
//                new int[]{android.R.id.text1, android.R.id.text2},
//                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);

        //음식 추가
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myFoodActivity.this, addFoodActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        //음식 롱클릭 시 삭제 다이얼로그
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //다이얼로그 띄우고 삭제할건지 물어보기.
                AlertDialog.Builder builder = new AlertDialog.Builder(myFoodActivity.this);
                builder.setTitle("음식 삭제")
                        .setMessage("선택한 음식을 삭제하시겠습니까?")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //DB에서 음식 삭제하기
                                if(dbManager.removeFood((int)id)){
                                    Toast.makeText(myFoodActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                                    onResume();
                                } else{
                                    Toast.makeText(myFoodActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }
        });

        //음식 클릭시 상세정보
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                food searchFood = dbManager.getFoodById(id);
                Intent intent = new Intent(myFoodActivity.this, foodInfoActivity.class);
                Log.d("current2", "리스뷰 클릭 id값: " + id);
                intent.putExtra("searchFood", searchFood);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = helper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM food_table;", null);
        totalKcal.setText(  (String.valueOf(dbManager.getTotalKcal()))  );
        adapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor != null)
            cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_FOOD_CODE){
            switch(resultCode){
                case RESULT_OK:
                    food addFood = (food) data.getSerializableExtra("addFood");
                    if(dbManager.addNewFood(addFood)){
                        Toast.makeText(myFoodActivity.this,"추가 완료", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(myFoodActivity.this, "추가 실패", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(myFoodActivity.this, "추가 안함", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
