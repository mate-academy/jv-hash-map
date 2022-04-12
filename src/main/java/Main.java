import core.basesyntax.MyHashMap;

public class Main {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        MyHashMap<String, String> myMap = new MyHashMap<>();
        final int N = 2_000_000_000;
        for (int i = 0; i < N; i++) {
            char ch = (char) i;
            String key = i + String.valueOf(ch);
            String value = (i + i) + " " + "SoManyCharactersInThisString";
            myMap.put(key, value);
        }
        System.out.println(System.currentTimeMillis() - time);
    }
}
