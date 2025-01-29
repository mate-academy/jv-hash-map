package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap<String, String> myHashMap = new MyHashMap<>();
        myHashMap.put("key1", "value1");
        myHashMap.put("key2", "value2");
        myHashMap.put("key3", "value3");

        System.out.println("Size: " + myHashMap.getSize());
        System.out.println("Value for 'key1': " + myHashMap.getValue("key1"));
        System.out.println("Value for 'key2': " + myHashMap.getValue("key2"));
        System.out.println("Value for 'key4': " + myHashMap.getValue("key4"));

        myHashMap.put("key1", "newValue");
        System.out.println("Updated Value for 'key1': " + myHashMap.getValue("key1"));

        myHashMap.put(null, "nullValue");
        System.out.println("Value for 'null': " + myHashMap.getValue(null));
    }
}
