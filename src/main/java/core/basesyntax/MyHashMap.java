package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int defaultCapacity;
    private final int growFactor;
    private final float loadFactor;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.defaultCapacity = 16;
        this.growFactor = 2;
        this.loadFactor = 0.75F;
        this.threshold = (int) (defaultCapacity * loadFactor);
        this.table = new Node[defaultCapacity];
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
        threshold *= growFactor;
        Node<K, V>[] oldArray = table;
        table = new Node[oldArray.length * growFactor];
        for (Node<K, V> node : oldArray) {
            Node<K, V> temp = node;
            while (temp != null) {
                put(temp.key, temp.value);
                temp = temp.next;
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
