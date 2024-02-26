package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;

    private MyEntry<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new MyEntry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }

        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key);
        MyEntry<K, V> entry = new MyEntry<>(key, value);
        if (table[index] == null) {
            table[index] = entry;
            size++;
        } else {
            MyEntry<K, V> current = table[index];
            while (current.next != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (Objects.equals(current.key, key)) {
                current.value = value;
            } else {
                current.next = entry;
                size++;
            }
        }
    }

    private void putForNullKey(V value) {
        MyEntry<K, V> entry = new MyEntry<>(null, value);
        if (table[0] == null) {
            table[0] = entry;
            size++;
        } else {
            MyEntry<K, V> current = table[0];
            while (current.next != null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key == null) {
                current.value = value;
            } else {
                current.next = entry;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        MyEntry<K, V> entry = table[index];
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        MyEntry<K, V>[] oldTable = table;
        table = new MyEntry[oldTable.length * GROW_FACTOR];
        size = 0;
        for (MyEntry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    static class MyEntry<K, V> {
        private final K key;
        private V value;
        private MyEntry<K, V> next;

        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
