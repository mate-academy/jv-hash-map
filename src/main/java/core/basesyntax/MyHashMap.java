package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_LENGTH = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int actualLoadFactorNumber;
    private int size;
    private int newLength;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        Node<K, V>[] oldTab = table;
        if (table == null || size >= actualLoadFactorNumber) {
            oldTab = resize();
        }
        int hash = hash(key);
        int index = Math.abs(hash(key) % newLength);
        table = addByIndex(hash, key, value, oldTab, index);
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = Math.abs(hash(key) % newLength);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (key == null && node.key == null || node.key != null && node.key.equals(key)) {
                return node.value;
            }
        }
        return table[index].value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V> [] addByIndex(int hash, K key, V value, Node<K, V>[] oldTab, int index) {
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        Node<K, V> currentBucket = oldTab[index];
        if (currentBucket == null) {
            oldTab[index] = newNode;
            size++;
        } else {
            Node<K, V> prevNode = null;
            while (currentBucket != null) {
                if (Objects.equals(currentBucket.key, key)) {
                    currentBucket.value = value;
                    return oldTab;
                }
                prevNode = currentBucket;
                currentBucket = currentBucket.next;
            }
            prevNode.next = newNode;
            size++;
        }
        return oldTab;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldLength;
        if (oldTable == null) {
            oldLength = 0;
        } else {
            oldLength = oldTable.length;
        }
        if (oldLength == 0) {
            return createDefaultSizeTable();
        } else if (size >= actualLoadFactorNumber) {
            size = 0;
            newLength = oldLength << 1;
            actualLoadFactorNumber = (int) (DEFAULT_LOAD_FACTOR * newLength);
            return arrayCopy(oldTable);
        }
        return oldTable;
    }

    private Node<K, V>[] arrayCopy(Node<K, V>[] oldTable) {
        Node<K, V>[] newTab = (Node<K,V>[]) new Node[newLength];
        table = newTab;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> bucket = oldTable[i];
            for (Node<K, V> node = bucket; node != null; node = node.next) {
                int newIndex = Math.abs(node.hash % newLength);
                addByIndex(node.hash, node.key, node.value, newTab, newIndex);
            }
        }
        return newTab;
    }

    private Node<K, V>[] createDefaultSizeTable() {
        actualLoadFactorNumber = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_LENGTH);
        newLength = DEFAULT_INITIAL_LENGTH;
        return (Node<K,V>[]) new Node[newLength];
    }

    private static class Node<K, V> {
        private static final int PRIME_NUMBER = 31;
        private static final int SIMPLE_NUMBER = 17;
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            final int prime = PRIME_NUMBER;
            int result = SIMPLE_NUMBER;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Node<K, V> other = (Node<K, V>) obj;
            if (key != null ? !key.equals(other.key) : other.key != null) {
                return false;
            }
            return value != null ? value.equals(other.value) : other.value == null;
        }
    }
}
