package core.basesyntax;

import java.util.Map;

public interface MyMap<K, V> { //extends Map<K, V> {
    V put(K key, V value);

    V getValue(K key);

    int getSize();
}
