package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap<Integer, String> string = new MyHashMap<>();
        string.put(2, "BatMan"); //2
        string.put(3, "AquaMan"); //3
        string.put(1, "SpiderMan"); //1
        string.put(17, "SuperMan"); // 1
        string.put(33, "WonderWoman"); //1
        string.put(34, "NightWolf"); //2
        string.put(18, "LiuKang"); //2

        System.out.println(string);
        System.out.println(string.getSize());
    }
}
