package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap<Integer, String> people = new MyHashMap<>();
        people.put(35, "John");
        people.put(51, "Bob");
        System.out.println(people);
        System.out.println("res: " + people.getValue(51));
    }
}
