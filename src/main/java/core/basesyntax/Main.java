package core.basesyntax;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        MyHashMap<Integer, String> myHashMap = new MyHashMap<>();
        myHashMap.put(30, "qwerty");
        myHashMap.put(16, "asdfgh");
        //myHashMap.put(30, "qwe");
        //myHashMap.put(14, "zxcvbn");
        //myHashMap.put(14, "xvb");
        myHashMap.put(null, "nulll");
        //myHashMap.put(null, "NULLL");

        myHashMap.print();
        System.out.println("Size: " + myHashMap.getSize());
        System.out.println("==============================");
        myHashMap.put(1, "q1");
        myHashMap.put(2, "q2");
        myHashMap.put(3, "q3");
        myHashMap.put(4, "q4");
        myHashMap.put(5, "q5");
        myHashMap.put(6, "q6");
        myHashMap.put(7, "q7");
        myHashMap.put(8, "q8");
        myHashMap.put(9, "w9");
        myHashMap.put(10, "www10");
        //myHashMap.put(11, "w");

        myHashMap.print();
        System.out.println("Size: " + myHashMap.getSize());
    }
}
