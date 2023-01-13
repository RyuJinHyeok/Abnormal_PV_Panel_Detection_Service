package com.example.risingstar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageInfoFragment extends Fragment {

    public static MapView mapView;

    private ImageView imageView;
    private Button button;
    private Bitmap bitmap;

    private static String NUM = "fragment_num";
    private static String LATITUDE = "latitude";
    private static String LONGITUDE = "longitude";

    private int num;
    private double latitude, longitude;

    public ImageInfoFragment() {
        // Required empty public constructor
    }

    public static ImageInfoFragment newInstance(int num, Location location) {
        ImageInfoFragment fragment = new ImageInfoFragment();
        Bundle args = new Bundle();
        args.putInt(NUM, num);
        args.putDouble(LATITUDE, location.getLatitude());
        args.putDouble(LONGITUDE, location.getLongitude());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            num = getArguments().getInt(NUM);
            latitude = getArguments().getDouble(LATITUDE);
            longitude = getArguments().getDouble(LONGITUDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image_info, container, false);

        imageView = (ImageView) v.findViewById(R.id.image);
        button = (Button) v.findViewById(R.id.button);

        button.setOnClickListener(view -> {
            movePosition(latitude, longitude);
        });

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.image1));

//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    URL url = new URL("http://168.131.30.102:31846/lab/tree/data/voltaic_team1/dataset_300ea/Test_Solar_Image_001.JPG");
//
//
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//                    connection.setDoInput(true);
//                    connection.connect();
//
//                    InputStream is = connection.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(is);
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        thread.start();
//
//        try {
//            thread.join();
//
//            imageView.setImageBitmap(bitmap);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return v;
    }

    // 입력한 위도 경도로 카메라 이동하는 함수
    private void movePosition(double latitude, double longitude) {

        // 지도에 표시된 POI 삭제
        mapView.removeAllPOIItems();

        MapPoint testPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

        // 마커 띄우기 (test)
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("hello");
        marker.setMapPoint(testPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);

        // 카메라 이동
        mapView.moveCamera(CameraUpdateFactory.newMapPoint(testPoint, MapView.MIN_ZOOM_LEVEL));

        // 카메라 이동 시 현재 위치 트랙킹 모드 off
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }
}