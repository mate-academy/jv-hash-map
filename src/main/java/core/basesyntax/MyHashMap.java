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
        if (size >= threshold) {
            threshold *= DOUBLING_FACTOR;
            resize();
        }
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int index = (table.length - 1) & newNode.hash;
        if (table[index] == null) {
            justAddNewNode(index, newNode);
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                if (theseKeysAreEqual(current.key, newNode.key)) {
                    reWrightValue(current, newNode);
                    return;
                } else {
                    current = current.next;
                }
            }
            if (theseKeysAreEqual(current.key, newNode.key)) {
                reWrightValue(current, newNode);
                return;
            }
            addNextNewNode(current, newNode);
        }
    }

    @Override
    public V getValue(K key) {
        int index = (table.length - 1) & hash(key);
        if (index < table.length && table[index] != null) {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.hash == hash(key)
                        && Objects.equals(current.key, key)) {
                    return current.value;
                } else {
                    current = current.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return 0;
        return size;
    }

    @Override
    public String toString() {
        return "table=" + Arrays.toString(table);
    }
    }
}
