package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.74F;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> temp = table[index];
            while (temp != null) {
                if (Objects.equals(temp.key, key)) {
                    temp.value = value;
                    return;
                }
                if (temp.next == null) {
                    temp.next = new Node<>(key, value, null);
                    break;
                }
                temp = temp.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> temp = table[getIndex(key)];
        while (temp != null) {
            if (Objects.equals(temp.key, key)) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return getHashCode(key) % table.length;
    }

    private void resize() {
        size = 0;
        threshold *= GROW_FACTOR;
        Node<K, V>[] oldArray = table;
        table = new Node[oldArray.length * GROW_FACTOR];
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getHashCode(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
