package com.example.risingstar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;

import android.content.pm.PackageManager;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.risingstar.databinding.ActivityMainBinding;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener {

    private ActivityMainBinding binding;

    private MapView mapView;
    private ViewGroup mapViewContainer;
    private RadioGroup radioGroup;
    private RadioButton normalBox, abnormalBox;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    private int normalDataCnt = 0, abnormalDataCnt = 0;
    private MetaData[] normalData, abnormalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 뷰 바인딩
        mapViewContainer = binding.mapView;
        radioGroup = binding.radioGroup;
        normalBox = binding.normal;
        abnormalBox = binding.abnormal;

        // 권한 확인 및 동의
        permissionCheck();

        //지도 띄움
        mapView = new MapView(this);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setMapType(MapView.MapType.Satellite);

        // 현재 위치 띄우기
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        // 데이터 불러오기
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

        // 변수 설정
        ImageInfoFragment.mapView = mapView;

        // viewPager 띄우기
        setViewPager(true);
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            setViewPager(normalBox.isChecked());
        });
    }

    // csv 파일 읽어옴
    private void loadData() throws IOException, CsvException {

        AssetManager assetManager = this.getAssets();
        InputStream inputStream = assetManager.open("sample.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));

        List<String[]> allContent = csvReader.readAll();

        normalData = new MetaData[allContent.size()];
        abnormalData = new MetaData[allContent.size()];

        for(String content[] : allContent){
            StringBuilder sb = new StringBuilder("");

            if (content[0].equals("1")) {
                normalData[normalDataCnt++] =
                        new MetaData(
                                new Location(Double.parseDouble(content[1]), Double.parseDouble(content[2])),
                                new Vertex(
                                        new Location(Double.parseDouble(content[3]), Double.parseDouble(content[4])),
                                        new Location(Double.parseDouble(content[5]), Double.parseDouble(content[6])),
                                        new Location(Double.parseDouble(content[7]), Double.parseDouble(content[8])),
                                        new Location(Double.parseDouble(content[9]), Double.parseDouble(content[10]))
                                )
                        );
            } else {
                abnormalData[abnormalDataCnt++] =
                        new MetaData(
                                new Location(Double.parseDouble(content[1]), Double.parseDouble(content[2])),
                                new Vertex(
                                        new Location(Double.parseDouble(content[3]), Double.parseDouble(content[4])),
                                        new Location(Double.parseDouble(content[5]), Double.parseDouble(content[6])),
                                        new Location(Double.parseDouble(content[7]), Double.parseDouble(content[8])),
                                        new Location(Double.parseDouble(content[9]), Double.parseDouble(content[10]))
                                )
                        );
            }
        }
    }

    // viewPager 띄우기
    private void setViewPager(boolean isNormalChecked) {

        // 초기화
        binding.defaultView.setVisibility(View.VISIBLE);

        // 데이터 개수 확인
        int cnt = isNormalChecked ? normalDataCnt : abnormalDataCnt;

        // 데이터가 있으면 데이터를 viewPager를 이용하여 띄운다
        if (cnt != 0) {
            binding.defaultView.setVisibility(View.INVISIBLE);

            // viewPager, pagerAdapter 설정
            viewPager = binding.viewPager;
            pagerAdapter = new MyAdapter(this, cnt, isNormalChecked ? normalData : abnormalData);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            viewPager.setCurrentItem(0);
            viewPager.setOffscreenPageLimit(cnt);
        }

        // viewPager 스크롤 이벤트 처리
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    viewPager.setCurrentItem(position);
                }
            }
        });
    }

    // 권한 체크
    private void permissionCheck() {
        // 권한 ID 가져옴
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);

        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int permission3 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        // 권한이 열려있는지 확인
        if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED || permission3 == PackageManager.PERMISSION_DENIED) {
            // 마쉬멜로우 이상버전부터 권한을 물어본다
            if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                requestPermissions(
                        new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1000);
            }
            return;
        }
    }

    // 권한 체크 이후로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
        if (requestCode == 1000) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result == false) {
                finish();
            }
        }
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}