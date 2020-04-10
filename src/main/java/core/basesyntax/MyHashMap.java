package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_ARRAY_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        size = 0;
        buckets = new Node[DEFAULT_ARRAY_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (ensureCapacity(size)) {
            resize();
        }
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int index = index(key);
        if (buckets[index] == null) {
            buckets[index] = new Node(key, value);
            size++;
            return;
        }
        if (buckets[index].value == null) {
            buckets[index] = new Node(key, value);
            size++;
        } else {
            setBucket(index, key, value);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int index = index(key);
        return getBucket(index, key);
    }

    @Override
    public int getSize() {
        return size;
    }

    private int index(K key) {
        return Math.abs(key.hashCode() % (buckets.length));
    }

    private boolean ensureCapacity(int size) {
        return size > buckets.length * LOAD_FACTOR;
    }

    private void putForNullKey(V value) {
        if (buckets[0] == null) {
            buckets[0] = new Node(null, value);
            size++;
            return;
        }
        if (buckets[0].key == null) {
            buckets[0].value = value;
            return;
        }
        Node<K, V> currentNode = buckets[0];
        while (currentNode.next != null) {
            if (currentNode.next.key == null) {
                currentNode.next.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> entry = new Node(value);
        currentNode.next = entry;
        size++;
    }

    private V getForNullKey() {
        if (buckets[0] == null) {
            return null;
        }
        if (buckets[0].key == null) {
            return buckets[0].value;
        }
        Node<K, V> currentNode = buckets[0];
        while (currentNode.next != null) {
            if (currentNode.next.key == null) {
                return currentNode.next.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] oldData = buckets;
        buckets = new Node[buckets.length * 2];
        size = 0;
        for (int i = 0; i < oldData.length; i++) {
            if (oldData[i] != null) {
                put(oldData[i].key, oldData[i].value);
                while (oldData[i].next != null) {
                    oldData[i] = oldData[i].next;
                    put(oldData[i].key, oldData[i].value);
                }
            }
        }
    }

    private void setBucket(int index, K key, V value) {
        if (key.equals(buckets[index].key)) {
            buckets[index].key = key;
            buckets[index].value = value;
            return;
        }
        Node<K, V> currentNode = buckets[index];
        while (currentNode.next != null) {
            if (key.equals(currentNode.next.key)) {
                currentNode.next.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> entry = new Node(key, value);
        currentNode.next = entry;
        size++;
    }

    private V getBucket(int index, K key) {
        if (buckets[index] == null) {
            return null;
        }
        if (key.equals(buckets[index].key)) {
            return buckets[index].value;
        }
        Node<K, V> currentNode = buckets[index];
        while (currentNode.next != null) {
            if (key.equals(currentNode.next.key)) {
                return currentNode.next.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(V value) {
            this.value = value;
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
