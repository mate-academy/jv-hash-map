import core.basesyntax.MyHashMap;
import core.basesyntax.MyMap;

public class Main {
    public static void main(String[] args) {
        MyMap<String, Integer> myHashMap = new MyHashMap<>();
        for (int i = 0; i < 1000; i++) {
            myHashMap.put("str" + i, i);
        }
        for (int i = 0; i < 1000; i++) {
            System.out.println(myHashMap.getValue("str" + i));
        }
    }
}
