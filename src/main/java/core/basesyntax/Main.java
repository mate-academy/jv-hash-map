package core.basesyntax;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        MyMap<Integer, Integer> myHashMap = new MyHashMap<>(16, 0.75F);

        for (int i = 0;i < 100;i++) {
            myHashMap.put(i, i + 1);
        }

        for (int i = 0;i < 100;i++) {
            System.out.println(myHashMap.getValue(i));
        }
    }
}
