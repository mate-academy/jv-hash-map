package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {


    private final int DEFAULT_CAPACITY = 16;

    private final double DEFAULT_LOAD_FACTOR = 0.75;

    private int size = 0;

    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        /* todo
            getHash of key
            check equals of keys in LL
            if match, replace, else add at the end
            at the end check on resize in another help function
        */
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
