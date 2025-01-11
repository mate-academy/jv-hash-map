package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap<String, String> names = new MyHashMap<>();
        names.put("max", "Cool man");
        names.put("Liza", "Cool girl");
        names.put("Liz", "Cool girl");
        names.put("Lza", "Cool girl");
        names.put("Lia", "Cool girl");
        names.put("1", "Cool girl");
        names.put("2", "Cool girl");
        names.put("3", "Cool girl");
        names.put("4", "Cool girl");
        names.put("5", "Cool girl");
        names.put("6", "Cool girl");
        names.put("7", "Cool girl");
        names.put("8", "Cool girl");
        names.put("9", "Cool girl");

        System.out.println(names);

        System.out.println(names.getValue("max"));
        System.out.println(names.getValue("Liza"));
        System.out.println(names.getSize());
    }
}
