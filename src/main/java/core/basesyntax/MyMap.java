package core.basesyntax;

import java.util.Set;

public interface MyMap<K, V> {
    void put(K key, V value);

    V remove(K key);

    boolean containsKey(K key);

    V getValue(K key);

    int getSize();

    void clear();

    Set<K> keySet();
}
