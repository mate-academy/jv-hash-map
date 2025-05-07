package core.basesyntax;

import java.util.LinkedList;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K,V> {

    private static final int INITIAL_CAPACITY = 16;
    private LinkedList<Entry<K, V>>[] table;
    private int size;
    private final float lf = 0.75f;

    private static class Entry<K, V> {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "key=" + key + ", value=" + value;
        }
    }

    public MyHashMap() {
        table = new LinkedList[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public V put(K key, V value) {
        int index = getIndex(key);

        if (((float) size / table.length) >= lf) {
            reSize();
        }

        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        for (Entry<K, V> entry : table[index]) {
            if (Objects.equals(entry.key, key)) {
                // Якщо ключ знайдено, оновлюємо значення
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }

        table[index].add(new Entry<>(key, value));
        size++;
        return null;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);

        // Якщо бакет відсутній, повертаємо null
        if (table[index] == null) {
            return null;
        }

        // Шукаємо значення в бакеті
        for (Entry<K, V> entry : table[index]) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
        }
        return null; // Якщо ключ не знайдено
    }

    @Override
    public int getSize() {
        return size;
    }

    private void reSize() {
        LinkedList<Entry<K, V>>[] oldTable = table;
        table = new LinkedList[oldTable.length * 2];
        size = 0;

        for (LinkedList<Entry<K, V>> bucket : oldTable) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }

    private int getIndex(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode())) % table.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                sb.append(table[i]).append(", ");
            }
        }
        return sb.toString();
    }
}
