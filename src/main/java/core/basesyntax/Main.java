package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap myMap = new MyHashMap();
        
        
        myMap.put(1, "String");
        myMap.put(2, 10);
        myMap.put(3, 10);
        myMap.put(4, "hello");
        myMap.put(5, 10);
        myMap.put(6, 10);
        myMap.put(7, "new");
        myMap.put(8, 7478);
        myMap.put(9, 10);
        myMap.put(10, 123132);
        myMap.put(11, "abracdabra");
        myMap.put(12, 123132);
        myMap.put(13, 123132);
        myMap.put(14, 123132);
        myMap.put(20, "tytoepw;slfd;asld");
        
        myMap.put(15, "trtrtr");
        myMap.put(15, "hello");
        myMap.put(15, "zaza");
        
        myMap.put(31, "]]]]]]]]]]]]]]]]]");
        myMap.put(31, "zzzzzzzzzzzzzzzzzzz");
        myMap.put(31, "xxxxxxxxxxxxxxxxx");
        myMap.put(31, "aaaaaaa");
        myMap.put(63, "ppppppppp");
    
        System.out.println(myMap);
    }
}
