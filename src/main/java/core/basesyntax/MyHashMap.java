package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] data;
    private int size;
    private int threshold;

    public MyHashMap() {
        data = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (data.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }

        Node<K, V> newNode = new Node<>(calculateHash(key), key, value, null);
        int index = getIndexByKey(key);

        Node<K, V> node = data[index];
        Node<K, V> prev = null;

        if (node == null) {
            data[index] = newNode;
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }

                prev = node;
                node = node.next;
            }

            prev.next = newNode;
        }

        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        Node<K, V> node = data[index];
        if (node != null) {
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

    private void resize() {
        Node<K, V>[] oldData = data;
        int oldCapacity = oldData.length;
        int newCapacity = oldCapacity * 2;
        threshold = (int) (newCapacity * LOAD_FACTOR);

        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCapacity];
        data = newTab;
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node;
            if ((node = oldData[i]) != null) {
                oldData[i] = null;
                if (node.next == null) {
                    newTab[node.hash & (newCapacity - 1)] = node;
                } else {
                    Node<K, V> lowHead = null;
                    Node<K,V> lowTail = null;
                    Node<K, V> highHead = null;
                    Node<K,V> highTail = null;
                    Node<K, V> next;
                    do {
                        next = node.next;
                        if ((node.hash & oldCapacity) == 0) {
                            if (lowTail == null) {
                                lowHead = node;
                            } else {
                                lowTail.next = node;
                            }

                            lowTail = node;
                        } else {
                            if (highTail == null) {
                                highHead = node;
                            } else {
                                highTail.next = node;
                            }

                            highTail = node;
                        }
                    } while ((node = next) != null);
                    if (lowTail != null) {
                        lowTail.next = null;
                        newTab[i] = lowHead;
                    }
                    if (highTail != null) {
                        highTail.next = null;
                        newTab[i + oldCapacity] = highHead;
                    }
                }
            }
        }
    }

    private int calculateHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getIndexByKey(K key) {
        return calculateHash(key) & (data.length - 1);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
