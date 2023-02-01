package core.basesyntax;

public class Main {
    public static void main(String[] args) {
        MyMap<TestObj, String> map = new MyHashMap<>();
        map.put(new TestObj(1, "Hello"),"one");
        map.put(null, "null value");
        map.put(new TestObj(5, "hewadf"),"two");
        map.put(new TestObj(5, "hasdf"),"three");
        map.put(new TestObj(5, "hgssa"),"four");
        map.put(new TestObj(5, "asgsdfasf"),"five");
    }
}
