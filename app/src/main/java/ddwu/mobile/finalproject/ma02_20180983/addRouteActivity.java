package ddwu.mobile.finalproject.ma02_20180983;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class addRouteActivity extends AppCompatActivity {

    final static int PERMISSION_REQ_CODE = 100;
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;

    private ArrayList<Marker> markerList;
    private Marker centerMarker;
    private MarkerOptions options;

    //위치 검색용
    EditText etRoute;
    Geocoder geocoder;
    List<Address> geoList = null;
    ArrayList<route> routeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addroute);

        etRoute = findViewById(R.id.et_route);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        markerList = new ArrayList();
        routeList = new ArrayList();

        if(checkPermission()) {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(mapReadyCallBack); //map로딩완료시 콜백메소드
        }

        geocoder = new Geocoder(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQ_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //퍼미션 획득 시 수행
                if(checkPermission()){
                    locationUpdate();
                }
            }
        }else{
            Toast.makeText(this, "앱 실행을 위한 권한이 필요함", Toast.LENGTH_SHORT).show();
        }
    }

    private void locationUpdate(){
        if(checkPermission()){
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng curLoc;
            if(loc != null) {
                Log.d("loc", "map초기: " + loc.getLatitude() + ", " + loc.getLongitude());
                curLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
            } else{
                curLoc = new LatLng(37.604094, 127.042463);
            }
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 17));
            options = new MarkerOptions();
            options.position(curLoc);
            options.title("현재위치");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            Marker marker = mGoogleMap.addMarker(options);
            marker.showInfoWindow();
        }
    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_route_search:
                Log.d("map", "지도검색");
                //여기서는 지오코딩 수행해서 지도에 마커찍어야함
                String data = etRoute.getText().toString();
                try{
                    geoList = geocoder.getFromLocationName(data, 3);
                } catch(IOException e){
                    e.printStackTrace();
                }
                if(geoList != null){
                    if(geoList.size() == 0)
                        etRoute.setText("주소 오류");
                    else {
                        MarkerOptions sOptions = new MarkerOptions();
                        LatLng searchLoc = new LatLng(geoList.get(0).getLatitude(), geoList.get(0).getLongitude());
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchLoc, 17));
                        sOptions.position(searchLoc);
                        sOptions.title(data);
                        sOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        centerMarker = mGoogleMap.addMarker(sOptions);
                        centerMarker.showInfoWindow();
                        //etRoute.setText(geoList.get(0).getAdminArea());
                        Log.d("map", "검색 주소: " + geoList.get(0).toString());
                        Log.d("map", "반환 크기" + geoList.size());
                    }
                } else{
                    Toast.makeText(this, "해당하는 주소가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_route_ok:
                Log.d("map", "추가완료버튼");
                Intent resultIntent = new Intent();
                resultIntent.putExtra("routeList", routeList);
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
        }
    }

    /*
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            locationManager.removeUpdates(locationListener);
        }
        @Override
        public void onProviderEnabled(String provider) {

        }
        @Override
        public void onProviderDisabled(String provider) {

        }
    };
     */

    //map정보호출 완료
    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap; //맵 객체 준비 완료
            locationUpdate();

//            if(checkPermission()) {
//                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                //Log.d("loc", "map초기: " + loc.getLatitude() + ", " + loc.getLongitude());
//                LatLng curLoc = new LatLng(loc.getLatitude(), loc.getLongitude());
//                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 17));
//                options = new MarkerOptions();
//                options.position(curLoc);
//                options.title("현재위치");
//                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                mGoogleMap.addMarker(options);
//            }

//            //LatLng temp = new LatLng(37.606320, 127.041808);
//            //고정된 위치로 화면 옮기기
//            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temp, 17));
//            options = new MarkerOptions();
//            options.position(temp);
//            options.title("현재위치");
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//
//            centerMarker = mGoogleMap.addMarker(options);
//            markerList.add(centerMarker);

            //지도 롱클릭 시
            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
                @Override
                public void onMapLongClick(LatLng latLng) {
                    Log.d("map", "map롱클릭메소드");
                    Marker longMarker = null;
                    LatLng longLoc = new LatLng(latLng.latitude, latLng.longitude);
                    try {
                        geoList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(geoList != null){
                        if(geoList.size() != 0) {
                            options.title(geoList.get(0).getThoroughfare() + " "+ geoList.get(0).getSubThoroughfare());
                            options.snippet(geoList.get(0).getSubLocality()+"구 " + geoList.get(0).getFeatureName()+"번지");
                        } else {
                            options.title("클릭 위치");
                        }
                        Log.d("map", "getCountryName(): " + geoList.get(0).getCountryName());
                        Log.d("map", "getAddressLine(0): " + geoList.get(0).getAddressLine(0));
                        Log.d("map", "getAddressLine(1): " + geoList.get(0).getAddressLine(1));
                        Log.d("map", "getAddressLine(2): " + geoList.get(0).getAddressLine(2));
                        Log.d("map", "getAdminArea: " + geoList.get(0).getAdminArea());
                        Log.d("map", "getSubAdminArea: " + geoList.get(0).getSubAdminArea());
                        Log.d("map", "getFeatureName: " + geoList.get(0).getFeatureName());
                        Log.d("map", "getSubLocality: " + geoList.get(0).getSubLocality());
                        Log.d("map", "getLocality(): " + geoList.get(0).getLocality());
                        Log.d("map", "getExtras(): " + geoList.get(0).getExtras());
                    }
//                    startAddressService(latLng.latitude, latLng.longitude);
                    options.position(longLoc);
//                    options.snippet("위도: " + formatter.format(currentLoc.latitude)
//                            + ", 경도: " + formatter.format(currentLoc.longitude));
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    longMarker = mGoogleMap.addMarker(options);
                    markerList.add(longMarker);
                }
            });

            //마커 말풍선 클릭 시
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
                @Override
                public void onInfoWindowClick(Marker marker) {
                    //일정에 추가하시겠습니까 다이얼로그 띄우기
                    AlertDialog.Builder builder = new AlertDialog.Builder(addRouteActivity.this);
                    builder.setTitle(marker.getTitle() + "을 일정에 추가하시겠습니까?")
                            .setNegativeButton("취소", null)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //일정리스트에 추가하기
                                    route r = new route(marker.getTitle(), marker.getPosition().latitude, marker.getPosition().longitude);
                                    routeList.add(r);
                                    Toast.makeText(addRouteActivity.this, "일정에 추가되었습니다!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });

        }
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
