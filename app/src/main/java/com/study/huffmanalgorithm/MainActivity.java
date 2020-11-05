package com.study.huffmanalgorithm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

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

    void bitmap_to_hex() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Resources res=getResources();
        Bitmap receipt= BitmapFactory.decodeResource(res,R.drawable.sample);
        int size = receipt.getRowBytes() * receipt.getHeight();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        receipt.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] receiptbyte = stream.toByteArray();
        String hexstring = toHex(receiptbyte);
    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }


}