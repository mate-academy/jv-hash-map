package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap<Integer, Integer> map = new MyHashMap<>();
        map.put(0,10);
        map.put(2,20);
        map.put(4,40);
        map.put(5,50);
        map.put(6,60);
        map.put(7,70);
        map.put(16,500);
        map.put(32,30);
        map.put(null,40);
        map.put(32,1000);
        map.put(0,100);
        map.put(18,200);
        map.put(33,300);
        map.put(15,100);
        map.put(7,30);
        System.out.println(map.getValue(0));
        System.out.println(map);
    }
}
