package com.trongan93.imageprocessingadvanced;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.trongan93.imageprocessingadvanced.util.StorgeProcessing;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import com.trongan93.imageprocessingadvanced.ImageProcessing.HistogramProcess;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    Button btnGallery;
    ImageView sourceImage, hstImage;
    Activity mActivity;
    private static int IMG_RESULT = 1;
    String ImageDecode;
    Mat rgba;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        btnGallery = (Button)view.findViewById(R.id.btnLoadImage);
        sourceImage = (ImageView)view.findViewById(R.id.imgSource);
        hstImage = (ImageView)view.findViewById(R.id.imgHistogram);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorgeProcessing.VerifyStoragePermissions(mActivity);
                Intent photoPickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickIntent, IMG_RESULT);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == IMG_RESULT && resultCode == mActivity.RESULT_OK){
                Uri URI = data.getData();
                String[] FILE = {MediaStore.Images.Media.DATA};

                Cursor cursor = mActivity.getContentResolver().query(URI,FILE,null,null,null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmapSourceImage = BitmapFactory.decodeFile(ImageDecode);
                sourceImage.setImageBitmap(bitmapSourceImage);
                Log.d("trongan93","Success set Image Bitmap");

//                Histogram
                //    Image Processing with OpenCV
                rgba = new Mat();
                Utils.bitmapToMat(bitmapSourceImage, rgba);
                Size rgbaSize = rgba.size();

                HistogramProcess histogramProcess = new HistogramProcess(rgba,rgbaSize);
                Bitmap hstBitmap = histogramProcess.GetBitMap();
                hstImage.setImageBitmap(hstBitmap);
            }
        }
        catch (Exception e){
            Toast.makeText(mActivity, "Please try again", Toast.LENGTH_LONG).show();
        }
    }


}
