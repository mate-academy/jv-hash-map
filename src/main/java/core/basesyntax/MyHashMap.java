package core.basesyntax;

import java.util.HashMap;


public class MyHashMap<K, V> implements MyMap<K, V> {
  private final HashMap<K,V> map;

    public MyHashMap() {
        this.map = new HashMap<>();
    }


    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public V getValue(K key) {
        return map.get(key);
    }

    @Override
    public int getSize() {
        return map.size();
    }
}
