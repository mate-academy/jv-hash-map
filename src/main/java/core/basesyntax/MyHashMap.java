package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_COEFFICIENT = 2;
    private int currentCapacity;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        currentCapacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(index, key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else if (Objects.equals(table[index].key, newNode.key)
                && table[index].next == null) {
            table[index] = newNode;
        } else if (Objects.equals(table[index].key, newNode.key)
                && table[index].next != null) {
            newNode.next = table[index].next;
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    break;
                }
                if (Objects.equals(current.next.key, newNode.key)) {
                    newNode.next = current.next.next;
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int indexOfKey = getIndex(key);
        if (getIndex(key) < table.length) {
            Node<K, V> current = table[indexOfKey];
            while (current != null) {
                if (Objects.equals(current.key,key)) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size == threshold) {
            currentCapacity *= GROW_COEFFICIENT;
            threshold *= GROW_COEFFICIENT;
            size = 0;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[currentCapacity];
            for (int i = 0; i < oldTable.length; i++) {
                if (oldTable[i] == null) {
                    continue;
                }
                Node<K, V> current = oldTable[i];
                while (current != null) {
                    int newIndex = getIndex(current.key);
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % currentCapacity);
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
