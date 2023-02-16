
package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hashKey = indexFromHash(key);
        Node<K, V> node = new Node<>(key, value);
        if (table[hashKey] == null) {
            table[hashKey] = node;
            size++;
        } else {
            Node<K, V> nodeCurrent = table[hashKey];
            while (nodeCurrent != null) {
                if ((key == null && table[hashKey].value != null) || nodeCurrent.key.equals(key)) {
                    nodeCurrent.value = value;
                    break;
                }
                if (!nodeCurrent.key.equals(key) && nodeCurrent.next == null) {
                    nodeCurrent.next = node;
                    size++;
                    break;
                }
                nodeCurrent = nodeCurrent.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[indexFromHash(key)];
        while (currentNode != null) {
            if (key != null && key.equals(currentNode.key) || currentNode.key == key) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        if (size > table.length * LOAD_FACTOR) {
            table = new Node[oldTab.length * GROW_FACTOR];
            size = 0;
            for (Node<K, V> kvNode : oldTab) {
                Node<K, V> currentNode = kvNode;
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private int indexFromHash(K key) {
        if (key == null) {
            return 0;
        }
        if (key.hashCode() == 0) {
            return 1;
        }
        return Math.max(key.hashCode() % table.length, 0);
    }

    static class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Node)) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash
                    && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key);
        }
    }
}
