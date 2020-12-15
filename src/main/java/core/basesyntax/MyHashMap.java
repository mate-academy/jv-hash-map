package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] array;
    private int size;
    private int threshold;

    public MyHashMap() {
        array = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (++size > threshold) {
            array = resize();
        }
        addElement(key, value, array);
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(key == null ? 0 : (key.hashCode() % array.length));
        Node<K, V> current = array[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addElement(K key, V value, Node<K, V>[] currentArray) {
        int keyHashCode = key == null ? 0 : key.hashCode();
        int index = keyHashCode % currentArray.length;
        index = Math.abs(index);
        Node<K, V> current;
        Node<K, V> prevNode = null;
        if (currentArray[index] == null) {
            current = currentArray[index];
        } else {
            current = currentArray[index];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    size--;
                    return;
                }
                if (current.next == null) {
                    prevNode = current;
                }
                current = current.next;
            }
        }
        Node<K, V> newNode = new Node(keyHashCode, value, key, null);
        if (prevNode == null) {
            currentArray[index] = newNode;
        } else {
            prevNode.next = newNode;
        }
    }

    private Node<K, V>[] resize() {
        int newLength = array.length * 2;
        Node<K, V>[] newArray = new Node[newLength];
        Node<K, V> current;
        for (int i = 0; i < array.length; i++) {
            current = array[i];
            while (current != null) {
                addElement(current.key, current.value, newArray);
                current = current.next;
            }
        }
        threshold = (int) (newLength * DEFAULT_LOAD_FACTOR);
        return newArray;
    }

    private static class Node<K, V> {
        final int hashCode;
        final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hashCode, V value, K key, Node<K, V> next) {
            this.hashCode = hashCode;
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
