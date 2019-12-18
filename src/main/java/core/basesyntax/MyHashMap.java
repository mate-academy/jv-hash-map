package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private Node<K, V>[] buckets;
    private int size = 0;

    public MyHashMap() {
        buckets = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int bucketIndex = defineBucket(key);
        Node<K, V> now = buckets[bucketIndex];
        if (now == null) {
            buckets[bucketIndex] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (now != null) {
            if (key == now.key || key != null && key.equals(now.key)) {
                now.value = value;
                return;
            }
            if (now.next == null) {
                now.next = new Node<>(key, value, null);
                break;
            }
            now = now.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return buckets[0].value;
        }
        int bucketIndex = defineBucket(key);
        Node<K, V> lookFor = buckets[bucketIndex];
        while (lookFor != null) {
            if (key == lookFor.key || key != null && key.equals(lookFor.key)) {
                return lookFor.value;
            }
            lookFor = lookFor.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int defineBucket(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % buckets.length - 1);
    }

    private void resize() {
        if (size >= (int) buckets.length * DEFAULT_LOAD_FACTOR) {
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[oldBuckets.length * 2];
            size = 0;
            for (int i = 0; i < oldBuckets.length; i++) {
                Node<K, V> toAddAgain = oldBuckets[i];
                while (toAddAgain != null) {
                    put(toAddAgain.key, toAddAgain.value);
                    toAddAgain = toAddAgain.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node (K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
