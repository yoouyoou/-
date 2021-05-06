package ddwu.mobile.finalproject.ma02_20180983;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class routeMapActivity extends AppCompatActivity {
    final static int PERMISSION_REQ_CODE = 100;

    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private Marker destinationMarker;
    private MarkerOptions options;
    HashMap<String, String> hashData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routemap);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        hashData = (HashMap<String, String>) getIntent().getSerializableExtra("hashData");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.route_map);
        mapFragment.getMapAsync(mapReadyCallBack);


//        if(checkPermission()) {     //퍼미션 있는지 없는지 체크
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    3000, 0, locationListener); //gps로부터 5초,0m마다 리스너동작
//        }
    }

    LocationListener locationListener = new LocationListener(){
        //location정보가 들어오면 현재 위치바꾸기
        @Override
        public void onLocationChanged(Location location) {
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
            //currentMarker.setPosition(currentLoc);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onProviderDisabled(String provider) { }
    };


    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback(){
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;

            //맵 로딩 후 내 위치 표시버튼 관련 설정
//            if(checkPermission()) {
//                mGoogleMap.setMyLocationEnabled(true);
//            }
            //mGoogleMap.setOnMyLocationButtonClickListener(locationButtonClickListener);
            //mGoogleMap.setOnMyLocationClickListener(locationClickListener);

            //해쉬로 받아온 마커값 찍기
            LatLng destinationLoc = new LatLng(Double.valueOf(hashData.get("placeLat")), Double.valueOf(hashData.get("placeLong")));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLoc, 17));
            options = new MarkerOptions();
            options.position(destinationLoc);
            options.title(hashData.get("placeName"));
            options.snippet("목적지");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            destinationMarker = mGoogleMap.addMarker(options);
            destinationMarker.showInfoWindow();
        }

//        //내위치확인 버튼클릭 처리 리스너
//        GoogleMap.OnMyLocationButtonClickListener locationButtonClickListener =
//                new GoogleMap.OnMyLocationButtonClickListener(){
//                    @Override
//                    public boolean onMyLocationButtonClick() {
//                        return false;
//                    }
//            };
//
//        //지도 상의 현재 위치 아이콘 클릭 처리 리스너
//        GoogleMap.OnMyLocationClickListener locationClickListener =
//                new GoogleMap.OnMyLocationClickListener(){
//                    @Override
//                    public void onMyLocationClick(@NonNull Location location) {
//                        Toast.makeText(routeMapActivity.this, "이동중", Toast.LENGTH_SHORT).show();
//                    }
//                };
    };

    private boolean checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ_CODE);
                return false; //퍼미션 없을시 다이얼로그 띄움
            }
        }
        return true;
    }

}
