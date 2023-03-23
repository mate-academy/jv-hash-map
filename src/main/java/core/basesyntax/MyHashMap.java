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
        Node<K,V> specificNode = mapTable[getIndex(key)];
        if (specificNode == null) {
            mapTable[getIndex(key)] = new Node<>(key, value);
        } else {
            Node<K,V> tempNode = null;
            if (Objects.equals(key, specificNode.key)) {
                tempNode = specificNode;
            } else {
                for (int i = 0; i < mapTable.length; i++) {
                    if ((tempNode = specificNode.next) == null) {
                        specificNode.next = new Node<>(key, value);
                        break;
                    }
                    if (Objects.equals(key, tempNode.key)) {
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
        for (Node<K,V> node = mapTable[getIndex(key)]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
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

    private int getIndex(K key) {
        return (mapTable.length - 1) & hashCode(key);
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
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
