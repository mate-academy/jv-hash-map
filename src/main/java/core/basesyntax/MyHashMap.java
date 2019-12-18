package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity;
    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.buckets = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size > capacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = hash(key);

        Node<K, V> newNode = buckets[index];
        while (newNode != null) {
            if (key == newNode.key || key != null && key.equals(newNode.key)) {
                newNode.value = value;
                return;
            }
            newNode = newNode.next;
        }
        Node<K, V> nextElementOfBucket = new Node<>(key, value, buckets[index]);
        buckets[index] = nextElementOfBucket;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> newNode = buckets[index];
        while (newNode != null) {
            if (key == null || key.equals(newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node[] oldBucket = buckets;
        buckets = new Node[capacity *= 2];
        for (Node<K, V> futureBucket : oldBucket) {
            while (futureBucket != null) {
                put(futureBucket.key, futureBucket.value);
                futureBucket = futureBucket.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : (key.hashCode() & 31) % capacity + 1;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
