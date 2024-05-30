package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap<String, Integer> strings = new MyHashMap<>();
        strings.put("one", 1);
        strings.put(null, 2);
        strings.put("three", 3);
        strings.put(null, 4);

        System.out.println("Size - " + strings.getSize());
        System.out.println(strings.getValue("one"));
        System.out.println(strings.getValue(null));
        System.out.println(strings.getValue("two"));

    }
}
