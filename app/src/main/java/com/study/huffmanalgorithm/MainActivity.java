package com.study.huffmanalgorithm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="Huffman";

    public static PriorityQueue<Node> queue; // 우선순위 큐
    public static HashMap<Character, String> charToCode=new HashMap<Character, String>(); // 문자에 따른 코드 값 해시맵 할당

    String[] hexa2bin = {"0000", "0001", "0010", "0011",

            "0100", "0101", "0110", "0111",

            "1000", "1001", "0010", "0011",

            "1100", "1101", "1110", "1111"};

    String[] bin2HEXA =	{"0", "1", "2", "3",

            "4", "5", "6", "7",

            "8", "9", "A", "B",

            "C", "D", "E", "F"};

    String[] bin2hexa =	{"0", "1", "2", "3",

            "4", "5", "6", "7",

            "8", "9", "a", "b",

            "c", "d", "e", "f"};

    String[][] binary;
    String binary_to_hex="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String stringHex=bitmap_to_hex();

        binary = new String[stringHex.length()][bin2hexa.length];

        Log.d(TAG, "16진수 : " + stringHex);
//
//
//        //hex를 2진수로
//        for (int i=0; i< stringHex.length(); i++){
//            for(int j=0; j <bin2hexa.length; j++) {
//                if (stringHex.substring(i, i + 1).compareTo(bin2hexa[j]) == 0) {
//                    binary[i][j] = hexa2bin[j];
//                    Log.d(TAG, "확인 : " + binary[i][j]);
//                }
//                else if (stringHex.substring(i, i + 1).compareTo(bin2HEXA[j]) == 0){
//                    binary[i][j] = hexa2bin[j];
//                    Log.d(TAG, "확인 : " + binary[i][j]);
//                }
//            }
//            binary_to_hex=binary_to_hex+binary[i];
//            //Log.d(TAG, "2진수 : " + list + " ");
//        }
//
//        Log.d(TAG, "2진수 : " + binary_to_hex);

//        Resources res=getResources();
//        Bitmap img_bitmap= BitmapFactory.decodeResource(res,R.drawable.sample);
//        Log.d(TAG, "bitmap 변환 : "+img_bitmap);
//    HashMap <Character,Integer> dictionary=new HashMap<Character, Integer>(); // 각각의 문자에 대한 빈도수 체크
//    for(int i=0; i<bitmap_to_hex().length(); i++){
//        char temp=bitmap_to_hex().charAt(i);
//        if(dictionary.containsKey(temp)) // 현재 문자가 이미 1번 이상 들어가 있다면 1 증가
//            dictionary.put(temp,dictionary.get(temp)+1);
//        else
//            dictionary.put(temp,1);
//    }
//    // 모든 노드를 우선순위 큐에 추가함으로써 트리 그룹 형성
//        queue=new PriorityQueue<Node>(1000, new FrequencyComparator());
//    int number=0;
//
//    //문자와 그 빈도수가 저장된 각각의 모든 노드들을 우선순위 큐에 삽입
//        for(Character c:dictionary.keySet()){
//            Node temp=new Node();
//            temp.character=c;
//            temp.frequency=dictionary.get(c);
//            queue.add(temp);
//            number++;
//        }
//
//        //전체 노드 개수만큼 재배열하여 우선순위 큐 상에서의 노드 재배열
//        Node root=huffmanCoding(number);
//        //변수 길이 코드를 생성
//        traversal(root, new String());
//
//        String result=new String();
//        for(int i=0; i<bitmap_to_hex().length(); i++){
//            result=result+charToCode.get(bitmap_to_hex().charAt(i))+" ";
//            Log.d(TAG, "result : " + result);
//        }

   }


    String bitmap_to_hex() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        Resources res=getResources();
        Bitmap receipt= BitmapFactory.decodeResource(res,R.drawable.sample);

        Log.d(TAG, "bitmap 변환 : " + receipt);
        int size = receipt.getRowBytes() * receipt.getHeight();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        receipt.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        Log.d(TAG, "압축?? : " + receipt.compress(Bitmap.CompressFormat.JPEG, 90, stream));
        byte[] receiptbyte = stream.toByteArray();

        Log.d(TAG, "receiptbyte : " + receiptbyte);
        //String hexstring = toHex(receiptbyte);

        BigInteger bi = new BigInteger(1, receiptbyte);
        Log.d(TAG, "toHex : " + String.format("%0" + (receiptbyte.length << 1) + "X", bi));
        String hexstring=  String.format("%0" + (receiptbyte.length << 1) + "X", bi);
        return hexstring;
    }

//    public static String toHex(byte[] bytes) {
//        BigInteger bi = new BigInteger(1, bytes);
//        Log.d(TAG, "toHex : " + String.format("%0" + (bytes.length << 1) + "X", bi));
//        return String.format("%0" + (bytes.length << 1) + "X", bi);
//    }

    public static Node huffmanCoding(int n){
        // 각각의 문자 빈도수에 따라서 트리를 건축하는 메소드
        for(int i=0; i<n-1; i++){
            Node node=new Node();
            node.right=queue.poll();
            node.left=queue.poll();
            node.frequency=node.right.frequency+node.left.frequency;
            queue.add(node);
        }
        return queue.poll();
    }

    //순회로 노드를 돌아 코드를 입력력
   public static void traversal(Node n, String s){
        if(n==null) return;
        traversal(n.left,s+"0");
        traversal(n.right,s+"1");
        if(n.character!='\0'){
            charToCode.put(n.character,s);
        }
    }
}