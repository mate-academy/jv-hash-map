package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        //System.out.println(getNewCapacity());
       /** MyHashMap<Integer, String> myHashMap = new MyHashMap<>();
        myHashMap.put(30, "qwerty");
        myHashMap.put(23, "asdfgh");
        myHashMap.put(30, "qwe");
        myHashMap.put(14, "zxcvbn");
        myHashMap.put(14, "xvb");
        myHashMap.put(null, "nulll");
        myHashMap.put(null, "NULLL"); */

        Bus firstBus = new Bus("FirstBus", "white");
        Bus secondBus = new Bus("SecondBus", "white");
        Bus thirdBus = new Bus("ThirdBus", "grey");

        MyMap<Bus, Integer> myHashMap1 = new MyHashMap<>();
        myHashMap1.put(firstBus, 3);
        myHashMap1.put(null, 4);
        myHashMap1.put(secondBus, 5);
        myHashMap1.put(null, 10);
        myHashMap1.put(thirdBus, 1);

        //myHashMap.print();
        System.out.println("Size: " + myHashMap1.getSize());
        System.out.println("==============================");
        System.out.println(myHashMap1.getValue(null));
    }
    private static int getNewCapacity() {
        int[] arrayInt = new int[16];
        return arrayInt.length << 1;
    }
}
