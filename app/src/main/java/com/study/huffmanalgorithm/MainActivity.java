package com.study.huffmanalgorithm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

    String hexstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res=getResources();
        Bitmap receipt= BitmapFactory.decodeResource(res,R.drawable.sample2);
        hexstring=BitmapToString(receipt);
        //Log.d(TAG, "hexstring : "+hexstring);
//        hexstring=bitmap_to_hex();
//        Log.d(TAG, "hexstring : "+hexstring);

//        binary = new String[stringHex.length()][bin2hexa.length];

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

    HashMap <Character,Integer> dictionary=new HashMap<Character, Integer>(); // 각각의 문자에 대한 빈도수 체크

    for(int i=0; i<hexstring.length(); i++){ // dictionary에 A, B, C, D, E, F, 0~9까지 빈도수가 들어간다
        char temp=hexstring.charAt(i);
        if(dictionary.containsKey(temp)) // 현재 문자가 이미 1번 이상 들어가 있다면 1 증가
            dictionary.put(temp,dictionary.get(temp)+1);
        else
            dictionary.put(temp,1);

       //Log.d(TAG, "dictionary : " + dictionary);
    }
    // 모든 노드를 우선순위 큐에 추가함으로써 트리 그룹 형성
        queue=new PriorityQueue<Node>(hexstring.length(), new FrequencyComparator());
    int number=0;

    //문자와 그 빈도수가 저장된 각각의 모든 노드들을 우선순위 큐에 삽입
        for(Character c:dictionary.keySet()){
            Node temp=new Node();
            temp.character=c;
            temp.frequency=dictionary.get(c);
            queue.add(temp);
            number++;
            //Log.d(TAG, "character : " + temp.character + "\nfrequency : "+temp.frequency);
        }

        Log.d(TAG, "number : " + number);

        //전체 노드 개수만큼 재배열하여 우선순위 큐 상에서의 노드 재배열
        Node root=huffmanCoding(number);
        Log.d(TAG, "Node root : " + root);
        //변수 길이 코드를 생성
        traversal(root, new String());

//        String result="";
//        for(int i=0; i<hexstring.length(); i++){
//            result=result+charToCode.get(hexstring.charAt(i))+" ";
//            Log.d(TAG+i, "결과 result : " + result);
//        }

   }


//    String bitmap_to_hex() {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 8;
//
//        Resources res=getResources();
//        Bitmap receipt= BitmapFactory.decodeResource(res,R.drawable.sample2);
//
//        //Log.d(TAG, "bitmap 변환 : " + receipt);
//        int size = receipt.getRowBytes() * receipt.getHeight();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//        receipt.compress(Bitmap.CompressFormat.JPEG, 90, stream);
//        //Log.d(TAG, "압축?? : " + receipt.compress(Bitmap.CompressFormat.JPEG, 90, stream));
//        byte[] receiptbyte = stream.toByteArray();
//
//        Log.d(TAG, "receiptbyte : " + receiptbyte);
//
//        String receiptbyteString=new String(receiptbyte);
//        Log.d(TAG, "receiptbyteString : " + receiptbyteString);
//
//        //String hexstring = toHex(receiptbyte);
//
//        BigInteger bi = new BigInteger(1, receiptbyte);
//        Log.d(TAG, "toHex : " + String.format("%0" + (receiptbyte.length << 1) + "X", bi));
//        //hexstring=  String.format("%0" + (receiptbyte.length << 1) + "X", bi);
//
//        return String.format("%0" + (receiptbyte.length << 1) + "X", bi);
//    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

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

    public static Node huffmanCoding(int n){
        // 각각의 문자 빈도수에 따라서 트리를 건축하는 메소드
        for(int i=0; i<n-1; i++){
            Node node=new Node();
            node.right=queue.poll();
            node.left=queue.poll();
            node.frequency=node.right.frequency+node.left.frequency;
            queue.add(node);
        }
        Log.d(TAG, "result : " + queue.poll());
        return queue.poll();
    }

    //순회로 노드를 돌아 코드를 입력
   public static void traversal(Node n, String s){
        if(n==null) return;
        traversal(n.left,s+"0");
        traversal(n.right,s+"1");
        if(n.character!='\0'){
            charToCode.put(n.character,s);
        }

    }
}