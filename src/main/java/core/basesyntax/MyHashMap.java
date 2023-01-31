package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private MyEntry<K, V>[] buckets;

    public MyHashMap() {
        this.buckets = new MyEntry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        MyEntry<K, V> myEntry = new MyEntry<>(key, value, null);
        int index = index(key);

        if (buckets[index] == null) {
            buckets[index] = myEntry;
            size++;
        } else {
            MyEntry<K, V> previousNode = null;
            MyEntry<K, V> currentNode = buckets[index];
            while (currentNode != null) {
                if (myEntry.getKey() == null && key == null || currentNode.getKey().equals(key)) {
                    currentNode.setValue(value);
                    return;
                }
                previousNode = currentNode;
                currentNode = currentNode.getNext();
            }
            if (previousNode != null) {
                previousNode.setNext(myEntry);
                size++;
            }
            if (size > buckets.length * LOAD_FACTOR) {
                resize();
            }
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;
        int index = index(key);
        MyEntry<K, V> myEntry = buckets[index];
        while (myEntry != null) {
            if (myEntry.getKey() == null && key == null || myEntry.getKey().equals(key)) {
                value = myEntry.getValue();
                break;
            }
            myEntry = myEntry.getNext();
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int tableSize = 2 * buckets.length;
        MyEntry<K, V>[] oldBuckets = buckets;
        buckets = new MyEntry[tableSize];
        size = 0;
        for (int i = 0; i < oldBuckets.length; i++) {
            if (oldBuckets[i] != null) {
                MyEntry<K, V> currentNode = oldBuckets[i];
                while (currentNode != null) {
                    put(currentNode.getKey(), currentNode.getValue());
                    currentNode = currentNode.getNext();
                }
            }
        }
    }

    private int index(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(Objects.hash(key) % buckets.length);
    }

    private static class MyEntry<K, V> {
        private final K key;
        private V value;
        private MyEntry<K, V> next;

        public MyEntry(K key, V value, MyEntry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(MyEntry<K, V> next) {
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public MyEntry<K, V> getNext() {
            return next;
        }
    }
}
