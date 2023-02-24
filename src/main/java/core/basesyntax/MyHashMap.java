package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K,V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (key.hashCode() % capacity);
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTable = table;
        capacity = capacity << 1;
        table = new Node[capacity];
        for (Node<K, V> element : oldTable) {
            while (element != null) {
                put(element.key,element.value);
                element = element.next;
            }
        }

    }

    @Override
    public void put(K key, V value) {
        int treshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        if (size >= treshold) {
            resize();
        }
        int index = hash(key);
        Node<K, V> node = new Node<>(index,key,value,null);
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = node;
            size++;
            return;
        }
        while (current != null) {
            if ((current.key == key)
                    || (current.hash == index && Objects.equals(key,current.key))) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                break;
            }
            current = current.next;
        }
        current.next = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K,V> current = table[index];
        while (current != null) {
            if (current.hash == index && Objects.equals(key,current.key)) {
                return current.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K,V> {
        private final int hash;
        private Node<K,V> next;
        private final K key;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
