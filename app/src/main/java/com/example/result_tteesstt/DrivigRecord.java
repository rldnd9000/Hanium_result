package com.example.result_tteesstt;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DrivigRecord extends AppCompatActivity {

    myDBHelper myHelper;
    EditText  dateedt1, dateedt2;
    Button btnSelect,btnDelete;
    SQLiteDatabase sqlDB;
    TextView tex1, accumulate_text1, subNu,selectDate;
    public static Context mContext;
    private LineChart lineChart;
    int MMi,M0l,DDI,intmoi,intddi;
    String strdd1,strmoi;
    boolean startORstop = true;
    //double MoveNu;
    private GraphicalView mChartView;

    ArrayList<Float> list_str2 = new ArrayList<Float>();
    ArrayList<Float> list_str3 = new ArrayList<Float>();
    ArrayList<String> stringDate = new ArrayList<String>();
  // ArrayList<Double> list_strMove = new ArrayList<Double>();
  // ArrayList<String> list_strDate = new ArrayList<String>();
  // ArrayList<Double> MoveNuArray = new ArrayList<Double>();
    ArrayList<Double> list_strMove = new ArrayList<Double>();
    ArrayList<String> list_strDate = new ArrayList<String>();
    ArrayList<Double> MoveNuArray = new ArrayList<Double>();
    ArrayList<String> DateArray = new ArrayList<String>();

    long _timeMillis = System.currentTimeMillis();

    String yyyy = DateFormat.format("yyyy", _timeMillis).toString();
    String MM = DateFormat.format("MM", _timeMillis).toString();
    String dd = DateFormat.format("dd", _timeMillis).toString();
    String HH = DateFormat.format("HH", _timeMillis).toString();
    String mm = DateFormat.format("mm", _timeMillis).toString();
    String ss = DateFormat.format("ss", _timeMillis).toString();

    Date time = new Date();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving_record);
        setTitle("주행기록");
        mContext = this;


        final Calendar cal = Calendar.getInstance();

        btnSelect = (Button) findViewById(R.id.btnSelect);
       // btnDelete = (Button) findViewById(R.id.btnDelete);
        //btnSelect2 = (Button) findViewById(R.id.btnSelect2);
        dateedt1 = (EditText) findViewById(R.id.dateedt1);
        dateedt2 = (EditText) findViewById(R.id.dateedt2);
        subNu = (TextView) findViewById(R.id.subNu);
        selectDate = (TextView) findViewById(R.id.selectDate);


        myHelper = new myDBHelper(this);


        SharedPreferences pref = getSharedPreferences("hi", MODE_PRIVATE);
        final String nuNu1 = pref.getString("NU1", "");
       // String n = nuNu1.substring(8, 11);




        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sqlDB = myHelper.getReadableDatabase();
                    Cursor cursor,cursor1;
                    selectDate.setVisibility(View.INVISIBLE);

                    String s1 = dateedt1.getText().toString();
                    String s2 = dateedt2.getText().toString();
                 //  String d1 = s1.replace("-", "");
                 //  String d2 = s2.replace("-", "");
                 //  int f1 = Integer.parseInt(d1);
                 //  int f2 = Integer.parseInt(d2);
                    //   String sq = s2.replace("0","");
                    //   String s3 = s1.replace(":","");


                //  SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
                //   Date newDate_n1 = format1.parse(s1);
              //     Date newDate_n2 = format1.parse(s2);
               //    String newDate_use1 = format1.format(newDate_n1);
              //     String newDate_use2 = format1.format(newDate_n2);
              //     Date date1 = format1.parse(s1);

              //   SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
              //  // Date currentTime = new Date ( );
              //   String sdate = formatter.format(s1);
              //     Date dateTime = formatter.parse(sdate);
             //  SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");
             //  TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
             //  df.setTimeZone(tz);
             //  String curTime=df.format(s1);
             //  Log.d("d","커서 데이터: " +curTime);


                    cursor = sqlDB.rawQuery("SELECT * FROM skwDB6 WHERE skwYMD BETWEEN '" + s1 + "' AND '" + s2 + "';", null);
                    ArrayList<Double> list_str1 = new ArrayList<Double>();
                    ArrayList<String > list_str2 = new ArrayList<String >();
                    double MoveNu = 0;

                    int j = 1;
                    double arrayResult = 0;
                    int k =0;
                    // String strmemotitle = "메모 제목" + "\r\n" + "--------" + "\r\n";

                    list_strDate.clear();
                    list_strMove.clear();
                    MoveNuArray.clear();
                    DateArray.clear();

                    //int p = 0;
                    int p =0;
                    while (startORstop) {
                        if (cursor.moveToNext()) {
                            String strYMD1 = cursor.getString(p);
                            list_strMove.add(cursor.getDouble(1));
                            list_strDate.add(strYMD1);
                            Log.d("d","for문이 도는지 확인");
                        }
                        else {
                            list_strDate.add("0");
                            startORstop = false;
                        }

                    }

                    int datesize = list_strDate.size();
                        for (int i = 0; i < datesize; i++) {
                            Log.d("d","for문 date값 " +list_strDate.get(i));
                            if (list_strDate.get(i).equals("0")){
                                Log.d("d","값이 없다 ");
                                Log.d("d","Ma사이즈:" + MoveNuArray.size());
                                Log.d("d","Da사이즈:" + DateArray.size());
                                RecordGraph();
                            }

                            if (list_strDate.get(i).equals(list_strDate.get(j))){
                                MoveNu +=list_strMove.get(i);
                                Log.d("d","값이 들어오는지 확인 " +MoveNu);
                                j++;
                            }
                            else if (list_strDate.get(i).equals(list_strDate.get(i-1))){
                                MoveNu +=list_strMove.get(i);
                                MoveNuArray.add(MoveNu);
                                DateArray.add(list_strDate.get(i));
                                MoveNu = 0.0;
                                j++;
                                Log.d("d","movearray22: ");
                            }
                        }
                    cursor.close();
                    sqlDB.close();
                } catch (Exception e) {
                }
            }
        });


        findViewById(R.id.btn_date_picker_dialog1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(DrivigRecord.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        String yyyy1 = String.format("%d", year);
                        int intyyyy1 = Integer.parseInt(yyyy1);
                        String MM1 = String.format("%d", month + 1);
                        int intMM1 = Integer.parseInt(MM1);
                        String dd1 = String.format("%d", date);
                        int intdd1 = Integer.parseInt(dd1);
                        int yyyyi = Integer.parseInt(yyyy1);


                        if (intMM1 ==10 ) {
                            strmoi = String.valueOf(intMM1);
                            //return;
                        }
                        else if (intMM1 ==11){
                            strmoi = String.valueOf(intMM1);
                        }
                        else if (intMM1 ==12){
                            strmoi = String.valueOf(intMM1);
                        }
                        else {
                            String strMM1 = String.valueOf(intMM1);
                            strmoi = "0" + strMM1;
                        }
                        if (intdd1 ==1 ) {
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                            // return;
                        }
                        else if (intdd1 ==2){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==3){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==4){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==5){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==6){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==7){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==8){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==9){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else {
                            strdd1 = String.valueOf(intdd1);

                        }

                        int ddi = Integer.parseInt(dd1);
                        String psmoi = String.valueOf(M0l);
                        String stry = String.valueOf(yyyyi);
                        dateedt1.setText(yyyyi + "-" + strmoi + "-" + strdd1);

                        String stryymmdd = stry + "-" + strmoi + "-" + strdd1;

                        Log.d("d","커서 데이터2: " +stryymmdd);


                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();

            }
        });


        findViewById(R.id.btn_date_picker_dialog2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(DrivigRecord.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        String yyyy1 = String.format("%d", year);
                        int intyyyy1 = Integer.parseInt(yyyy1);
                        String MM1 = String.format("%d", month + 1);
                        int intMM1 = Integer.parseInt(MM1);
                        String dd1 = String.format("%d", date);
                        int intdd1 = Integer.parseInt(dd1);


                        int yyyyi = Integer.parseInt(yyyy1);
                        if (intMM1 ==10 ) {
                            strmoi = String.valueOf(intMM1);
                            // return;
                        }
                        else if (intMM1 ==11){
                            strmoi = String.valueOf(intMM1);
                        }
                        else if (intMM1 ==12){
                            strmoi = String.valueOf(intMM1);
                        }
                        else {
                            String strMM1 = String.valueOf(intMM1);
                            strmoi = "0" + strMM1;
                            //  Log.d("d","먼스: " +intmoi);
                            // M0l = intmoi;
                        }
                        if (intdd1 ==1 ) {
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                            // return;
                        }
                        else if (intdd1 ==2){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==3){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==4){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==5){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==6){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==7){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==8){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else if (intdd1 ==9){
                            String strddi = String.valueOf(intdd1);
                            strdd1 = "0" + strddi;
                        }
                        else {
                            strdd1 = String.valueOf(intdd1);

                        }

                        int ddi = Integer.parseInt(dd1);
                        String psmoi = String.valueOf(M0l);

                        //Log.d("d","커서 데이터2: " +psmoi);
                        dateedt2.setText(yyyyi + "-" + strmoi + "-" + strdd1);
                        String stryymmdd = yyyyi + "-" + strmoi + "-" + strdd1;
                        Log.d("d","커서 데이터3: " +stryymmdd);


                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                dialog.getDatePicker().setMaxDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();

            }
        });
    }
    public void RecordGraph() {
        double size1 = 0.0,size2 = 0.0, size3 = 0.0;
        for (int i = 0; i<MoveNuArray.size(); i++) {
            if (MoveNuArray.get(i) > size1){
                size1 = MoveNuArray.get(i);
            }
        }
        size3=size1;
        for (int i = 0; i<MoveNuArray.size(); i++) {
            if (MoveNuArray.get(i) < size3){
                size3 = MoveNuArray.get(i);
            }
        }


            XYSeries expenseSeries = new XYSeries("");


            for (int i = 0; i < MoveNuArray.size(); i++) {


                expenseSeries.add(i, Math.round(MoveNuArray.get(i)));


            }



            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();


            dataset.addSeries(expenseSeries);

            XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();

            expenseRenderer.setColor(Color.parseColor("#FF9436"));

            expenseRenderer.setFillPoints(true);

            expenseRenderer.setChartValuesTextSize(30);
            expenseRenderer.setChartValuesTextAlign(Paint.Align.RIGHT);


            expenseRenderer.setLineWidth(10);//

            expenseRenderer.setDisplayChartValues(true);

            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

            multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);

            multiRenderer.setXLabels(0);

            multiRenderer.setXTitle("Year 2019");


            /***

             * Customizing graphs

             */

            //setting text size of the title

            multiRenderer.setChartTitleTextSize(28);

            multiRenderer.setAxisTitleTextSize(24);

            //setting text size of the graph lable

            multiRenderer.setLabelsTextSize(24);

            //setting zoom buttons visiblity

            multiRenderer.setZoomButtonsVisible(false);

            //setting pan enablity which uses graph to move on both axis

            multiRenderer.setPanEnabled(false, false);

            //setting click false on graph

            multiRenderer.setClickEnabled(false);

            //setting zoom to false on both axis

            multiRenderer.setZoomEnabled(false, false);

            //setting lines to display on y axis

            multiRenderer.setShowGridY(false);

            //setting lines to display on x axis

            multiRenderer.setShowGridX(false);

            //setting legend to fit the screen size

            multiRenderer.setFitLegend(true);

            //setting displaying line on grid

            multiRenderer.setShowGrid(false);

            //setting zoom to false

            multiRenderer.setZoomEnabled(false);

            //setting external zoom functions to false

            multiRenderer.setExternalZoomEnabled(false);

            //setting displaying lines on graph to be formatted(like using graphics)

            multiRenderer.setAntialiasing(true);

            //setting to in scroll to false

            multiRenderer.setInScroll(false);

            //setting to set legend height of the graph

            multiRenderer.setLegendHeight(30);

            //setting x axis label align

            multiRenderer.setXLabelsAlign(Paint.Align.CENTER);

            //setting y axis label to align

            multiRenderer.setYLabelsAlign(Paint.Align.LEFT);

            //setting text style

            multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);

            //setting no of values to display in y axis

            multiRenderer.setYLabels(50);

            // setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.

            // if you use dynamic values then get the max y value and set here

            multiRenderer.setYAxisMax(size1 + 5.0);

            multiRenderer.setYAxisMin(size3 - 5.0);
            //setting used to move the graph on xaxiz to .5 to the right

            multiRenderer.setXAxisMin(-1.0);

            //setting max values to be display in x axis

            multiRenderer.setXAxisMax(MoveNuArray.size());  //x축의 갯수  기록이 있는 MoveArray를 넣는게 맞다

            //setting bar size or space between two bars

            multiRenderer.setBarSpacing(1.5);


            //Setting background color of the graph to transparent

            multiRenderer.setBackgroundColor(Color.TRANSPARENT);

            //Setting margin color of the graph to transparent

            multiRenderer.setMarginsColor(19);

            multiRenderer.setApplyBackgroundColor(true);

            //setting the margin size for the graph in the order top, left, bottom, right

            multiRenderer.setMargins(new int[]{30, 30, 30, 30});

            for (int i = 0; i < DateArray.size(); i++) {

                String SubDate = DateArray.get(i).substring(5);
                multiRenderer.addXTextLabel(i, SubDate);

            }


            multiRenderer.addSeriesRenderer(expenseRenderer);


            LinearLayout layout = (LinearLayout) findViewById(R.id.chart_bar);

            //remove any views before u paint the chart

            layout.removeAllViews();

            //drawing bar chart

            mChartView = ChartFactory.getBarChartView(this, dataset, multiRenderer, BarChart.Type.DEFAULT);


            layout.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,

                    LinearLayout.LayoutParams.FILL_PARENT));

        }
    }


