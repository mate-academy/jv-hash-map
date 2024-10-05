package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DOUBLING_FACTOR = 2;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    @SuppressWarnings({"unchecked"})
    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.size = 0;
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

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
