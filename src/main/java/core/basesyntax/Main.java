package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap<Integer, String> myMap = new MyHashMap<>();
        myMap.put(10, "ten");
        myMap.put(20, "twenty");
        myMap.put(10, "newTen");
        myMap.put(null, "null");
        myMap.put(null, "newNull");
        myMap.put(16, "null");
        myMap.put(32, "null");
        myMap.put(48, "null");
        myMap.put(64, "null");
        myMap.put(26, "null");

        System.out.println(myMap);
    }
}
