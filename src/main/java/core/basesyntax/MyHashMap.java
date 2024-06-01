package core.basesyntax;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int SCALE = 2;
    private int capacity;
    private int size;
    private int threshold;
    private HashSet<Node<K, V>>[] backets;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (LOAD_FACTOR * capacity);
        backets = new HashSet[capacity];
        for (int i = 0; i < capacity; i++) {
            backets[i] = new HashSet<Node<K, V>>();
        }
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        HashSet<Node<K, V>> backet = backets[index];
        for (Node<K, V> element : backet) {
            K elementKey = element.key;
            V oldValue = element.value;
            if (Objects.equals(elementKey, key)) {
                element.value = value;
                return;
            }
        }
        Node<K, V> newNode = new Node<>(key, value);
        backet.add(newNode);
        ++size;
        if (size == threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        HashSet<Node<K, V>> bucket = backets[index];
        for (Node<K, V> node : bucket) {
            K currentKey = node.key;
            V value = node.value;
            if (Objects.equals(currentKey, key)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapasity = capacity * SCALE;
        threshold = (int) (newCapasity * LOAD_FACTOR);
        HashSet<Node<K, V>>[] newBackets = new HashSet[newCapasity];
        for (int i = 0; i < newCapasity; i++) {
            newBackets[i] = new HashSet<Node<K, V>>();
        }
        Set<Node<K, V>> tmp = new HashSet<>();
        for (HashSet<Node<K, V>> backet : backets) {
            tmp.addAll(backet);
        }
        backets = newBackets;
        capacity = newCapasity;
        size = 0;
        for (Node<K, V> kvNode : tmp) {
            put(kvNode.key, kvNode.value);
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        int index = key.hashCode() % capacity;
        index = index < 0 ? -index : index;
        return index;
    }

    private static class Node<K, V> {
        private K key;
        private V value;

        public Node() {
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }
}
