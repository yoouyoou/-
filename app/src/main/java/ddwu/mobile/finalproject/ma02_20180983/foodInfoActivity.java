package ddwu.mobile.finalproject.ma02_20180983;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.kakao.kakaolink.v2.KakaoLinkCallback;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.kakaolink.v2.network.KakaoLinkImageService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class foodInfoActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 100;

    TextView tvName;
    TextView tvVolume;
    TextView tvKcal;
    TextView tvCarbo;
    TextView tvProtein;
    TextView tvFat;
    TextView tvSugar;
    TextView tvNatrium;
    TextView tvChole;
    TextView tvFattyAcid;
    TextView tvTransFat;
    TextView tvMaker;

    ImageView imgView;
    private String mCurrentPhotoPath = "0";
    food searchFood;
    foodDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodinfo);

        tvName = findViewById(R.id.tv_nameInfo);
        tvVolume = findViewById(R.id.tv_volInfo);
        tvKcal = findViewById(R.id.tv_kcalInfo);
        tvCarbo = findViewById(R.id.tv_carboInfo);
        tvProtein = findViewById(R.id.tv_proteinInfo);
        tvFat = findViewById(R.id.tv_fatInfo);
        tvSugar = findViewById(R.id.tv_sugarInfo);
        tvNatrium = findViewById(R.id.tv_natInfo);
        tvChole = findViewById(R.id.tv_choleInfo);
        tvFattyAcid = findViewById(R.id.tv_fatAcidInfo);
        tvTransFat = findViewById(R.id.tv_transInfo);
        tvMaker = findViewById(R.id.tv_makerInfo);
        imgView = findViewById(R.id.camera);
        dbManager = new foodDBManager(this);

        searchFood = (food) getIntent().getSerializableExtra("searchFood");
        Log.d("current2", "id로 찾은 food객체: " + searchFood.getName());
        tvName.setText(searchFood.getName() + " 영양정보");
        tvVolume.setText(String.valueOf(searchFood.getVolume()) + "g");
        tvKcal.setText(String.valueOf(searchFood.getKcal()) + "kcal");
        tvCarbo.setText(String.valueOf(searchFood.getCarbohydrate()) + "g");
        tvProtein.setText(String.valueOf(searchFood.getProtein()) + "g");
        tvFat.setText(String.valueOf(searchFood.getFat()) + "g");
        tvSugar.setText(String.valueOf(searchFood.getSugar()) + "g");
        tvNatrium.setText(String.valueOf(searchFood.getNatrium()) + "mg");
        tvChole.setText(String.valueOf(searchFood.getCholesterol()) + "mg");
        tvFattyAcid.setText(String.valueOf(searchFood.getFattyAcid()) + "g");
        tvTransFat.setText(String.valueOf(searchFood.getTransfat()) +"g");
        tvMaker.setText(searchFood.getMaker());

        Log.d("foodInfoActivity", "searchFood id: " + searchFood.get_id());
        Log.d("foodInfoActivity", "searchFood name: " + searchFood.getName());
        Log.d("foodInfoActivity", "searchFood Photo: " + searchFood.getPhoto());
        if(searchFood.getPhoto().equals("0")) {
            Log.d("current", "food객체의 포토값 없음:" + searchFood.getPhoto());
        } else{
            Log.d("current", "food객체의 사진경로가 존재할 경우: " + searchFood.getPhoto());
            setPic(searchFood.getPhoto());
        }

        Log.i("current", "외부공용저장소 확인: " + getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        Log.i("current", "외부파일폴더 확인?: " + getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());

    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_camera:
                dispatchTakePictureIntent();
                break;
            case R.id.btn_ok:
                //DB에 mCurrentPhotoPath저장하기
                Log.d("foodInfoActivity", "현재 mCurrnetPhotoPath: " + mCurrentPhotoPath);
                if (!mCurrentPhotoPath.equals("0"))
                    dbManager.setPhotoById(searchFood.get_id(), mCurrentPhotoPath); //mCurrentPhotoPath
                finish();
                break;
            case R.id.btn_share:
                Toast.makeText(this, "sns공유 버튼 클릭", Toast.LENGTH_SHORT).show();
                LinkObject link = LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .build();
                TemplateParams params = TextTemplate.newBuilder(searchFood.getName()+"영양성분\n더 자세한 사항이 궁금하면 클릭", link)
                        .setButtonTitle("This is button")
                        .build();
                // 기본 템플릿으로 카카오링크 보내기
                KakaoLinkService.getInstance()
                        .sendDefault(foodInfoActivity.this, params, new ResponseCallback<KakaoLinkResponse>() {
                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                Log.e("KAKAO_API", "카카오링크 공유 실패: " + errorResult);
                                Toast.makeText(foodInfoActivity.this, "카카오링크 공유 실패: " + errorResult, Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onSuccess(KakaoLinkResponse result) {
                                Log.i("KAKAO_API", "카카오링크 공유 성공");
                                Toast.makeText(foodInfoActivity.this, "카카오링크 공유 성공", Toast.LENGTH_SHORT).show();
                                // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                                Log.w("KAKAO_API", "warning messages: " + result.getWarningMsg());
                                Log.w("KAKAO_API", "argument messages: " + result.getArgumentMsg());
                                Toast.makeText(foodInfoActivity.this, "Warning messages: " + result.getWarningMsg() + " / Argument message: " + result.getArgumentMsg(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }

    /*원본 사진 파일 저장*/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            } catch(IOException e){
                e.printStackTrace();
            }
            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this, "ddwu.mobile.finalproject.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /*현재 시간 정보를 사용하여 파일 정보 생성*/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /*사진의 크기를 ImageView에서 표시할 수 있는 크기로 변경*/
    private void setPic(String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = imgView.getWidth();
        int targetH = imgView.getHeight();
        Log.d("setPic", "targetW: " + imgView.getWidth());
        Log.d("setPic", "targetH: " + imgView.getHeight());
        if(targetW == 0)
            targetW = 777;
        if(targetH == 0)
            targetH = 499;


        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        Log.d("setPic", "photoW: " + photoW);
        Log.d("setPic", "photoH: " + photoH);

        // Determine how much to scale down the image
        //if(targetW != 0 && targetH != 0)
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        Log.d("setPic", "scaleFactor: " + scaleFactor);


        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imgView.setImageBitmap(bitmap);
    }

    /*사진촬영 완료시*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d("foodInfoActivity", "사진촬영 완료시 mCurrentPhotoPath: " + mCurrentPhotoPath);
            setPic(mCurrentPhotoPath);
        }
    }

}
