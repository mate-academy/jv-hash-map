package core.basesyntax;

import java.util.HashMap;
import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final int THRESHOLD;
    Node<K, V>[] table;
    private int CAPACITY;
    private int size;

    public MyHashMap() {
        THRESHOLD = (int) (CAPACITY * DEFAULT_LOAD_FACTOR);
        CAPACITY = 16;
        table = new Node[CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = hashCode(key) % CAPACITY;
        }
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hashCode(key), key, value, null);
            return;
        }
        while (currentNode != null) {
            if (currentNode.kay.equals(key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(hashCode(key), key, value, null);
                return;
            }
            currentNode = currentNode.next;
        }
        if (size++ > THRESHOLD) {

        }
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashCode(Object kay) {
        int h;
        return (kay == null) ? 0 : kay.hashCode();
    }

    static class Node<K, V> {
        final int hash;
        final K kay;
        V value;
        Node<K, V> next;
        Map<String, Integer> newmap = new HashMap<>();// ВИДАЛИТИ!

        public Node(int hash, K kay, V value, Node<K, V> next) {
            this.hash = hash;
            this.kay = kay;
            this.value = value;
            this.next = next;
        }

    }
}
