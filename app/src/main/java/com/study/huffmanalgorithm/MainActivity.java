package com.study.huffmanalgorithm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Huffman";

    String hexstring;

    private static Map<Character, String> prefixCodeTable = new HashMap<>();    // Binary PrefixCode by Character (HashMap)

    // Node of Huffman Tree
    static class Node implements Comparable<Node> {
        char cData;
        int frequency;
        Node left, right;

        Node() {
        }

        Node(char cData, int frequency) {
            this.cData = cData;
            this.frequency = frequency;
        }

        // For Sorting priorityQueue
        @Override
        public int compareTo(Node node) {
            return frequency - node.frequency;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        Bitmap receipt = BitmapFactory.decodeResource(res, R.drawable.sample);
        hexstring = bitmap_to_hex(receipt);

        Log.d(TAG, "Original data : " + this.hexstring);

        // Huffman Encoding
        String encodeData = encode(hexstring);
        Log.d(TAG, "Encoded data : " + encodeData);
        Double size= (encodeData.length())/1024.0;
        Log.d(TAG, "크기 : " + size);

        // Huffman Decoding
        String decodeData = decode(encodeData);
        Log.d(TAG, "Decoded data : " + decodeData + "\n");

        // Print Report
        int originDataByteSize = hexstring.getBytes(StandardCharsets.UTF_8).length;
        Log.d(TAG, "Original data size : " + originDataByteSize * 8 + "Bit (" + originDataByteSize + "Byte)");
        int encodeDataByteSize = encodeData.length() % 8 == 0 ? encodeData.length() / 8 : encodeData.length() / 8 + 1;
        Log.d(TAG, "Encoded data size : " + encodeData.length() + "bit (" + encodeDataByteSize + "Byte)");
    }

    // Encoding method
    public static String encode(String data) {
        // Get Frequency by Character
        Map<Character, Integer> charFreq = new HashMap<>();
        for (char c : data.toCharArray()) {
            if (!charFreq.containsKey(c)) {
                charFreq.put(c, 1);
            } else {
                int no = charFreq.get(c);
                charFreq.put(c, ++no);
            }
        }
        Log.d(TAG, "Frequency by Character : " + charFreq);

        // Build Huffman Tree
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        Set<Character> keySet = charFreq.keySet();
        for (char c : keySet) {
            Node node = new Node(c, charFreq.get(c));
            priorityQueue.offer(node);      // Add priorityQueue by char's freq
        }
        Node rootNode = buildTree(priorityQueue);       // Recursive Call

        // Set Prefix Code by Character
        Log.d(TAG, "Prefix Code Table");
        setPrefixCode(rootNode, "");            // Recursive Call

        // Convert Origin data to Prefix code
        StringBuilder sb = new StringBuilder();
        for (char c : data.toCharArray()) {
            sb.append(prefixCodeTable.get(c));
        }

        return sb.toString();
    }

    // Decoding method
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String decode(String encodeData) {
        StringBuilder sb = new StringBuilder();
        String tmp = "";
        for (char c : encodeData.toCharArray()) {
            tmp += c;

            if (prefixCodeTable.containsValue(tmp)) {
                Stream<Character> keyStream = getKeysByValue(prefixCodeTable, tmp);
                char key = keyStream.findFirst().get();
                sb.append(key);
                tmp = "";
            }
        }
        return sb.toString();
    }

    // Build Huffman Tree Recursive method
    public static Node buildTree(PriorityQueue<Node> priorityQueue) {
        if (priorityQueue.size() == 1) {
            return priorityQueue.poll();
        } else {
            Node leftNode = priorityQueue.poll();
            Node rightNode = priorityQueue.poll();

            Node sumNode = new Node();
            sumNode.cData = '`';
            sumNode.frequency = leftNode.frequency + rightNode.frequency;
            sumNode.left = leftNode;
            sumNode.right = rightNode;

            priorityQueue.offer(sumNode);
            return buildTree(priorityQueue);
        }
    }

    // Set Prefix Code Recursive method
    public static void setPrefixCode(Node node, String code) {
        if (node == null) {
            return;
        }

        if (node.cData != '`' && node.left == null && node.right == null) {
            prefixCodeTable.put(node.cData, code);
            Log.d(TAG,"- " + node.cData + "(" + node.frequency + ") = " + code);
        } else {
            setPrefixCode(node.left, code + '0');
            setPrefixCode(node.right, code + '1');
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <K, V> Stream<K> getKeysByValue(Map<K, V> map, V value) {
        return map
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
    }

    void binary_to_byte(String encoding) {
        int i = 0;
        for (char c : encoding.toCharArray()) {
            i++;

        }
    }

    String bitmap_to_hex(Bitmap receipt) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;

        //Resources res=getResources();
        //Bitmap receipt= BitmapFactory.decodeResource(res,R.drawable.sample2);

        //Log.d(TAG, "bitmap 변환 : " + receipt);
        int size = receipt.getRowBytes() * receipt.getHeight();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        receipt.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        //Log.d(TAG, "압축?? : " + receipt.compress(Bitmap.CompressFormat.JPEG, 90, stream));
        byte[] receiptbyte = stream.toByteArray();

        Log.d(TAG, "receiptbyte : " + receiptbyte);

        //String hexstring = toHex(receiptbyte);

        BigInteger bi = new BigInteger(1, receiptbyte);
        Log.d(TAG, "toHex : " + String.format("%0" + (receiptbyte.length << 1) + "X", bi));
        //hexstring=  String.format("%0" + (receiptbyte.length << 1) + "X", bi);

        return String.format("%0" + (receiptbyte.length << 1) + "X", bi);
    }

//    public String BitmapToString(Bitmap bitmap) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
//        byte[] bytes = baos.toByteArray();
//        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
//        return temp;
//    }

//    int hex_to_string(){
//        int hex2dec = Integer.parseInt(bitmap_to_hex(), 16);
//        Log.d(TAG, "toHex : " + hex2dec);
//        return hex2dec;
//    }

//    public static String toHex(byte[] bytes) {
//        BigInteger bi = new BigInteger(1, bytes);
//        Log.d(TAG, "toHex : " + String.format("%0" + (bytes.length << 1) + "X", bi));
//        return String.format("%0" + (bytes.length << 1) + "X", bi);
//    }

}