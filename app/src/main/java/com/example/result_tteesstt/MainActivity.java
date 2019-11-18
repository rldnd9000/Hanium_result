package com.example.result_tteesstt;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class MainActivity extends AppCompatActivity implements LocationListener {
    private ArrayList<String>colorList;
    private ArrayList<String>stringList;
    private ArrayList<String>numberList;
    private ArrayList<String>boolList;
    private JSONArray jsonArray;

    LocationManager locationManager;
    double latitude;
    double longitude;
    TextView  weather1,b1,b2;

    ImageView Im1,Im2,Im3,Im5,Im6;

    String jsonplease, c1;

    private static final String TAG = "android_camera_example";
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK; // Camera.CameraInfo.CAMERA_FACING_FRONT

    private SurfaceView surfaceView;
    private CameraPreview mCameraPreview;
    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)

    public static double lat1 , long1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("개발 중");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 화면 켜진 상태를 유지합니다.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**
             * 사용자 단말기의 권한 중 "카메라" 권한이 허용되어 있는지 확인한다.
             *  Android는 C언어 기반으로 만들어졌기 때문에 Boolean 타입보다 Int 타입을 사용한다.
             */
            int permissionResult = checkSelfPermission(Manifest.permission.CAMERA);


            /** * 패키지는 안드로이드 어플리케이션의 아이디이다. *
             *  현재 어플리케이션이 카메라에 대해 거부되어있는지 확인한다. */
            if (permissionResult == PackageManager.PERMISSION_DENIED) {


                /** * 사용자가 CALL_PHONE 권한을 거부한 적이 있는지 확인한다. *
                 * 거부한적이 있으면 True를 리턴하고 *
                 * 거부한적이 없으면 False를 리턴한다. */
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("권한이 필요합니다.").setMessage("이 기능을 사용하기 위해서는 단말기의 \"카메라\" 권한이 필요합니다. 계속 하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    /** * 새로운 인스턴스(onClickListener)를 생성했기 때문에 *
                                     * 버전체크를 다시 해준다. */
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        // CALL_PHONE 권한을 Android OS에 요청한다.
                                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 1000);
                                    }
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
                                }
                            }).create().show();


                    //     return -1;
                    // need to retry
                }
                // 최초로 권한을 요청할 때
                else {
                    // CALL_PHONE 권한을 Android OS에 요청한다.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1000);
                }
            }
            // CALL_PHONE의 권한이 있을 때
            else {
            }
        } //cameraPlzPlz 로 카메라캡쳐 기능 구현 이제 졸작앱에 적용시키자 어느 액티비티던 실행가능하게
        // 스레드로 구현하자

        mLayout = findViewById(R.id.layout_main);
        surfaceView = findViewById(R.id.camera_preview_main);


        // 런타임 퍼미션 완료될때 까지 화면에서 보이지 않게 해야합니다.
        surfaceView.setVisibility(View.GONE);

            ImageView Im1 = (ImageView) findViewById(R.id.im1);
            ImageView Im2 = (ImageView) findViewById(R.id.im2);
            ImageView Im3 = (ImageView) findViewById(R.id.im3);
            ImageView Im5 = (ImageView) findViewById(R.id.im5);
            ImageView Im6 = (ImageView) findViewById(R.id.im6);
            ImageView Im7 = (ImageView) findViewById(R.id.im7);

            Im1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DrivigRecord.class);
                    startActivity(intent);
                }
            });



            Im3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, roadSearch2.class);
                    startActivity(intent);

                }
            });
            Im2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, sagoalbumJava.class);
                    startActivity(intent);
                }

            });


            initView();

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            requestLocation();

   //    if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

   //        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
   //        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


   //        if (cameraPermission == PackageManager.PERMISSION_GRANTED
   //                && writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
   //            startCamera();


   //        } else {
   //            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
   //                    || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

   //                Snackbar.make(mLayout, "이 앱을 실행하려면 카메라와 외부 저장소 접근 권한이 필요합니다.",
   //                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

   //                    @Override
   //                    public void onClick(View view) {

   //                        ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
   //                                PERMISSIONS_REQUEST_CODE);
   //                    }
   //                }).show();


   //            } else {
   //                // 2. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
   //                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
   //                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
   //                        PERMISSIONS_REQUEST_CODE);
   //            }

   //        }

   //    } else {

   //        final Snackbar snackbar = Snackbar.make(mLayout, "디바이스가 카메라를 지원하지 않습니다.",
   //                Snackbar.LENGTH_INDEFINITE);
   //        snackbar.setAction("확인", new View.OnClickListener() {
   //            @Override
   //            public void onClick(View v) {
   //                snackbar.dismiss();
   //            }
   //        });
   //        snackbar.show();
   //    }



        }



    private void initView() {

        weather1 = (TextView) findViewById(R.id.weather1);
         b1 = (TextView)findViewById(R.id.b1);
         b2 = (TextView)findViewById(R.id.b2);
        colorList = new ArrayList<>();
        stringList = new ArrayList<>();
        numberList = new ArrayList<>();
        boolList = new ArrayList<>();

        //button.setOnClickListener(this);
    }


    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lat1 = location.getLatitude();
        long1 = location.getLongitude();
        getWeather(latitude, longitude);
        locationManager.removeUpdates(MainActivity.this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void requestLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }
    }



    private interface ApiService {
        String BASEURL = "http://api.openweathermap.org/";
        String APPKEY = "ba26ffcb4487279a887f7d5ec72e648e";

        @GET("data/2.5/weather")
        Call<JsonObject> getHourly(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String appKey); //자리맞춤 필수

    }

    private void getWeather(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.BASEURL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonObject> call = apiService.getHourly(latitude, longitude, ApiService.APPKEY);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject object = response.body();
                    if (object != null) {
                        jsonplease = object.toString();
                        //Log.d(TAG,"asd" + jsonplease);
                        jsonRead();
                    }
                }
            }

            private void jsonRead() {
                 // try {
              JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonplease);
                      JsonObject dataObject = (JsonObject) jsonObject.get("main");
                JsonObject dataObject1 = (JsonObject) jsonObject.get("wind");

                      try {
                          JSONObject root = new JSONObject(jsonplease);
                          JSONArray ja = root.getJSONArray("weather");

                          for(int i = 0; i < ja.length();i++)
                          {
                              JSONObject jo = ja.getJSONObject(i);
                             String id = jo.getString("description");

                             String temp1 = dataObject.get("temp").toString();
                             double temp2 = Double.parseDouble(temp1);
                             double temp3 = temp2 -273.15;
                              String strNumber = String.format("%.1f", temp3);
                              b1.setText( strNumber + "°C");
                              b2.setText(dataObject1.get("speed").toString() + "m/s");

                              switch (id) {
                                  case "clear sky":
                                      weather1.setText("맑음");
                                      break;
                                  case "few clouds":
                                      weather1.setText("구름 조금");
                                      break;
                                  case "scattered clouds":
                                      weather1.setText("구름 개임");
                                      break;
                                  case "broken clouds":
                                      weather1.setText("드문 구름");
                                      break;
                                  case "shower rain":
                                      weather1.setText("소나기");
                                      break;
                                  case "rain":
                                      weather1.setText("비");
                                      break;
                                  case "thunderstorm":
                                      weather1.setText("천둥 번개");
                                      break;
                                  case "snow":
                                      weather1.setText("눈");
                                      break;
                                  case "mist":
                                      weather1.setText("안개 많음");
                                      break;
                                  case "haze":
                                      weather1.setText("실 안개");
                                      break;
                                  default:
                                      weather1.setText(id+" ");
                              }
                              }
                      }
                      catch(JSONException e){
                          e.printStackTrace();
                      }
            }



            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            }
        });
    }

 // void startCamera() {

 //     // Create the Preview view and set it as the content of this Activity.
 //     mCameraPreview = new CameraPreview(this, this, CAMERA_FACING, surfaceView);

 // }


 //  @Override
 //  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {

 //      if (requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

 //          boolean check_result = true;

 //          for (int result : grandResults) {
 //              if (result != PackageManager.PERMISSION_GRANTED) {
 //                  check_result = false;
 //                  break;
 //              }
 //          }

 //          if (check_result) {

 //              startCamera();
 //          } else {

 //              if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
 //                      || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

 //                  Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
 //                          Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

 //                      @Override
 //                      public void onClick(View view) {

 //                          finish();
 //                      }
 //                  }).show();

 //              } else {

 //                  Snackbar.make(mLayout, "설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
 //                          Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

 //                      @Override
 //                      public void onClick(View view) {

 //                          finish();
 //                      }
 //                  }).show();
 //              }
 //          }

 //      }


 //  }

 //  class ThreadEx1_2 implements Runnable {
 //      @Override
 //      public void run() {
 //          boolean start_stop = true;
 //          while (start_stop) {
 //              try {
 //                  // Thread.currentThread() -> 현재 실행중인 쓰레드 반환
 //                  mCameraPreview.takePicture();
 //                  start_stop = false;


 //              } catch (RuntimeException e) {
 //                  e.printStackTrace();
 //                  start_stop = false;
 //                  //  }
 //                  // SystemClock.sleep(3000);
 //              }
 //          }

 //      }
 //  }
}
