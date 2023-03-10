package com.example.risingstar;

import android.app.Dialog;
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

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

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
    private Button moveBtn, zoomBtn;

    private static String DATAINFO = "DataInfo";

    private DataInfo dataInfo;

    public ImageInfoFragment() {
        // Required empty public constructor
    }

    public static ImageInfoFragment newInstance(DataInfo d) {
        ImageInfoFragment fragment = new ImageInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(DATAINFO, d);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dataInfo = (DataInfo) getArguments().getSerializable(DATAINFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image_info, container, false);


        imageView = v.findViewById(R.id.image);
        moveBtn = v.findViewById(R.id.moveBtn);
        zoomBtn = v.findViewById(R.id.zoomBtn);

        // ????????? ????????????
        imageView.setImageBitmap(loadImage(dataInfo.getFileName()));

        moveBtn.setOnClickListener(view -> {
            movePosition();
        });

        zoomBtn.setOnClickListener(view -> {

            // ?????? ?????? formatting
            String IRName = dataInfo.getFileName();

            int num = Integer.parseInt(IRName.substring(2, 5)) + 1;
            String RGBName;

            if (num / 10 == 0) RGBName = "00" + num;    // ???????????? ??????
            else if (num / 100 == 0) RGBName = "0" + num;   // ???????????? ??????
            else RGBName = "" + num;    // ???????????? ??????

            RGBName = "00" + RGBName + ".jpg";


            // dialog ?????????
            showDialog(IRName, RGBName);
        });

        return v;
    }

    // ????????? ?????? ????????? ????????? ???????????? ??????
    private void movePosition() {

        // ????????? ????????? ????????? ??????
        mapView.removeAllPolylines();

        // ????????? ????????? ??? ????????? ??????
        drawPolyLine();

        // ????????? ?????? ??? ?????? ?????? ????????? ?????? off
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
    }

    // ????????? ????????? ??? ????????? ??????
    private void drawPolyLine() {
        MapPolyline polyline = new MapPolyline();
        polyline.setLineColor(Color.argb(255, 255, 51, 0)); // Polyline ?????? ??????.

        // Polyline ?????? ??????.
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dataInfo.getRT().getLatitude(), dataInfo.getRT().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dataInfo.getLT().getLatitude(), dataInfo.getLT().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dataInfo.getLB().getLatitude(), dataInfo.getLB().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dataInfo.getRB().getLatitude(), dataInfo.getRB().getLongitude()));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(dataInfo.getRT().getLatitude(), dataInfo.getRT().getLongitude()));

        // Polyline ????????? ?????????.
        mapView.addPolyline(polyline);

        // ???????????? ??????????????? ???????????? Polyline??? ?????? ???????????? ??????
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }

    // ????????? ????????? ????????? ??????
    private Bitmap loadImage(String imageString) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/HackathonData");

        FileInputStream fis = null;
        BufferedInputStream buf = null;
        Bitmap bitmap = null;
        String name = myDir + "/" + imageString;
        try {
            fis = new FileInputStream(name);
            buf = new BufferedInputStream(fis);
            bitmap = BitmapFactory.decodeStream(buf);
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

        return bitmap;
    }

    // dialog ?????????
    private void showDialog(String IRName, String RGBName) {

        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.dialog_layout);

        SubsamplingScaleImageView IRView = dialog.findViewById(R.id.IRView);
        SubsamplingScaleImageView RGBView = dialog.findViewById(R.id.RGBView);

        IRView.setImage(ImageSource.bitmap(loadImage(IRName)));
        RGBView.setImage(ImageSource.bitmap(loadImage(RGBName)));

        dialog.show();
    }
}