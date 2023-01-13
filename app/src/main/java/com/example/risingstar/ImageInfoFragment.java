package com.example.risingstar;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;


public class ImageInfoFragment extends Fragment {

    public static MapView mapView;

    private ImageView imageView;
    private Button button;

    private static String NUM = "fragment_num";
    private static String VERTEX = "Vertex";

    private int num;
    private Vertex vertex;

    public ImageInfoFragment() {
        // Required empty public constructor
    }

    public static ImageInfoFragment newInstance(int num, Vertex v) {
        ImageInfoFragment fragment = new ImageInfoFragment();
        Bundle args = new Bundle();
        args.putInt(NUM, num);
        args.putSerializable(VERTEX, v);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            num = getArguments().getInt(NUM);
            vertex = (Vertex) getArguments().getSerializable(VERTEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image_info, container, false);

        imageView = v.findViewById(R.id.image);
        button = v.findViewById(R.id.button);

        button.setOnClickListener(view -> {
            movePosition();
        });

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.image1));

        return v;
    }

    // 입력한 위도 경도로 카메라 이동하는 함수
    private void movePosition() {

        // 지도에 표시된 요소들 삭제
        mapView.removeAllPolylines();

        // 경계선 그리기 및 카메라 이동
        drawPolyLine();

        // 카메라 이동 시 현재 위치 트랙킹 모드 off
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }

    // 경계선 그리기 및 카메라 이동
    private void drawPolyLine() {
        MapPolyline polyline = new MapPolyline();
        polyline.setLineColor(Color.argb(255, 255, 51, 0)); // Polyline 컬러 지정.

        // Polyline 좌표 지정.
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(vertex.getRT().getLatitude(), vertex.getRT().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(vertex.getLT().getLatitude(), vertex.getLT().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(vertex.getLB().getLatitude(), vertex.getLB().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(vertex.getRB().getLatitude(), vertex.getRB().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(vertex.getRT().getLatitude(), vertex.getRT().getLongitude()));

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline);

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }
}