class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context) {
        super(context, "skwDB6", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE skwDB6 ( skwYMD date,ride double PRIMARY KEY );");  //총 2개
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS skwDB6");
        onCreate(db);
    }
}





            //  while (true){

            // 메인에서 생성된 Handler 객체의 sendEmpryMessage 를 통해 Message 전달








// DB 에 받아온 값을 저장을 하고 저장된 값 들을 레코드 식으로 검색하지않고 다 더해서 토탈값으로 보이게 하면됨.
// 처음 텍스트창에는 여태 주행했던 거리를 나오게, 시작 날짜와 끝 날짜로 찾을때는 그 값만 보이게

//=====
// 디스턴스값을 구했을즉시 db에 담기게끔

//=====
//insert 구문 date1 date2 로 넣기 select 에 date1 date2 범위 값으로

// 커서로 select 해당하는 칼럼들만 모아옴 거기서 moveToNext()로 한줄씩 커서가 가르키고 getInt()로 데이터를
//뽑아올 수 있음 그렇게 날짜 분류해서 주별, 일별 그래프 산출을 구현하자

//새로만든 그래프 값 만들기 공식에 String 값으로 읽게 잘 구현하자
//DB에 String 으로 저장해도 안에서는 date형식이기에 뽑아쓸때는 String 으로 바로 못가져옴 그전에 변환해서 받아오자