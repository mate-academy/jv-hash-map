package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        //System.out.println(getNewCapacity());
        MyHashMap<Integer, String> myHashMap = new MyHashMap<>();
        myHashMap.put(13, "qwerty");
        myHashMap.put(23, "asdfg");
        //myHashMap.put();
        myHashMap.put(null, "zxcvb");
        myHashMap.print();
        System.out.println("==============================");
        System.out.println(myHashMap.getValue(null));
        System.out.println(myHashMap.getValue(13));
        System.out.println(null == null);
    }
    private static int getNewCapacity() {
        int[] arrayInt = new int[16];
        return arrayInt.length << 1;
    }
}
