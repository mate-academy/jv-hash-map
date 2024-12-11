package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] storage;

    public MyHashMap() {
        storage = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkLoadedCapacity();
        putByIndex(key, value, calculateIndex(key));
    }

    @Override
    public V getValue(K key) {
        if (size > 0) {
            Node<K, V> temp = storage[calculateIndex(key)];
            while (temp != null) {
                if (Objects.equals(key, temp.key)) {
                    return temp.value;
                }
                temp = temp.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resizeArray() {
        Node<K, V>[] boxes = new Node[storage.length];
        System.arraycopy(storage, 0, boxes, 0, storage.length);
        storage = new Node[storage.length * 2];
        return boxes;
    }

    private void writeDataToNewArray(Node<K, V>[] boxes) {
        Node<K, V> temp;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] == null) {
                continue;
            }
            while (boxes[i] != null) {
                temp = boxes[i];
                boxes[i] = boxes[i].next;
                temp.next = null;
                size--;
                putByIndex(temp.key, temp.value, calculateIndex(temp.key));
            }
        }
    }

    private void putByIndex(K key, V value, int index) {
        if (storage[index] == null) {
            storage[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> temp = storage[index];
            while (temp != null) {
                if (Objects.equals(temp.key, key)) {
                    temp.value = value;
                    return;
                }
                if (temp.next == null) {
                    break;
                }
                temp = temp.next;
            }
            temp.next = new Node<>(key, value, null);
        }
        size++;
    }

    private int calculateIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() %
                storage.length);
    }

    private void checkLoadedCapacity() {
        if (size >= storage.length * LOAD_FACTOR) {
            writeDataToNewArray(resizeArray());
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
