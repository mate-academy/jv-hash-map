package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DOUBLING_FACTOR = 2;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    @SuppressWarnings({"unchecked"})
    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.size = 0;
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            threshold *= DOUBLING_FACTOR;
            resize();
        }
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int index = (table.length - 1) & newNode.hash;
        if (table[index] == null) {
            justAddNewNode(index, newNode);
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                if (theseKeysAreEqual(current.key, newNode.key)) {
                    reWrightValue(current, newNode);
                    return;
                } else {
                    current = current.next;
                }
            }
            if (theseKeysAreEqual(current.key, newNode.key)) {
                reWrightValue(current, newNode);
                return;
            }
            addNextNewNode(current, newNode);
        }
    }

    @Override
    public V getValue(K key) {
        int index = (table.length - 1) & hash(key);
        if (index < table.length && table[index] != null) {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.hash == hash(key)
                        && Objects.equals(current.key, key)) {
                    return current.value;
                } else {
                    current = current.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return 0;
        return size;
    }

    @Override
    public String toString() {
        return "table=" + Arrays.toString(table);
    }

    @SuppressWarnings({"unchecked"})
    private void resize() {
        Node<K,V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[oldTable.length * DOUBLING_FACTOR];
        size = 0;
        transfer(oldTable);
    }

    private void justAddNewNode(int index, Node<K, V> newNode) {
        table[index] = newNode;
        size++;
    }

    private void addNextNewNode(Node<K, V> current, Node<K, V> newNode) {
        current.next = newNode;
        size++;
    }

    private boolean theseKeysAreEqual(K currentKey, K newKey) {
        return hash(currentKey) == hash(newKey)
                && (Objects.equals(currentKey, newKey));
    }

    private void reWrightValue(Node<K, V> current, Node<K, V> newNode) {
        current.value = newNode.value;
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> oldNode : oldTable) {
            if (oldNode != null) {
                Node<K, V> current = oldNode;
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
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

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (obj.getClass().equals(Node.class)) {
                Node<K,V> current = (Node<K,V>) obj;
                return ((hash == current.hash)
                        && Objects.equals(key, current.key)
                        && Objects.equals(value, current.value)
                        && Objects.equals(next, current.next));
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + hash;
            result = 31 * result + (key == null ? 0 : key.hashCode());
            result = 31 * result + (value == null ? 0 : value.hashCode());
            result = 31 * result + (next == null ? 0 : next.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return "{"
                    + "key=" + key
                    + ", value=" + value
                    + ", next=" + next
                    + "}";
        }
    }
}
