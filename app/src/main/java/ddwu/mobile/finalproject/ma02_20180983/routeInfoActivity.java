package ddwu.mobile.finalproject.ma02_20180983;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static noman.googleplaces.PlaceType.*;


public class routeInfoActivity extends AppCompatActivity {
    String api_key = "AIzaSyDBqqH7UcwVR2iOAYuzNcyrRnSJykW8diY";

    TextView tvRoute;
    ListView lvFitness;
    ListView lvPark;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    ArrayList<String> fitnessList;
    ArrayList<String> parkList;

    ArrayList<HashMap<String, String>> hashList;
    HashMap<String, String> hashData;
    ArrayList<HashMap<String, String>> hashList2;
    HashMap<String, String> hashData2;


    route searchRoute;
    private PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeinfo);

        tvRoute = findViewById(R.id.tv_routeInfo);
        lvFitness = findViewById(R.id.lv_fitness);
        lvPark = findViewById(R.id.lv_park);

        fitnessList = new ArrayList<String>();
        parkList = new ArrayList<String>();

        hashList = new ArrayList<HashMap<String, String>>();
        hashList2 = new ArrayList<HashMap<String, String>>();

        Places.initialize(getApplicationContext(), api_key);
        placesClient = Places.createClient(this);

        searchRoute = (route) getIntent().getSerializableExtra("searchRoute");
        tvRoute.setText(searchRoute.getAddress() + "주변에서 잠깐이라도 운동하는게 어떠세요?");
        Log.d("current", "일정객체: " + searchRoute.getAddress());

        searchStart(GYM);
        searchStart(PARK);

        //헬스장 클릭시 세부정보
        lvFitness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("current", "헬스장 클릭이름: " + hashList.get(position).get("placeName"));
                Intent intent = new Intent(routeInfoActivity.this, routeMapActivity.class);
                intent.putExtra("hashData", hashList.get(position));
                startActivity(intent);
            }
        });

        //공원 클릭 시 세부정보
        lvPark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("map", "포지션 값: " + position);
                Log.d("map", "공원 클릭이름: " + hashList2.get(position).get("placeName"));
                Intent intent = new Intent(routeInfoActivity.this, routeMapActivity.class);
                intent.putExtra("hashData", hashList2.get(position));
                startActivity(intent);
            }
        });

    }

    //입력된 유형의 주변정보 검색
    private void searchStart(String type){
        if(type.equals("gym")) {
            Log.d("current", "gym서치 내부: " + searchRoute.getLatitude()+","+searchRoute.getLongitude());
            new NRPlaces.Builder().listener(placesListener)
                    .key(api_key)
                    .latlng(searchRoute.getLatitude(), searchRoute.getLongitude())
                    .radius(1000)
                    .type(type)
                    .build()
                    .execute();
        }
        if(type.equals("park")){
            Log.d("current", "park서치 내부: " + searchRoute.getLatitude()+","+searchRoute.getLongitude());
            new NRPlaces.Builder().listener(placesListener2)
                    .key(api_key)
                    .latlng(searchRoute.getLatitude(), searchRoute.getLongitude())
                    .radius(1000)
                    .type(type)
                    .build()
                    .execute();
        }
    }

    //헬스장
    PlacesListener placesListener = new PlacesListener(){
        @Override
        public void onPlacesStart() { }
        @Override
        public void onPlacesFinished() { }
        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
            Log.d("current", "노맨 리스너 성공동작");
            Log.d("current", "결과 개수: " + places.size());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(noman.googleplaces.Place place : places){
                        hashData = new HashMap<>();
                        fitnessList.add(place.getName());
                        hashData.put("placeId", place.getPlaceId());
                        hashData.put("placeName", place.getName());
                        hashData.put("placeLat", String.valueOf(place.getLatitude()));
                        hashData.put("placeLong", String.valueOf(place.getLongitude()));
                        hashList.add(hashData);
                        Log.d("map", place.getPlaceId() +": " + place.getName());
                    }
                    //어댑터 연결
                    adapter = new ArrayAdapter<String>(routeInfoActivity.this, android.R.layout.simple_list_item_1, fitnessList);
                    lvFitness.setAdapter(adapter);
                }
            });
        }
        @Override
        public void onPlacesFailure(PlacesException e) {
            Log.d("current", "노맨 리스너 실패동작");
            e.printStackTrace();
            Toast.makeText(routeInfoActivity.this, "정보수신 실패", Toast.LENGTH_SHORT).show();
        }
    };

    //공원
    PlacesListener placesListener2 = new PlacesListener(){
        @Override
        public void onPlacesStart() { }
        @Override
        public void onPlacesFinished() { }
        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
            Log.d("current", "노맨 리스너 성공동작");
            Log.d("current", "결과 개수: " + places.size());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(noman.googleplaces.Place place : places){
                        hashData2 = new HashMap<>();
                        parkList.add(place.getName());
                        hashData2.put("placeId", place.getPlaceId());
                        hashData2.put("placeName", place.getName());
                        hashData2.put("placeLat", String.valueOf(place.getLatitude()));
                        hashData2.put("placeLong", String.valueOf(place.getLongitude()));
                        hashList2.add(hashData2);
                        Log.d("map", "현재까지 해쉬리스트 개수: " + hashList2.size());
                        Log.d("map", "방금 추가된 해쉬이름 값: " + hashData2.get("placeName"));
                        Log.d("map", place.getPlaceId() +": " + place.getName());
                    }
                    //어댑터 연결
                    adapter2 = new ArrayAdapter<String>(routeInfoActivity.this, android.R.layout.simple_list_item_1, parkList);
                    lvPark.setAdapter(adapter2);
                }
            });
        }
        @Override
        public void onPlacesFailure(PlacesException e) {
            Log.d("current", "노맨 리스너 실패동작");
            e.printStackTrace();
            Toast.makeText(routeInfoActivity.this, "정보수신 실패", Toast.LENGTH_SHORT).show();
        }
    };

    /*Place ID 의 장소에 대한 세부정보 획득*/
    private void getPlaceDetail(String placeId) {
        List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.PHONE_NUMBER, com.google.android.libraries.places.api.model.Place.Field.ADDRESS);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(
                new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse response) {
                        com.google.android.libraries.places.api.model.Place place = response.getPlace();
                        Log.d("map", "Place found: " + place.getName());
                        Log.d("map", "Phone: " + place.getPhoneNumber());
                        Log.d("map", "Address: " + place.getAddress());
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof ApiException){  //ApiException(구글서버쪽)에 속한 개체라면
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e("map", "Place not found: " + statusCode + " " + e.getMessage());
                        }
                    }
                }
        );
    }

}
