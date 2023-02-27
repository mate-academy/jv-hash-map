package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75F;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            size = 0;
            threshold *= GROW_FACTOR;
            resize();
        }
        if (table[getIndex(key)] == null) {
            table[getIndex(key)] = new Node<>(key, value, null);
        } else {
            Node<K, V> temp = table[getIndex(key)];
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
        Node<K, V>[] oldArr = table;
        table = new Node[oldArr.length * 2];
        for (Node<K, V> node : oldArr) {
            if (node != null) {
                Node<K, V> temp = node;
                while (temp != null) {
                    put(temp.key, temp.value);
                    temp = temp.next;
                }
            }
        }
    }

    private int getHashCode(Object key) {
        int h;
        return key == null ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
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
