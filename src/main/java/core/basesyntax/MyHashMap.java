package core.basesyntax;

import java.util.LinkedList;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 *  * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 *  * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    public static final double LOAD_FACTOR = 0.75;
    public static final int DEFAULT_CAPACITY = 16;
    public static final int GROW_RATE = 2;

    private LinkedList<Data<K, V>>[] table;
    private int size;

    public MyHashMap() {
        init(DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int id = getId(key);
        for (Data<K, V> data : table[id]) {
            if (data.key == key || data.key.equals(key)) {
                data.value = value;
                return;
            }
        }

        table[id].addLast(new Data<>(key, value));
        ++size;
        resize();
    }

    @Override
    public V getValue(K key) {
        int id = getId(key);
        for (Data<K, V> data : table[id]) {
            if (data.key == key || data.key.equals(key)) {
                return data.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getId(K key) {
        return key == null ? 0 : 1 + (Math.abs(key.hashCode() % table.length - 1));
    }

    private void init(int capacity) {
        table = new LinkedList[capacity];
        size = 0;
        for (int i = 0; i < this.table.length; ++i) {
            this.table[i] = new LinkedList<>();
        }
    }

    private void resize() {
        if (size < table.length * LOAD_FACTOR) {
            return;
        }

        LinkedList<Data<K, V>>[] tmp = table;
        init(tmp.length * GROW_RATE);

        for (LinkedList<Data<K, V>> data : tmp) {
            for (Data<K, V> kvData : data) {
                put(kvData.key, kvData.value);
            }
        }
    }

    private static class Data<K, V> {
        public K key;
        public V value;

        public Data(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
