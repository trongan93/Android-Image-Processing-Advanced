package com.trongan93.imageprocessingadvanced.ImageProcessing;

import android.graphics.Bitmap;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;

/**
 * Created by Bui Trong An on 12/3/2016.
 * VipLab
 * trongan93@gmail.com
 */

public class HistogramProcess {
    Mat[] histograms;
    public Mat histMatBitmap;
    MatOfInt[] channels;
    Mat rgba;
    MatOfInt histogramSize;
    MatOfFloat histogramRange;
    int histogramHeight;
    int histSize, binWidth;
    Scalar[] colorsRgb;
    public HistogramProcess(Mat rgba, Size rgbaSize){
        this.rgba = rgba;
        //Set the amount of bars in the histogram.
        histSize = 256;
        histogramSize = new MatOfInt(histSize);

        //Set the height of the histogram and width of the bar
        histogramHeight = (int) rgbaSize.height;
        binWidth = 5;

        //Set the value range
        histogramRange = new MatOfFloat(0f, 256f);

        //Create two separate lists: onr for colors and one for channels ( these will be used as separate data sets)
        colorsRgb = new Scalar[]{new Scalar(200,0,0,255), new Scalar(0,200,0,255), new Scalar(0,0,200,255)};
        channels = new MatOfInt[]{new MatOfInt(0), new MatOfInt(1), new MatOfInt(2)};

        //Create an array to be saved in the histogram and a second array, on which the histogram chart will be drawn.
        histograms = new Mat[]{new Mat(), new Mat(), new Mat()};
        histMatBitmap = new Mat(rgbaSize, rgba.type());

        //Separating Channels
        CalHistForEachChannel();
    }

    /*
     Function Separateing channels from Source Image
     Source Image have three channels
     Create function by trongan93
     */
    private void CalHistForEachChannel(){
        for(int i = 0; i< channels.length; i++){
            Imgproc.calcHist(Collections.singletonList(rgba), channels[i], new Mat(), histograms[i], histogramSize, histogramRange);
            Core.normalize(histograms[i], histograms[i], histogramHeight, 0, Core.NORM_INF);
            for(int j = 0; j < histSize; j++){
                Point p1 = new Point(binWidth * (j -1), histogramHeight - Math.round(histograms[i].get((j - 1), 0)[0]));
                Point p2 = new Point(binWidth * j, histogramHeight - Math.round(histograms[i].get(j,0)[0]));
                Core.line(histMatBitmap, p1, p2, colorsRgb[i], 2, 8, 0);
            }

        }
    }

    public Bitmap GetBitMap(){
        Bitmap histogramBitmap = Bitmap.createBitmap(histMatBitmap.cols(), histMatBitmap.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(histMatBitmap, histogramBitmap);
        return histogramBitmap;
    }






}
