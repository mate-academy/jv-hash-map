package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        //System.out.println(getNewCapacity());
        MyHashMap<Integer, String> myHashMap = new MyHashMap<>();
        myHashMap.put(13, "qwerty");
        myHashMap.put(23, "asdfg");
        myHashMap.print();
    }
    private static int getNewCapacity() {
        int[] arrayInt = new int[16];
        return arrayInt.length << 1;
    }
}
