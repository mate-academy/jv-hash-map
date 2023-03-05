package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final static int DEFAULT_CAPACITY = 16;
    private final static float LOAD_FOLDER = 0.75f;
    private int size = 0;
    private Node<K,V>[] table;

    @Override
    public void put(K key, V value) {

    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }
}
