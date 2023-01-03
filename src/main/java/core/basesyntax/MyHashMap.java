package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Node)) {
                return false;
            }
            Node<K, V> node = (Node) obj;
            return node.key.equals(key) && node.value.equals(value)
                    && node.next.equals(next) && node.hash == hash;
        }
    }

    private static final int DEFAULT_SIZE_OF_ARRAY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table = new Node[DEFAULT_SIZE_OF_ARRAY];
    private Node<K, V> node;
    private int hash;
    private int size;

    @Override
    public void put(K key, V value) {
        node = new Node<>(hash, key, value, null);
        int index = getIndexOfArray(key);
        K k = node.key;
        checkSizeOfArray(table);
        if (key == null) {
            table[0] = node;
            size++;
        } else {
            if (table[index] != null && key.hashCode() == index && k.equals(key)) {
                table[index].value = value;
            } else if (table[index] != null) {
                Node<K, V> currentNode = table[index];
                while (currentNode != null) {
                    if (currentNode.key.equals(key)) {
                        currentNode = node;
                        break;
                    } else {
                        currentNode.next = node;
                    }
                }
            } else {
                table[index] = node;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexOfArray(key);
        if (table[index].key.equals(key) && table[index].next == null) {
            return table[index].value;
        } else {
            if (table[index].next != null) {
                Node<K, V> currentNode = table[index].next;
                while (currentNode.key.equals(key)) {
                    return currentNode.value;
                }
            }
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getIndexOfArray(K key) {
        if (key == null) {
            return 0;
        } else {
            return key.hashCode() % DEFAULT_SIZE_OF_ARRAY;
        }
    }

    public void checkSizeOfArray(Node<K, V>[] table) {
        if (table.length >= (DEFAULT_SIZE_OF_ARRAY * LOAD_FACTOR)) {
            table = new Node[DEFAULT_SIZE_OF_ARRAY << 1];
        }
    }
}
