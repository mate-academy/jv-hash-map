package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyMap<String, Integer> map = new MyHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        System.out.println("Value for key 'one': " + map.getValue("one"));
        System.out.println("Value for key 'four': " + map.getValue("four"));
        System.out.println("Size of map: " + map.getSize());
    }
}
