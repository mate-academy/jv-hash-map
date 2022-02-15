package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_INCREASE_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int keyHashcode = hashCodeGenerate(key);
        putVal(keyHashcode,key,value);
    }

    @Override
    public V getValue(K key) {
        int keyHashcode = hashCodeGenerate(key);
        Node<K,V> e;
        e = getNode(keyHashcode, key);
        return (e != null) ? e.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putVal(int hash, K key, V value) {
        int index = (key == null) ? 0 : hash % (table.length - 1);
        if (table[index] == null) {
            table[index] = new Node<>(hash,key,value,null);
        } else {
            Node<K,V> current = table[index];
            Node<K,V> previous = null;
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                previous = current;
                current = current.next;
            }
            if (previous != null) {
                previous.next = new Node<>(hash,key,value,null);
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    private void resize() {
        int newCapacity;
        newCapacity = table.length * DEFAULT_INCREASE_FACTOR;
        threshold = threshold * DEFAULT_INCREASE_FACTOR;
        Node<K,V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K,V> node : oldTable) {
            if (node == null) {
                continue;
            }
            while (node.next != null) {
                put(node.key, node.value);
                node = node.next;
            }
            put(node.key, node.value);
        }
    }

    private int hashCodeGenerate(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private Node<K,V> getNode(int hash, Object key) {
        Node<K, V>[] tab;
        Node<K, V> nodeToBeReturned;
        K keyFromReturnedNode;
        tab = table;
        if ((tab != null) && (nodeToBeReturned = tab[hash % (tab.length - 1)]) != null) {
            if (nodeToBeReturned.hash == hash
                    && ((keyFromReturnedNode = nodeToBeReturned.key) == key
                            || (key != null && key.equals(keyFromReturnedNode)))) {
                return nodeToBeReturned;
            } else {
                while (nodeToBeReturned != null) {
                    if (nodeToBeReturned.hash == hash
                            && ((keyFromReturnedNode = nodeToBeReturned.key) == key
                            || (key != null && key.equals(keyFromReturnedNode)))) {
                        return nodeToBeReturned;
                    }
                    nodeToBeReturned = nodeToBeReturned.next;
                }
            }
        }
        return null;
    }
}
