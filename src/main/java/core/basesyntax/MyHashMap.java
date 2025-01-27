package core.basesyntax;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;

    Map<String, Integer> myNewMap = new HashMap<>();
    private Object[] keys;
    private Object[] values;

    public MyHashMap() {
        keys = new Object[DEFAULT_CAPACITY];
        values = new Object[DEFAULT_CAPACITY];
    }

    private int getHash(K key) {
        int hash = (key == null) ? 0 : Math.abs(key.hashCode()) % keys.length;
        return hash;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getHash(key);
        while (keys[index] != null) {
            if (keys[index].equals(key)) {
                values[index] = value;
                return;
            }
            index = (index + 1) % keys.length;
        }
        keys[index] = key;
        values[index] = value;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V getValue(K key) {
        int index = getHash(key);
        while (keys[index] != null) {
            if (Objects.equals(keys[index], key)) {
               return (V) values[index];
            }
            index = (index + 1) % keys.length;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    public void resize() {
        if (size >= keys.length * LOAD_FACTOR) {
            Object[] oldKeys = keys;
            Object[] oldValues = values;
            keys = new Object[oldKeys.length * 2];
            values = new Object[oldValues.length * 2];
            size = 0;
            for (int i = 0; i < oldKeys.length; i++) {
                if (oldKeys[i] != null) {
                    put ((K) oldKeys[i], (V) oldValues[i]);
                }
            }
        }
    }
}
