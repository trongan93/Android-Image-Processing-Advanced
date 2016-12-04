package com.trongan93.imageprocessingadvanced.ImageProcessing;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Bui Trong An on 12/4/2016.
 * VipLab
 * trongan93@gmail.com
 */

public class MorphologicalProcess {
    Mat sourceMat, grayMat, resutMat;

    public MorphologicalProcess(Mat sourceMat){
        this.sourceMat = sourceMat;
        grayMat = new Mat();
        resutMat = new Mat();
    }
    public void Process(){
//        //Tranform source image to gray image and blur it
//        Imgproc.cvtColor(sourceMat, grayMat, Imgproc.COLOR_BGR2GRAY);
////        Imgproc.blur(grayMat, grayMat, new Size(3,3));
//        Mat edgeMat = new Mat();
//        Imgproc.Canny(grayMat, edgeMat, 100, 200);
//


//        ===================================
        Mat	blurredImage= new Mat();
        Size size = new Size(7,7);
        Imgproc.GaussianBlur(sourceMat, blurredImage, size, 0,0);

        Mat gray = new Mat();
        Imgproc.cvtColor(blurredImage, gray, Imgproc.COLOR_RGB2GRAY);

        Mat xFirstDervative	= new Mat(), yFirstDervative = new Mat();
        int ddepth = CvType.CV_16S;

        Imgproc.Sobel(gray,xFirstDervative, ddepth, 1,0);
        Imgproc.Sobel(gray,yFirstDervative,ddepth, 0,1);

        Mat absXD = new Mat(), absYD = new Mat();
        Core.convertScaleAbs(xFirstDervative, absXD);
        Core.convertScaleAbs(yFirstDervative, absYD);

        Mat edgeImage = new Mat();
        Core.addWeighted(absXD, 0.5, absYD, 0.5, 0, edgeImage);

        resutMat = edgeImage;
    }
    public Bitmap GetBitMap(){
        Bitmap resultBitmap = Bitmap.createBitmap(resutMat.cols(), resutMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resutMat, resultBitmap);
        return resultBitmap;
    }



}
