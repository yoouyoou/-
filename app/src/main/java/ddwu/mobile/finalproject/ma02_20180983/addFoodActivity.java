package ddwu.mobile.finalproject.ma02_20180983;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class addFoodActivity extends AppCompatActivity {
    EditText etSearch;
    ListView listView;
    foodDBManager dbManager;
    //Array어댑터 사용시
//    ArrayAdapter<String> adapter;
//    ArrayList<String> foodNameList;
//    searchXmlParser parser;
    //해쉬맵 사용 시
//    SimpleAdapter adapter;
//    ArrayList<HashMap<String, String>> mapList;
//    searchXmlParser parser;
    //커스텀어댑터 사용시
    AddFoodAdapter adapter;
    ArrayList<food> foodList;
    foodXmlParser parser;
    String apiAddress;
    String data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfood);
        etSearch = findViewById(R.id.tv_search);
        listView = findViewById(R.id.lv_search);
        dbManager = new foodDBManager(this);
        //커스텀어댑터 사용 시
        foodList = new ArrayList();
        adapter = new AddFoodAdapter(this, R.layout.listview_food, foodList);
        listView.setAdapter(adapter);
        parser = new foodXmlParser();
        //parser = new searchXmlParser();
        apiAddress = "http://openapi.foodsafetykorea.go.kr/api/41af611c4953451294a7/I2790/xml/1/20/DESC_KOR=";
        Log.d("current", "addFoodActivity 내부");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(addFoodActivity.this, "아이템 클릭", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(addFoodActivity.this);
                builder.setTitle("음식 추가")
                        .setMessage(foodList.get(position).getName() + "을 추가하시겠습니까?")
                        .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("addFood", foodList.get(position));
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.imageButton:
                data = etSearch.getText().toString();
                try{
                    if (!isOnline()) {
                        Toast.makeText(addFoodActivity.this, "네트워크를 사용가능하게 설정해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    apiAddress += URLEncoder.encode(data, "UTF-8");
                    new NetworkAsynTask().execute(apiAddress);
                } catch(UnsupportedEncodingException e){
                    e.printStackTrace();    //인코딩 수행이 제대로 안될경우
                }
                break;
        }
    }

    class NetworkAsynTask extends AsyncTask<String, Integer, String>{
        ProgressDialog progressDlg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("current", "onPreExecute");
            progressDlg = ProgressDialog.show(addFoodActivity.this, "Wait", "Searching...");
        }
        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            Log.d("current", "doInBackgroud 주소: " + address);

            InputStream stream = null;
            HttpURLConnection conn= null;
            String result = null;
            try{
                URL url = new URL(address);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int responseCode = conn.getResponseCode();
                if(responseCode != HttpsURLConnection.HTTP_OK){
                    throw new IOException("HTTP error code: " + conn.getResponseCode());
                }

                stream = conn.getInputStream();
                if(stream != null)
                    result = readStreamToString(stream);

                Log.d("current", "파서 시작");
                if(result != null)
                    //mapList = parser.parse(result);
                    foodList = parser.parse(result);
                else
                    return "Error";
                Log.d("current", "파서한 후 foodList개수: " + foodList.size());
            } catch(Exception e){
                e.printStackTrace();
            } finally {
                if(stream != null) {
                    try { stream.close(); }
                    catch (IOException e) { e.printStackTrace(); }
                }
                if(conn != null)
                    conn.disconnect();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            Log.d("current", "onPostExecute");
            //simple_1 사용 시
//            adapter = new ArrayAdapter<String>(addFoodActivity.this, android.R.layout.simple_list_item_1, foodNameList);
//            listView.setAdapter(adapter);
            //해쉬맵 사용 시
//            adapter = new SimpleAdapter(addFoodActivity.this, mapList, android.R.layout.simple_list_item_2,
//                    new String[]{"name", "maker"}, new int[]{android.R.id.text1, android.R.id.text2});
//            Log.d("current", "아 왜 안나와: " + mapList.get(0).get("name"));
//            listView.setAdapter(adapter);
            //커스텀 어댑터 사용시
            etSearch.setText("");
            data = "";
            adapter.setList(foodList);
            progressDlg.dismiss();
        }
    }

    /* InputStream을 전달받아 문자열로 변환 후 반환 */
    protected String readStreamToString(InputStream stream){
        Log.d("current", "readStreamToString내부");
        StringBuilder result = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine = bufferedReader.readLine();

            while (readLine != null) {
                result.append(readLine + "\n");
                readLine = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("current", "readStreamToString결과: " + result.toString());
        return result.toString();
    }

    /* 네트워크 환경 조사 */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}

