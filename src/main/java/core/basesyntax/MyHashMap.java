package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int keyHashCode = key.hashCode();
        int index = Math.abs(keyHashCode % table.length);

        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (true) {
                if (currentNode.key != null && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            }
        }

        if ((float) size / table.length > DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int keyHashCode = key.hashCode();
        int index = Math.abs(keyHashCode % table.length);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            return null;
        }
        if (Objects.equals(currentNode.key,key)) {
            return currentNode.value;
        }
        while (currentNode.next != null) {
            currentNode = currentNode.next;
            if (Objects.equals(currentNode.key,key)) {
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putForNullKey(V value) {
        if (table[0] == null) {
            table[0] = new Node<>(null, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[0];
            while (true) {
                if (currentNode.key == null) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(null, value, null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    private V getForNullKey() {
        Node<K, V> currentNode = table[0];
        while (currentNode != null) {
            if (currentNode.key == null) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {

            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Node<?, ?> e) {
                return Objects.equals(key, e.key) && Objects.equals(value, e.value);
            }
            return false;
        }
    }
}
