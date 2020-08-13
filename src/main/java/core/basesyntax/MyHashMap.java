package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> newNode = new Node<>(key, value);
        int index = indexOf(newNode.key);
        Node<K,V> oldNode = table[index];
        if (oldNode == null) {
            table[index] = newNode;
        } else {
            while (oldNode.next != null || Objects.equals(oldNode.key, newNode.key)) {
                if (Objects.equals(oldNode.key, newNode.key)) {
                    oldNode.value = value;
                    return;
                }
                oldNode = oldNode.next;
            }
            oldNode.next = newNode;
        }
        size++;
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K,V> node = table[indexOf(key)]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int indexOf(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % (table.length - 1));
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * 2];
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyHashMap<?, ?> myHashMap = (MyHashMap<?, ?>) o;
        return Float.compare(myHashMap.LOAD_FACTOR, LOAD_FACTOR) == 0
                && DEFAULT_CAPACITY == myHashMap.DEFAULT_CAPACITY
                && size == myHashMap.size
                && Arrays.equals(table, myHashMap.table);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(LOAD_FACTOR, DEFAULT_CAPACITY, size);
        result = 31 * result + Arrays.hashCode(table);
        return result;
    }
}
