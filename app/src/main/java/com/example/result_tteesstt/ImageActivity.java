package com.example.result_tteesstt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ImageActivity extends AppCompatActivity {

    ImageView img1;
    TextView text1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageactivity);
        setTitle("개발 중");

        TextView text1 = (TextView)findViewById(R.id.text1);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("hi"); /*String형*/ //인텐트로 내 앨범폴더 주소를 String으로 받는다.
        text1.setText(name + " ");

        File imgFile = new File(Environment.getExternalStorageDirectory()+"/camtest/"+name);  //받는 이름이 28번째 까지 잘라져있어서 다시 28번째까지 생성

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath()); //이미지까지 출력 확인, 정확한 주소가 있어야 파고들어서 이미지를 가져올 수 있음

            ImageView img1 = (ImageView) findViewById(R.id.img1);

            img1.setImageBitmap(myBitmap);

        }


    }
}
