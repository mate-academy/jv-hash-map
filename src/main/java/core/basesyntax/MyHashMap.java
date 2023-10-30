package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_ARRAY_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        if ((table) == null || (table.length) == 0) {
            resize();
        }
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        Node<K,V> e = getNode(key);
        return e == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode() % table.length));
    }

    private void resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int newCap;
        if (oldCap > 0) {
            newCap = oldCap * DEFAULT_ARRAY_FACTOR;
            threshold = threshold * DEFAULT_ARRAY_FACTOR;
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            threshold = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        table = (Node<K,V>[])new Node[newCap];
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> node = oldTab[j];
                if (node != null) {
                    oldTab[j] = null;
                    while (node != null) {
                        setNewLastNode(node);
                        node = node.next;
                    }
                }
            }
        }
    }

    private void setNewLastNode(Node<K, V> node) {
        int hash = hash(node.key);
        Node<K, V> newNode = new Node<>(hash,node.key, node.value, null);
        Node<K, V> last = getLastNodeOfIndex(hash);
        if (last == null) {
            table[hash] = newNode;
        } else {
            last.next = newNode;
        }
    }

    private Node<K, V> getLastNodeOfIndex(int index) {
        if (table == null || index > table.length || index < 0) {
            return null;
        }
        Node<K, V> node = table[index];
        if (node != null) {
            while (node.next != null) {
                node = node.next;
            }
            return node;
        }
        return null;
    }

    private Node<K,V> getNode(K key) {
        int hash = hash(key);
        Node<K,V> node = table[hash];
        if (node != null) {
            do {
                if (node.hash == hash && Objects.equals(key, node.key)) {
                    return node;
                }
            } while ((node = node.next) != null);
        }
        return null;
    }

    private void putValue(int hash, K key, V value) {
        Node<K, V> node = getNode(key);
        if (node != null) {
            node.value = value;
            return;
        }
        node = new Node(hash, key, value, null);
        setNewLastNode(node);
        if (++size > threshold) {
            resize();
        }
    }
}
