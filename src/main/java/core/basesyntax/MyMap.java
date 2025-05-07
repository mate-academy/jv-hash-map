package core.basesyntax;

import java.util.Map;

public interface MyMap<K, V> {
    void put(K key, V value);

    void putAll(Map<K, V> m);

    V getValue(K key);

    V remove(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    int getSize();

    boolean isEmpty();
}
