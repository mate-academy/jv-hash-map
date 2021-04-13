package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int DEFAULT_CAPACITY = 16;
    private static float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] data;
    private int size;
    private int threshold;

    static class Node<K, V> {
        private Node<K, V> next;
        private V value;
        private final K key;
        private final int hash;

        public Node(Node<K, V> next, V value, K key, int hash) {
            this.next = next;
            this.value = value;
            this.key = key;
            this.hash = hash;
        }
    }

    @Override
    public void put(K key, V value) {
        if (data == null || size == threshold) {
            resize();
        }
        final Node<K, V> newNode = new Node<>(null, value, key,
                (key == null) ? 0 : key.hashCode());
        Node<K, V> existenceNode = searchNodeByKey(key);
        if (existenceNode == null) {
            int index = Math.abs((key == null ? 0 : key.hashCode()) % data.length);
            Node<K, V> temp = data[index];
            if (temp != null) {
                while (temp.next != null) {
                    temp = temp.next;
                }
                temp.next = newNode;
            } else {
                data[index] = newNode;
            }
            size++;
            return;
        }
        existenceNode.value = value;
    }

    @Override
    public V getValue(K key) {
        if (size != 0) {
            return Objects.requireNonNull(searchNodeByKey(key)).value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (data == null) {
            data = (Node<K, V>[])new Node[DEFAULT_CAPACITY];
            threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
            return;
        }
        int newCapacity = data.length * 2;
        Node<K, V>[] oldData = data;
        data = (Node<K, V>[])new Node[newCapacity];
        final int currentSize = size;
        threshold *= 2;
        for (Node<K, V> element : oldData) {
            if (element != null) {
                while (element != null) {
                    put(element.key, element.value);
                    element = element.next;
                }
            }
        }
        size = currentSize;
    }

    private Node<K, V> searchNodeByKey(K key) {
        int index = Math.abs((key == null ? 0 : key.hashCode()) % data.length);
        Node<K, V> temp = data[index];
        while (temp != null) {
            if (Objects.equals(temp.key, key)) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }
}
