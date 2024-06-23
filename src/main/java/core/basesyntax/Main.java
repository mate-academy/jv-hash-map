package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyMap<Integer, String> map = new MyHashMap<>();
        for (int i = 1; i < 13; i++) {
            map.put(i,"line");
        }
        map.put(13, "some");
        System.out.println(map.getSize());
    }
}
