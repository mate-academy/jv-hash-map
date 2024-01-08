package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyMap<String, Integer> myHashMap = new MyHashMap<>();

        myHashMap.put("One", 1);
        myHashMap.put("Two", 2);
        myHashMap.put("Three", 3);

        System.out.println("Value for key 'Two': " + myHashMap.getValue("Two"));
        System.out.println("Value for key 'Four': " + myHashMap.getValue("Four"));
        System.out.println("Size of the map: " + myHashMap.getSize());

        // Adding more entries to trigger resizing
        for (int i = 0; i < 20; i++) {
            myHashMap.put("Key" + i, i);
        }

        System.out.println("Size of the map after resizing: " + myHashMap.getSize());
    }
}
