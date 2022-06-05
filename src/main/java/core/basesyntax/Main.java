package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap<Integer, String> strings = new MyHashMap<>();
        strings.put(1, "SuperMan");
        strings.put(2, "BatMan");
        strings.put(17, "WonderWoman");
        strings.put(18, "GreenLight");
        strings.put(33, "MarcianHunter");
        strings.put(34, "Joker");

        System.out.println(strings);
        System.out.println(strings.getSize());
        System.out.println(strings.getValue(18));
    }
}
