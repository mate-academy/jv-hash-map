package core.basesyntax;

public class Main {

    public static void main(String[] args) {
        MyHashMap<Integer, Integer> map = new MyHashMap<>();
        map.put(1, 1);
        map.put(null, 99);
        map.put(2, 2);
        map.put(null, 100);
        map.put(3, 3);
        map.put(3, 33);
        System.out.println(map.getValue(1));
        System.out.println(map.getValue(null));
        System.out.println(map.getValue(2));
        System.out.println(map.getValue(null));
        System.out.println(map.getValue(3));
        System.out.println(map.getValue(3));
        System.out.println("size = " + map.getSize());
        System.out.println("*******************************");
        Bus firstBus = new Bus("FirstBus", "white");
        Bus secondBus = new Bus("SecondBus", "white");
        Bus thirdBus = new Bus("ThirdBus", "grey");
        MyMap<Bus, Integer> myHashMap = new MyHashMap<>();
        myHashMap.put(firstBus, 3);
        myHashMap.put(null, 4);
        myHashMap.put(secondBus, 5);
        myHashMap.put(null, 10);
        myHashMap.put(thirdBus, 1);
        System.out.println(myHashMap.getValue(firstBus));
        System.out.println(myHashMap.getValue(null));
        System.out.println(myHashMap.getValue(secondBus));
        System.out.println(myHashMap.getValue(null));
        System.out.println(myHashMap.getValue(thirdBus));
        System.out.println("size = " + myHashMap.getSize());

    }
}
