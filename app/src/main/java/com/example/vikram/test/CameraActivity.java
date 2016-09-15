package com.example.vikram.test;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements Camera.PreviewCallback{

    private Camera mCamera;
    private MySurfaceView mPreview;
    private int count = 6;
    private TextView time;
    private TextView loc;
    private long sum=0;
    private int ct=0;
    private long begin = 0l;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cv);
        loc = (TextView) findViewById(R.id.loc);
        time = (TextView) findViewById(R.id.time);
        // Create an instance of Camera
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        // Create our Preview view and set it as the content of our activity.

        Camera.Parameters mParameters = mCamera.getParameters();
        List<Camera.Size> sizeList = mCamera.getParameters().getSupportedPreviewSizes();
        Camera.Size bestSize = sizeList.get(sizeList.size()-1);
        mParameters.setPreviewSize(bestSize.width, bestSize.height);
        mCamera.setParameters(mParameters);
        mPreview = new MySurfaceView(this, mCamera, this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        begin = System.nanoTime();
    }
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (count == 0) {
            long curr = System.nanoTime();
            sum += (curr - begin);
            ct +=1;
            time.setText("sum : "+ sum/1000000 + ",ct"+ct);
            begin = curr;
            Camera.Parameters parameters = camera.getParameters();
            int width = parameters.getPreviewSize().width;
            int height = parameters.getPreviewSize().height;
            ByteArrayOutputStream outstr = new ByteArrayOutputStream();
            Rect rect = new Rect(0, 0, width, height);
            YuvImage yuvimage=new YuvImage(data, ImageFormat.NV21,width,height,null);
            yuvimage.compressToJpeg(rect, 96, outstr);
            Bitmap bmp = BitmapFactory.decodeByteArray(outstr.toByteArray(), 0, outstr.size());
            /*boolean[][] red = new boolean[width][height];
            int left = width;
            int right = 0;
            for (int i = 0; i<width; i++){
                for(int j = 0; j<height; j++){
                    int px = bmp.getPixel(i, j);
                    int redValue = Color.red(px);
                    int blueValue = Color.blue(px);
                    int greenValue = Color.green(px);
                    if (greenValue<100 && redValue>140 && blueValue<100){
                        try {
                            if (red[i + 1][j] && red[i + 2][j] && red[i + 3][j]) {
                                red[i][j] = true;
                            }
                        } catch (Exception e){

                        }
                        try {
                            if (red[i - 1][j] && red[i - 2][j] && red[i - 3][j]) {
                                red[i][j] = true;
                            }
                        } catch (Exception e){

                        }
                    }
                    if(red[i][j]){
                        if (i < left){
                            left = i;
                        }
                        if (right>i){
                            right = i;
                        }
                    }
                }
            }
            if (left != width){
                if((width - right + 10) < (left)){
                    loc.setText("Center");
                } else {
                    loc.setText("Right");
                }
            }*/
            count = 6;
        }
        count--;
    }
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

}