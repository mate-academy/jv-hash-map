package core.basesyntax;

import java.util.Collection;
import java.util.Set;

public interface MyMap<K, V> {

    void put(K key, V value);

    void clear();

    boolean isEmpty();

    boolean containsKey(K key);

    boolean containsValue(V value);

    V getValue(K key);

    int getSize();

    Collection<V> values();

    Set<K> keySet();
}
