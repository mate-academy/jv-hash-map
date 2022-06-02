
package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_VALUE = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_INDEX = 2;
    private Node [] elements = new Node[DEFAULT_VALUE];
    private int size;

    private class Node<K,V> {
        private Integer hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    @Override
    public void put(K key, V value) {
        if (size > elements.length * LOAD_FACTOR) {
            resize();
        }
        int index = Math.abs(getHashCode(key)) % elements.length;
        if (elements[index] != null) {
            Node currentBucket = elements[index];
            do {
                if (currentBucket.hash == getHashCode(key)
                        && Objects.equals(key, currentBucket.key)) {
                    currentBucket.value = value;
                    return;
                }
                if (currentBucket.next != null) {
                    currentBucket = currentBucket.next;
                }
            } while (currentBucket.next != null);
            if (currentBucket.hash == getHashCode(key) && Objects.equals(key, currentBucket.key)) {
                currentBucket.value = value;
                return;
            }
            Node newNode = new Node<>(getHashCode(key), key, value, null);
            currentBucket.next = newNode;
            size++;
            return;
        }
        elements[index] = new Node(getHashCode(key), key, value, null);
        size++;
    }

    public void resize() {
        Node<K, V>[] oldBuckets = elements;
        elements = new Node[oldBuckets.length * RESIZE_INDEX];
        size = 0;
        for (int i = 0; i < oldBuckets.length; i++) {
            Node<K, V> node = oldBuckets[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != null) {
                if (key == null) {
                    i = 0;
                }
                if (Objects.equals(key, elements[i].key)) {
                    return (V) elements[i].value;
                } else if (elements[i].next != null) {
                    Node currentBucket = elements[i].next;
                    while (currentBucket != null) {
                        if (Objects.equals(key, currentBucket.key)) {
                            return (V) currentBucket.value;
                        }
                        currentBucket = currentBucket.next;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
