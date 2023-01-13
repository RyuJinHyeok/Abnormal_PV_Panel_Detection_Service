package com.example.risingstar;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class ImageInfoFragment extends Fragment {

    public static MapView mapView;

    private static String NUM = "fragment_num";
    private static String COLOR = "color";
    private static String LATITUDE = "latitude";
    private static String LONGITUDE = "longitude";

    private int num;
    private String color;
    private double latitude, longitude;

    public ImageInfoFragment() {
        // Required empty public constructor
    }

    public static ImageInfoFragment newInstance(int num, String color, Location location) {
        ImageInfoFragment fragment = new ImageInfoFragment();
        Bundle args = new Bundle();
        args.putInt(NUM, num);
        args.putString(COLOR, color);
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
            color = getArguments().getString(COLOR);
            latitude = getArguments().getDouble(LATITUDE);
            longitude = getArguments().getDouble(LONGITUDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image_info, container, false);

        ConstraintLayout viewPage = (ConstraintLayout) v.findViewById(R.id.viewPage);
        TextView text = (TextView) v.findViewById(R.id.text);
        Button button = (Button) v.findViewById(R.id.button);

        viewPage.setBackgroundColor(Color.parseColor(color));
        text.setText("fragment" + num);
        button.setOnClickListener(view -> {
            movePosition(latitude, longitude);
        });

        return v;
    }

    // 입력한 위도 경도로 카메라 이동하는 함수
    private void movePosition(double latitude, double longitude) {

        MapPoint testPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

        // 마커 띄우기 (test)
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("hello");
        marker.setMapPoint(testPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);

        // 카메라 이동
        mapView.moveCamera(CameraUpdateFactory.newMapPoint(testPoint, -2));

        // 카메라 이동 시 현재 위치 트랙킹 모드 off
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }
}