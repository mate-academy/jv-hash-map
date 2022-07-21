package core.basesyntax;

import java.util.Map;

public class MainHashMap {
    public static void main(String[] args) {
        MyHashMap<String, Integer> myHashMap = new MyHashMap<>();
        System.out.println(myHashMap.size);
        myHashMap.put("bob",15);
        System.out.println(myHashMap.size);
        System.out.println(myHashMap.getValue("bob"));

    }
}
