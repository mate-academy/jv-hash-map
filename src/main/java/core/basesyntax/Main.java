package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyMap<Integer, String> map = new MyHashMap<>();
        map.put(18,"Horse");
        map.put(2,"Horse Josh");
        map.put(17,"Kate");
        System.out.println(map.getValue(18));
    }
}
