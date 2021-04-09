package core.basesyntax;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private List<Node<K, V>>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new List[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, Objects.hashCode(key));
        int indexFor = hash(key);
        if (table[indexFor] == null) {
            initBucket(indexFor);
            add(indexFor, node);
        } else {
            int index = 0;
            for (Node<K, V> n : table[indexFor]) {
                if (Objects.equals(node.key, n.key)) {
                    replace(table[indexFor], n, node, index);
                    return;
                }
                index++;
            }
            table[indexFor].add(node);
            size++;
        }
        if ((size + 1) > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int indexFor = hash(key);
        if (table[indexFor] != null) {
            for (Node<K, V> n : table[indexFor]) {
                if (Objects.equals(key, n.key)) {
                    return n.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private K key;
        private V value;

        Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }

    private void resize() {
        final List<Node<K, V>>[] oldTab = table;
        table = new List[newCapacity()];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;
        transfer(oldTab, table);
    }

    private void transfer(List<Node<K, V>>[] old, List<Node<K, V>>[] newTab) {
        for (List<Node<K, V>> bucket : old) {
            if (bucket != null) {
                for (Node<K, V> n : bucket) {
                    put(n.key, n.value);
                }
            }
        }
    }

    private int newCapacity() {
        return table.length << 1;
    }

    private void replace(List<Node<K, V>> nodes,
                         Node<K, V> replaced, Node<K, V> newNode, int index) {
        nodes.remove(replaced);
        nodes.add(index, newNode);
    }

    private void add(int index, Node<K, V> node) {
        table[index].add(node);
        size++;
    }

    private void initBucket(int index) {
        table[index] = new LinkedList<>();
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
