package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int newCapacity = DEFAULT_INITIAL_CAPACITY;

    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        if (size > valueResize()) {
            resize();
        }
        size++;
        int index = findIndex(key);
        if (table[index] == null) {
            table[index] = node;
            return;
        }
        if (table[index].key != null && table[index].key.equals(key)) {
            table[index].value = value;
            size--;
            return;
        }
        Node<K, V> newNode = table[index];
       while (newNode.next != null) {
           if (newNode.next.key != null && newNode.next.key.equals(key)) {
               newNode.next.value = value;
               size--;
               return;
           }
           newNode = newNode.next;
           if (newNode.key != null && newNode.key.equals(key)) {
               newNode.value = value;
           }
           if (newNode.key == null && newNode.key == key) {
               newNode.value = value;
               size--;
           }
       }
       if (newNode.key == null && newNode.key == key) {
           newNode.value = value;
           size--;
       }
       newNode.next = node;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int findIndex(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode() % newCapacity);
        return index;
    }

    private void resize() {
        newCapacity = newCapacity << 1;
        Node<K, V>[] previousTable = table;
        table = new Node[newCapacity];
        size = 0;

        for (Node<K, V> node : previousTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    public float valueResize() {
        return newCapacity * DEFAULT_LOAD_FACTOR;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
