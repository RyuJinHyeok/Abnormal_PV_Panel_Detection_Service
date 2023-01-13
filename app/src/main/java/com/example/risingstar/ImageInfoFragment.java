package com.example.risingstar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class ImageInfoFragment extends Fragment {

    public static MapView mapView;

    private ImageView imageView;
    private Button moveBtn;

    private static String NUM = "fragment_num";
    private static String DATAINFO = "DataInfo";

    private int num;
    private DataInfo dataInfo;

    public ImageInfoFragment() {
        // Required empty public constructor
    }

    public static ImageInfoFragment newInstance(int num, DataInfo d) {
        ImageInfoFragment fragment = new ImageInfoFragment();
        Bundle args = new Bundle();
        args.putInt(NUM, num);
        args.putSerializable(DATAINFO, d);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            num = getArguments().getInt(NUM);
            dataInfo = (DataInfo) getArguments().getSerializable(DATAINFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image_info, container, false);


        imageView = v.findViewById(R.id.image);
        moveBtn = v.findViewById(R.id.moveBtn);

        moveBtn.setOnClickListener(view -> {
            movePosition();
        });

        // 이미지 불러오기
        loadImage(dataInfo.getFileName());

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
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dataInfo.getRT().getLatitude(), dataInfo.getRT().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dataInfo.getLT().getLatitude(), dataInfo.getLT().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dataInfo.getLB().getLatitude(), dataInfo.getLB().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dataInfo.getRB().getLatitude(), dataInfo.getRB().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dataInfo.getRT().getLatitude(), dataInfo.getRT().getLongitude()));

        // Polyline 지도에 올리기.
        mapView.addPolyline(polyline);

        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }

    private void loadImage(String imageString) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/HackathonData");

        FileInputStream fis = null;
        BufferedInputStream buf = null;
        String name = myDir + "/" + imageString;
        try {
            fis = new FileInputStream(name);
            buf = new BufferedInputStream(fis);

            Bitmap bitmap = BitmapFactory.decodeStream(buf);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}