package core.basesyntax;

import java.util.List;

public interface MyMap<K, V> {

    void put(K key, V value);

    V getValue(K key);

    int getSize();

    public List<K> getKeys();

    public List<V> getValues();
}
