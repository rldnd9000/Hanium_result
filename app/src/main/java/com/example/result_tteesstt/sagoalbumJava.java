package com.example.result_tteesstt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class sagoalbumJava extends AppCompatActivity {

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sagoalbum);
        setTitle("개발 중");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        listview = (ListView) findViewById(R.id.listview);

        //데이터를 저장하게 되는 리스트
        final List<String> list = new ArrayList<>();

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);

        //리스트뷰의 어댑터를 지정해준다.
        listview.setAdapter(adapter);


        //리스트뷰에 보여질 아이템을 추가

        final File file = new File(Environment.getExternalStorageDirectory() +"/camtest");
        File[] files = file.listFiles();
        String [] titleList = new String [files.length];
        for(int i = 0;i < files.length;i++)
        {
            titleList[i] = files[i].getName();	//루프로 돌면서 어레이에 하나씩 집어 넣습니다.
            File imgFile = new File(Environment.getExternalStorageDirectory() + "/camtest/"+ titleList[i]);
            String strFile = String.valueOf(imgFile);


            list.add(strFile.substring(28)); // 리스트에 사진 하나하나의 이름을 넣는데 주소가 전부 다 보여짐 ex)~~/~~/~~
        }                                    //그러므로 앞에서 28번째 까지 이름을 잘라내서 보여줌
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){

                Intent intent = new Intent(getApplicationContext(), ImageActivity.class); // 다음넘어갈 화면

                String tv = (String)parent.getAdapter().getItem(position);

                intent.putExtra("hi",tv);

                startActivity(intent);
            }
        });


    }

}