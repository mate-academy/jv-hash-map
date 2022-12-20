package core.basesyntax;

import java.util.List;
import java.util.Set;

public interface MyMap<K, V> {

    void put(K key, V value);

    V getValue(K key);

    int getSize();

    public Set<K> getKeys();

    public List<V> getValues();
}
