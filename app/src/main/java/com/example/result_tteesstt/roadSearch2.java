package com.example.result_tteesstt;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static org.opencv.core.CvType.CV_8UC3;

import static android.speech.tts.TextToSpeech.ERROR;


public class roadSearch2 extends AppCompatActivity
        implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback ,CameraBridgeViewBase.CvCameraViewListener2 {


    private GoogleMap mMap;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;


    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소


    Location mCurrentLocatiion;
    LatLng currentPosition;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;


    private View mLayout, mmLayout;
    TextView SagoSpeed1, road1, TTS_Text;
    TextView Loc1;
    int z = 1;
    double distance5 = 0.0, distance6 = 0.0, dis1 = 0.0, dis;
    Location LocatA = new Location("LocatA");

    public MainActivity MA;
    double distanceNu, distance1, distanSub;
    public DrivigRecord DR;
    private CameraPreview mCameraPreview;
    Button btnNam, btnh1, playingBtn;
    String label = "Hi";
     private SurfaceView surfaceView;
    //  private static final String TAG = "android_camera_example";
    //  private static final int PERMISSIONS_REQUEST_CODE = 100;
    // String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA,
    //      Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK;
    private TextToSpeech tts;
    myDBHelper dbMgr = new myDBHelper(this);

    long _timeMillis = System.currentTimeMillis();

    String yyyy = DateFormat.format("yyyy", _timeMillis).toString();
    String MM = DateFormat.format("MM", _timeMillis).toString();
    String dd = DateFormat.format("dd", _timeMillis).toString();
    String HH = DateFormat.format("HH", _timeMillis).toString();
    String mm = DateFormat.format("mm", _timeMillis).toString();
    String ss = DateFormat.format("ss", _timeMillis).toString();

    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");

    Date time = new Date();


    Intent manboService;
    BroadcastReceiver receiver;

    boolean flag = true;
    String serviceData;
    //TextView countText;

    //private static final String TAG = "opencv";

    private Net net;
    private static final String[] classNames = {"background",
            "aeroplane", "bicycle", "bird", "boat",
            "bottle", "bus", "car", "cat", "chair",
            "cow", "diningtable", "dog", "horse",
            "motorbike", "person", "pottedplant",
            "sheep", "sofa", "train", "tvmonitor"};
    private CameraBridgeViewBase mOpenCvCameraView;

    private static String copyFile(String filename, Context context) {
        String baseDir = Environment.getExternalStorageDirectory().getPath();
        String pathDir = baseDir + File.separator + filename;

        AssetManager assetManager = context.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            Log.d(TAG, "copyFile :: 다음 경로로 파일복사 " + pathDir);
            inputStream = assetManager.open(filename);
            outputStream = new FileOutputStream(pathDir);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            inputStream = null;
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (Exception e) {
            Log.d(TAG, "copyFile :: 파일 복사 중 예외 발생 " + e.toString());
        }

        return pathDir;

    }


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("개발 중");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 화면 켜진 상태를 유지합니다.
        // setContentView(R.layout.sago2);


        //     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //     getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        //             WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.sago2);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(0); // front-camera(1),  back-camera(0)

        mmLayout = findViewById(R.id.layout_main);

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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(roadSearch2.this);
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
                                    Toast.makeText(roadSearch2.this, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
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
        }

        mLayout = findViewById(R.id.layout_main1);
         surfaceView = findViewById(R.id.camera_preview_main1);


        // 런타임 퍼미션 완료될때 까지 화면에서 보이지 않게 해야합니다.
         surfaceView.setVisibility(View.GONE);

        SagoSpeed1 = (TextView) findViewById(R.id.sagoSpped1);
        road1 = (TextView) findViewById(R.id.road1);
        Loc1 = (TextView) findViewById(R.id.loc1);
        // btnNam = (Button) findViewById(R.id.btnNam);
        // btnh1 = (Button) findViewById(R.id.btnh1);
        TTS_Text = (TextView) findViewById(R.id.TTS_Text);
        //distance5 = 0.0;
        //  playingBtn = (Button)findViewById(R.id.btnStopService);

      //  manboService = new Intent(this, StepCheckService.class);
      //  receiver = new PlayingReceiver();


        Log.d(TAG, "onCreate");


        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // final double loc1 = 0.0;

        //  btnNam.setOnClickListener(new View.OnClickListener() {
        //      @Override
        //      public void onClick(View view) {
        //          try {
        //              // Thread.currentThread() -> 현재 실행중인 쓰레드 반환
        //              mCameraPreview.takePicture();
        //          } catch (RuntimeException e) {
        //              e.printStackTrace();
//
        //          }
        //      }
        //  });

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


            if (cameraPermission == PackageManager.PERMISSION_GRANTED
                    && writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                startCamera();
                Log.d(TAG, "1차 startcamera");


            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Snackbar.make(mLayout, "이 앱을 실행하려면 카메라와 외부 저장소 접근 권한이 필요합니다.",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            ActivityCompat.requestPermissions(roadSearch2.this, REQUIRED_PERMISSIONS,
                                    PERMISSIONS_REQUEST_CODE);
                        }
                    }).show();


                } else {
                    // 2. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                    // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);
                }

            }

        } else {

            final Snackbar snackbar = Snackbar.make(mLayout, "디바이스가 카메라를 지원하지 않습니다.",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        //  btnh1.setOnClickListener(new View.OnClickListener() {
        //      @Override
        //      public void onClick(View view) {
        //          // editText에 있는 문장을 읽는다.
        //          tts.setSpeechRate(-5.0f);
        //          tts.speak(TTS_Text.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
        //      }
        //  });
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


 //     if (flag) {
 //         // TODO Auto-generated method stub
 //         try {

 //             IntentFilter mainFilter = new IntentFilter("make.a.yong.manbo");

 //             registerReceiver(receiver, mainFilter);
 //             startService(manboService);
 //         } catch (Exception e) {
 //             // TODO: handle exception
 //             Toast.makeText(getApplicationContext(), e.getMessage(),
 //                     Toast.LENGTH_LONG).show();
 //         }
 //     } else {

 //         playingBtn.setText("Go !!");

 //         // TODO Auto-generated method stub
 //         try {

 //             unregisterReceiver(receiver);

 //             stopService(manboService);

 //             // txtMsg.setText("After stoping Service:\n"+service.getClassName());
 //         } catch (Exception e) {
 //             // TODO: handle exception
 //             Toast.makeText(getApplicationContext(), e.getMessage(),
 //                     Toast.LENGTH_LONG).show();
 //         }
 //     }

 //     flag = !flag;


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");

        mMap = googleMap;

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();


        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mmLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions(roadSearch2.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }


        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d(TAG, "onMapClick :");
            }
        });
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());


                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);

                handler.sendEmptyMessage(0);


                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);


                mCurrentLocatiion = location;
            }


        }

    };


    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        } else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);


            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap != null)
                mMap.setMyLocationEnabled(true);

        }

        onCameraPermissionGranted();


    }


    @Override
    protected void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        String locstar1;
        locstar1 = markerOptions.getTitle();
        Loc1.setText(locstar1.substring(5));


        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);

    }


    public void setDefaultLocation() {


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;

    }


    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            onCameraPermissionGranted();
            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
                startCamera();
                Log.d(TAG, "2차 startcamera");
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mmLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                } else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mmLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(roadSearch2.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "onResume :: Internal OpenCV library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "onResum :: OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    public void onDestroy() {
        super.onDestroy();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

        String pro = copyFile("MobileNetSSD_deploy.prototxt.txt", this);
        String caff = copyFile("MobileNetSSD_deploy.caffemodel", this);
        net = Dnn.readNetFromCaffe(pro, caff);
    }

    @Override
    public void onCameraViewStopped() {

    }


    public float resize(Mat img_src, Mat img_resize, int resize_width) {

        float scale = resize_width / (float) img_src.cols();
        if (img_src.cols() > resize_width) {
            int new_height = Math.round(img_src.rows() * scale);
            Imgproc.resize(img_src, img_resize, new Size(resize_width, new_height));
        } else {
            img_resize = img_src;
        }
        return scale;
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        final int IN_WIDTH = 300;
        final int IN_HEIGHT = 300;
        final float WH_RATIO = (float) IN_WIDTH / IN_HEIGHT;
        final double IN_SCALE_FACTOR = 0.007843;
        final double MEAN_VAL = 127.5;
        final double THRESHOLD = 0.2;

        // Get a new frame
        Mat frame = inputFrame.rgba();
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);

        Mat frame_resized = new Mat(frame.rows(), frame.cols(), CV_8UC3, new Scalar(255, 255, 255));
        float resizeRatio = resize(frame, frame_resized, 160);


        // Forward image through network.
        Mat blob = Dnn.blobFromImage(frame_resized, IN_SCALE_FACTOR,
                new Size(IN_WIDTH, IN_HEIGHT),
                new Scalar(MEAN_VAL, MEAN_VAL, MEAN_VAL), /*swapRB*/false, /*crop*/false);
        net.setInput(blob);
        Mat detections = net.forward();
        int cols = frame_resized.cols();
        int rows = frame_resized.rows();
        detections = detections.reshape(1, (int) detections.total() / 7);




        for (int i = 0; i < detections.rows(); ++i) {
            double confidence = detections.get(i, 2)[0];
            if (confidence > THRESHOLD) {
                int classId = (int) detections.get(i, 1)[0];
                int left = (int) ((detections.get(i, 3)[0] * cols) / resizeRatio);
                int top = (int) ((detections.get(i, 4)[0] * rows) / resizeRatio);
                int right = (int) ((detections.get(i, 5)[0] * cols) / resizeRatio);
                int bottom = (int) ((detections.get(i, 6)[0] * rows) / resizeRatio);


                // Draw rectangle around detected object.
                Imgproc.rectangle(frame, new Point(left, top), new Point(right, bottom),
                        new Scalar(0, 255, 0));
                label = classNames[classId] + ": " + String.format("%,4.2f", confidence);
                ;
                int[] baseLine = new int[1];
                Size labelSize = Imgproc.getTextSize(label, Imgproc.FONT_HERSHEY_SIMPLEX, 2.5, 5, baseLine);
                // Draw background for label.
                Imgproc.rectangle(frame, new Point(left, top - labelSize.height),
                        new Point(left + labelSize.width, top + baseLine[0]),
                        new Scalar(255, 255, 255), Imgproc.FILLED);
                // Write class name and confidence.
                Imgproc.putText(frame, label, new Point(left, top),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 2.5, new Scalar(0, 0, 0));

                if (label.contains("person")) {
                    handler1.sendEmptyMessage(1);
                    try {

                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (label.contains("bicycle")) {
                    handler1.sendEmptyMessage(0);
                    try {

                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

      }
        return frame;
    }

 //  class ThreadEx1_2 implements Runnable {
 //      @Override
 //      public void run() {
 //          if (label.contains("person")) {
 //              try {
 //                  handler1.sendEmptyMessage(0);
 //                  //SystemClock.sleep(3000);
 //                  Thread.sleep(20000);


 //              } catch (Exception e) {
 //                  e.printStackTrace();
 //              }
 //          }
 //      }

 //  }
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tts.setSpeechRate(-5.0f);
                tts.speak(TTS_Text.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
            if (msg.what == 1) {
                tts.setSpeechRate(-5.0f);
                tts.speak(TTS_Text.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }

        }
    };


    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }


    //여기서부턴 퍼미션 관련 메소드
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;


    protected void onCameraPermissionGranted() {
        List<? extends CameraBridgeViewBase> cameraViews = getCameraViewList();
        if (cameraViews == null) {
            return;
        }
        for (CameraBridgeViewBase cameraBridgeViewBase : cameraViews) {
            if (cameraBridgeViewBase != null) {
                cameraBridgeViewBase.setCameraPermissionGranted();
            }
        }
    }

//  @Override
//  protected void onStart() {
//      super.onStart();
//      boolean havePermission = true;
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//          if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED
//                  && checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//              requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
//              havePermission = false;
//          }
//      }
//      if (havePermission) {
//          onCameraPermissionGranted();
//      }
//  }

//  @Override
//  @TargetApi(Build.VERSION_CODES.M)
//  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//      if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//              && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//          onCameraPermissionGranted();
//      }else{
//          showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
//      }
//      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//  }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(roadSearch2.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }

    //public void getDistance(double gps1, double gps2){
    //final double loc1 = 0.0;


    //  Runnable r = new ThreadEx1_3();
    //  Thread t2 = new Thread(r); // 생성자 Thread(Runnable target)
    //  t2.setDaemon(true);
    //  t2.start(); //쓰레드 실행


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                Location locationA = new Location("point A");
                Location locationB = new Location("point B");

                double longitude = location.getLongitude(); //경도
                double latitude = location.getLatitude();   //위도
                //    double altitude = location.getAltitude();   //고도

                double distance;
                locationA.setLongitude(MA.long1);
                locationA.setLatitude(MA.lat1);
                float accuracy = location.getAccuracy();    //정확도
                //    String provider = location.getProvider();   //위치제공자
                //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
                //Network 위치제공자에 의한 위치변화
                //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.

                // double spd1 = Double.parseDouble(String.format("%.3f", location.getSpeed() * 3600 / 1000));
                // SagoSpeed1.setText("\n현재 시속 : " + spd1);


                locationB.setLatitude(latitude);
                locationB.setLongitude(longitude);

                String skk = locationA.toString();

                //   Log.e("lat1 !!: " + locationB, "내 경도 : " + longitude);

                distance = locationA.distanceTo(locationB);   //만약 100   //이어서 120 // 70
                if (distance >= distance1) {  // 만약 측정값이 직전 측정값 보다 크다면
                    double subdis = distance - distance1; //측정값에서 직전 측정값을 뺀다
                   // distanSub = subdis;
                    distanceNu += subdis;  //누적 거리값에 남은 값을 누적더한다.
                    dis = subdis;
                    // return;
                } else if (distance < distance1) { //만약 직전 측정값이 현 측정값 보다 크다면
                    double subdis = distance1 - distance;  // 직전 측정값에서 현 측정값을 뺀다.
                   // distanSub = subdis;
                    distanceNu += subdis;  //남은 값을 누적
                    dis = subdis;
                    //return;
                }
                try {
                    double disNu1 = (Math.round(distanceNu * 100) / 100.0);
                    String strdisNu1 = String.valueOf(disNu1);
                    road1.setText("이동 거리: " + strdisNu1 + "m");
                    double spd1 = Double.parseDouble(String.format("%.1f", location.getSpeed() * 3600 / 1000));
                    SagoSpeed1.setText("현재 시속 : " + spd1 + "k/m");

                    distance1 = distance + 0.0;

                } catch (StringIndexOutOfBoundsException e) {
                    Log.e("lat1 !!: " + e, "정확도 : " + accuracy);
                }

                SQLiteDatabase db = dbMgr.getWritableDatabase();
                try {
                    // Toast.makeText(getApplicationContext(), "들어왔다", 0 + Toast.LENGTH_SHORT).show();

                    String time1 = format1.format(time);

                    db.execSQL("INSERT INTO skwDB6 VALUES ('" + time1 + "'," + dis + ");");
                    db.close();
                    //Toast.makeText(getApplicationContext(), "입력됨" + dis, 0 + Toast.LENGTH_SHORT).show();
                    Log.d("d", "dis값은?: " + dis);
                    return;
                    //    }
                    //   }


                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }

    };


    // class PlayingReceiver extends BroadcastReceiver {

    //     @Override
    //     public void onReceive(Context context, Intent intent) {
    //         Log.i("PlayignReceiver", "IN");
    //         serviceData = intent.getStringExtra("stepService");
    //         //countText.setText(serviceData);
    //         try {
    //             mCameraPreview.takePicture();
    //         }catch (Exception e){
    //             e.printStackTrace();
    //             Toast.makeText(getApplicationContext(), "Playing game"+e.getMessage(), Toast.LENGTH_SHORT).show();
    //         }

    //     }
    // }


    //  class ThreadEx1_3 implements Runnable {
    //      @Override
    //      public void run() {

    //          try {
    //              handler.sendEmptyMessage(0);
    //              String disNu = String.valueOf(distanceNu);
    //              String disNu1 = disNu.substring(0,3);
    //              double disNu2 = Double.parseDouble(disNu1);


    //          }catch (RuntimeException e) {
    //              e.printStackTrace();
    //                }
    //               SystemClock.sleep(3000);
    //          }


    //  }


// Handler handler = new Handler(){
//     @Override
//     public void handleMessage(Message msg) {
//         if(msg.what == 0){   // Message id 가 0 이면
//            // DR.NUinsert();
//             // DB 연결
//             SQLiteDatabase db = dbMgr.getWritableDatabase();
//             try {
//                // Toast.makeText(getApplicationContext(), "들어왔다", 0 + Toast.LENGTH_SHORT).show();

//                 String time1 = format1.format(time);
//                // String time2 = format2.format(time);

//                // String ymd = yyyy + MM + dd;   //ex 20191130
//                // int ymdresult = Integer.parseInt(ymd);

//               //  String strDN = String.valueOf(dis);

//           //    if (MM != "10") {    // 10월이 아니면  실행
//           //        //String MM1 = MM.replace("0", ""); //09월은 9월, 10월은 1월이 되므로 else에서 처리
//           //       // String ymd1 = yyyy + MM1 + dd;  //2019927
//           //       // int ymdresult1 = Integer.parseInt(ymd1);   //ex 20191130
//           //        db.execSQL("INSERT INTO skwDB6 VALUES (" + ymdresult1 + "," + dis + ");");
//           //        db.close();
//           //        //  dateedt.setText(ymd1);
//           //        //  timeedt.setText(skwHm1);
//           //        //btnSelect.callOnClick();
//           //        Toast.makeText(getApplicationContext(), "입력됨1" + ymd1, 0 + Toast.LENGTH_SHORT).show();
//           //        return;
//           //    } else {
//                     db.execSQL("INSERT INTO skwDB6 VALUES ('" +  time1  + "'," + dis + ");");
//                     db.close();
//                     //Toast.makeText(getApplicationContext(), "입력됨" + dis, 0 + Toast.LENGTH_SHORT).show();
//                     Log.d("d","dis값은?: " +dis);
//                     return;
//             //    }
//                 //   }


//             } catch (RuntimeException e) {
//                 e.printStackTrace();
//             }
//             double spd1 = Double.parseDouble(String.format("%.1f", location.getSpeed() * 3600 / 1000));
//             SagoSpeed1.setText("현재 시속 : " + spd1 + "k/m");
//         }
//     }
// };

 //  class PlayingReceiver extends BroadcastReceiver {

 //      @Override
 //      public void onReceive(Context context, Intent intent) {
 //          try {

 //              Log.i("PlayignReceiver", "IN");
 //              serviceData = intent.getStringExtra("stepService");
 //              //countText.setText(serviceData);
 //              mCameraPreview.takePicture();
 //              tts.setSpeechRate(-5.0f);
 //              tts.speak(TTS_Text.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
 //              Toast.makeText(getApplicationContext(), "Playing game", Toast.LENGTH_SHORT).show();
 //          } catch (RuntimeException e) {
 //              e.printStackTrace();
 //              Log.d(TAG, "왜안됩니까 :" + e.getMessage());
 //          }
 //      }
///void startCamera() {
///    mCameraPreview = new CameraPreview(this, this, CAMERA_FACING, mOpenCvCameraView);

///}

 //  }
    void startCamera() {

        // Create the Preview view and set it as the content of this Activity.
        mCameraPreview = new CameraPreview(this, this, CAMERA_FACING, surfaceView);

    }
}














