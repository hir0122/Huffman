package com.study.huffmanalgorithm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="Huffman";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res=getResources();
        Bitmap img_bitmap= BitmapFactory.decodeResource(res,R.drawable.sample);
        Log.d(TAG, "bitmap 변환 : "+img_bitmap);
    }
}