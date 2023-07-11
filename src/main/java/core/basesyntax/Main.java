package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap<Integer,Integer> map = new MyHashMap<>();
        map.put(3,1);
        map.put(4,5);
        map.put(3,5);
        System.out.println(map.getValue(3));
    }
}
