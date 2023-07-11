package core.basesyntax;

import java.util.List;

public interface MyMap<K, V> {
    void put(K key, V value);

    V getValue(K key);

    V remove(K key);

    List<K> getKeys();

    List<V> getValues();

    boolean isEmpty();

    int getSize();
}
