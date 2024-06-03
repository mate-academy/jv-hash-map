package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);

        System.out.println("Value for key 'One': " + map.getValue("One"));
        System.out.println("Value for key 'Two': " + map.getValue("Two"));
        System.out.println("Value for key 'Three': " + map.getValue("Three"));
        System.out.println("Size of the map: " + map.getSize());

        System.out.println("Contains key 'Two': " + map.containsKey("Two"));
        System.out.println("Removed value for key 'Two': " + map.remove("Two"));
        System.out.println("Contains key 'Two' after removal: " + map.containsKey("Two"));
        System.out.println("Size of the map after removal: " + map.getSize());
    }
}
