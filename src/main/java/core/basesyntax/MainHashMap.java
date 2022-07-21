package core.basesyntax;

import java.util.Map;

public class MainHashMap {
    public static void main(String[] args) {

        MyHashMap<String, Integer> myHashMap = new MyHashMap<>();
        System.out.println(myHashMap.isEmpty());
        myHashMap.put("bob",15);
        System.out.println(myHashMap.getSize());
        System.out.println(myHashMap.getValue("bob"));
        System.out.println(myHashMap.containsKey("bob"));
    }
}
