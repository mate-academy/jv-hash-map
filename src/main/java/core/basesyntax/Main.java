package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyMap<String, String> map = new MyHashMap<>();
        map.put("1", "sasha");
        map.put("2", "sonya");

        System.out.println(map.getValue("2"));
    }
}
