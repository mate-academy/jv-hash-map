package core.basesyntax;

public interface MyMap<K, V> {
    void put(K key, V value);

    V getValue(K key);

    int getSize();

    V remove(Object key);

    boolean containsKey(K key);

    boolean containsValue(V value);
}
