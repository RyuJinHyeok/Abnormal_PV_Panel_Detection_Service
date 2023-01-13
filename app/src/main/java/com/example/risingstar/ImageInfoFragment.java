package com.example.risingstar;

import android.graphics.Color;
import android.os.Bundle;

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
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;


public class ImageInfoFragment extends Fragment {

    public static MapView mapView;

    private ImageView imageView;
    private Button button;
    private TextView defaultView;

    private static String NUM = "fragment_num";
    private static String METADATA = "MetaData";

    private int num;
    private MetaData mData;

    public ImageInfoFragment() {
        // Required empty public constructor
    }

    public static ImageInfoFragment newInstance(int num, MetaData data) {
        ImageInfoFragment fragment = new ImageInfoFragment();
        Bundle args = new Bundle();
        args.putInt(NUM, num);
        args.putSerializable(METADATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            num = getArguments().getInt(NUM);
            mData = (MetaData) getArguments().getSerializable(METADATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image_info, container, false);

        imageView = v.findViewById(R.id.image);
        button = v.findViewById(R.id.button);

        button.setOnClickListener(view -> {
            movePosition(mData.getCameraLocation().getLatitude(), mData.getCameraLocation().getLongitude());
        });

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.image1));

        return v;
    }

    // 입력한 위도 경도로 카메라 이동하는 함수
    private void movePosition(double latitude, double longitude) {

        // 지도에 표시된 요소들 삭제
        mapView.removeAllPOIItems();
        mapView.removeAllPolylines();

        MapPoint testPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

        // 마커 띄우기 (test)
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("hello");
        marker.setMapPoint(testPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);

        // 경계선 그리기
        drawPolyLine();

        // 카메라 이동
        mapView.moveCamera(CameraUpdateFactory.newMapPoint(testPoint, MapView.MIN_ZOOM_LEVEL));

        // 카메라 이동 시 현재 위치 트랙킹 모드 off
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }

    // 경계선 그리기
    private void drawPolyLine() {
        MapPolyline polyline = new MapPolyline();
        polyline.setLineColor(Color.argb(255, 255, 51, 0)); // Polyline 컬러 지정.

        // Polyline 좌표 지정.
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(mData.getVertex().getRT().getLatitude(), mData.getVertex().getRT().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(mData.getVertex().getLT().getLatitude(), mData.getVertex().getLT().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(mData.getVertex().getLB().getLatitude(), mData.getVertex().getLB().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(mData.getVertex().getRB().getLatitude(), mData.getVertex().getRB().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(mData.getVertex().getRT().getLatitude(), mData.getVertex().getRT().getLongitude()));

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline);
    }
}