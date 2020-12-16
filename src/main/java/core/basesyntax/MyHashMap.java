package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int index = indexFor(hash(key), table.length);
        for (Node<K,V> elem = table[index]; elem != null; elem = elem.next) {
            Object k;
            if (elem.hash == hash(key) && ((k = elem.key) == key || key.equals(k))) {
                elem.value = value;
                return;
            }
        }
        addEntry(hash(key),key,value,index);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            for (Node<K,V> elem = table[0]; elem != null; elem = elem.next) {
                if (elem.key == null) {
                    return elem.value;
                }
            }
        }
        int index = indexFor(hash(key), table.length);
        for (Node<K,V> elem = table[index]; elem != null; elem = elem.next) {
            Object currentKey;
            if (elem.hash == hash(key) && ((currentKey = elem.key) == key
                    || key.equals(currentKey))) {
                return elem.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize(int newCapacity) {
        Node[] newTable = new Node[newCapacity];
        rehash(newTable);
        table = newTable;
        threshold = (int)(newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private void rehash(Node[] newTable) {
        Node[] source = table;
        int newCapacity = newTable.length;
        for (int j = 0; j < source.length; j++) {
            Node<K, V> element = source[j];
            if (element != null) {
                source[j] = null;
                do {
                    Node<K, V> next = element.next;
                    int i = indexFor(element.hash, newCapacity);
                    element.next = newTable[i];
                    newTable[i] = element;
                    element = next;
                } while (element != null);
            }
        }
    }

    private void addEntry(int hash, K key, V value, int bucketIndex) {
        Node<K,V> elem = table[bucketIndex];
        table[bucketIndex] = new Node<>(hash,key,value,elem);
        if (size++ >= threshold) {
            resize(2 * table.length);
        }

    }

    private void putForNullKey(V value) {
        for (Node<K,V> elem = table[0]; elem != null; elem = elem.next) {
            if (elem.key == null) {
                elem.value = value;
                return;
            }
        }
        addEntry(0,null,value,0);
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

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
}
