package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyMap<Integer, String> myMap = new MyHashMap<>();

        myMap.put(1, "First");
        myMap.put(2, "Second");
        myMap.put(3, "Third");
        myMap.put(17, "Forth");
        myMap.put(33, "Fifth");
        myMap.put(49, "Sixth");
        myMap.put(null, "null");
        myMap.put(21, "First");
        myMap.put(232, "Second");
        myMap.put(323, "Third");
        myMap.put(417, "Forth");
        myMap.put(215, "Fifth");
        myMap.put(325, "Sixth");

    }
}
