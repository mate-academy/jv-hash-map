package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

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

        if (size + 1 > table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        MyEntry<K, V> entry = new MyEntry<>(key, value);
        if (table[index] == null) {
            table[index] = entry;
            size++;
        } else {
            MyEntry<K, V> current = table[index];
            while (current.getNext() != null) {
                if (Objects.equals(current.getKey(), key)) {
                    current.setValue(value);
                    return;
                }
                current = current.getNext();
            }
            if (Objects.equals(current.getKey(), key)) {
                current.setValue(value);
            } else {
                current.setNext(entry);
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
            while (current.getNext() != null) {
                if (current.getKey() == null) {
                    current.setValue(value);
                    return;
                }
                current = current.getNext();
            }
            if (current.getKey() == null) {
                current.setValue(value);
            } else {
                current.setNext(entry);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        MyEntry<K, V> entry = table[index];
        while (entry != null) {
            if (Objects.equals(entry.getKey(), key)) {
                return entry.getValue();
            }
            entry = entry.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return Math.abs(Objects.hashCode(key)) % table.length;
    }

    private void resize() {
        MyEntry<K, V>[] oldTable = table;
        table = new MyEntry[table.length * 2];
        size = 0;
        for (MyEntry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
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

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public MyEntry<K, V> getNext() {
            return next;
        }

        public void setNext(MyEntry<K, V> next) {
            this.next = next;
        }
    }
}
