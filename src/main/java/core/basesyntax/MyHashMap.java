package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTORY = 0.75f;

    private Node<K,V>[] mapTable;
    private int size;
    private final float loadFactor;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTORY;
        this.threshold = (int) (DEFAULT_LOAD_FACTORY * DEFAULT_CAPACITY);
        mapTable = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int hash = hashCode(key);
        int index = (mapTable.length - 1) & hash;
        Node<K,V> specificNode = mapTable[index];
        if (specificNode == null) {
            mapTable[index] = new Node<>(hash, key, value, null);
        } else {
            Node<K,V> tempNode;
            if (specificNode.hash == hash && (Objects.equals(key, specificNode.key))) {
                tempNode = specificNode;
            } else {
                for ( ; ; ) {
                    if ((tempNode = specificNode.next) == null) {
                        specificNode.next = new Node<>(hash, key, value, null);
                        break;
                    }
                    if (tempNode.hash == hash && (tempNode.key == key
                            || (key != null && key.equals(specificNode.key)))) {
                        break;
                    }
                    specificNode = tempNode;
                }
            }
            if (tempNode != null) {
                tempNode.value = value;
                return;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hashCode(key);
        Node<K,V> node = mapTable[(mapTable.length - 1) & hash];
        Node<K,V> nodeNext;
        if (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            if ((nodeNext = node.next) != null) {
                do {
                    if ((Objects.equals(key, nodeNext.key))) {
                        return nodeNext.value;
                    }
                } while ((nodeNext = nodeNext.next) != null);
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashCode(Object key) {
        int hash;
        return key == null ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        size = 0;
        int newLength = mapTable.length << 1;
        threshold = (int) (newLength * loadFactor);
        Node<K,V>[] oldTable = mapTable;
        mapTable = (Node<K,V>[])new Node[newLength];
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K,V> {
        private final int hash;
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
}
