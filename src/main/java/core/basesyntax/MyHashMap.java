package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int realCapacity;
    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        this.realCapacity = DEFAULT_INITIAL_CAPACITY;
        this.buckets = new Node[realCapacity];
    }

    @Override
    public void put(K key, V value) {
        if (size > realCapacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = hash(key);

        if (buckets[index] == null) {
            buckets[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> newNode = buckets[index];
            while (newNode != null) {
                if (key == newNode.key || key.equals(newNode.key)) {
                    newNode.value = value;
                    return;
                }
                newNode = newNode.next;
            }
            Node<K, V> nextElementOfBucket = new Node<>(key, value, buckets[index]);
            buckets[index] = nextElementOfBucket;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return buckets[0].value;
        }
        int index = hash(key);
        Node<K, V> newNode = buckets[index];
        while (newNode != null) {
            if (key.equals(newNode.key)) {
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
        Node[] pastBucket = buckets;
        buckets = new Node[realCapacity *= 2];
        for (Node<K, V> futureBucket : pastBucket) {
            while (futureBucket != null) {
                put(futureBucket.key, futureBucket.value);
                futureBucket = futureBucket.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : (key.hashCode() & 31) % realCapacity + 1;
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
