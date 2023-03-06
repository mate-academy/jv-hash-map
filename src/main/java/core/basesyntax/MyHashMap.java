package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_THRESHOLD = 12;

    private int arrayCapacity = DEFAULT_CAPACITY;
    private int threshold = DEFAULT_THRESHOLD;
    private int size;
    private Node[] hashMapArray = new Node[arrayCapacity];

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int hash;
        if (key == null) {
            hash = 0;
        } else {
            hash = key.hashCode();
        }
        int elementPosition = hash % arrayCapacity;
        if (elementPosition < 0) {
            elementPosition /= -1;
        }
        if (hashMapArray[elementPosition] == null) {
            hashMapArray[elementPosition] = new Node(hash, key, value, null);
            size++;
        } else {
            Node node = hashMapArray[elementPosition];
            while (true) {
                if (key == null || node.key == null) {
                    if (key == null && node.key == null) {
                        node.value = value;
                        break;
                    }
                } else {
                    if (node.key.equals(key)) {
                        node.value = value;
                        break;
                    }
                }
                if (node.next == null) {
                    node.next = new Node(hash, key, value, null);
                    size++;
                    break;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int elementPosition;
        if (key == null) {
            elementPosition = 0;
        } else {
            elementPosition = key.hashCode() % arrayCapacity;
        }
        if (elementPosition < 0) {
            elementPosition /= -1;
        }
        Node node = hashMapArray[elementPosition];
        while (true) {
            if (node == null) {
                return null;
            }
            if (Objects.equals(key, node.key)) {
                return (V) node.value;
            }
            node = node.next;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        arrayCapacity *= 2;
        threshold *= 2;
        Node[] oldArray = Arrays.copyOf(hashMapArray, hashMapArray.length);
        hashMapArray = new Node[arrayCapacity];
        for (Node node : oldArray) {
            while (node != null) {
                int elementPosition = node.hash % arrayCapacity;
                if (elementPosition < 0) {
                    elementPosition /= -1;
                }
                if (hashMapArray[elementPosition] == null) {
                    hashMapArray[elementPosition] = new Node(node.hash, node.key, node.value, null);
                } else {
                    Node tmpNode = hashMapArray[elementPosition];
                    while (true) {
                        if (tmpNode.next == null) {
                            tmpNode.next = new Node(node.hash, node.key, node.value, null);
                            break;
                        }
                        tmpNode = tmpNode.next;
                    }
                }
                node = node.next;
            }
        }
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }
}
