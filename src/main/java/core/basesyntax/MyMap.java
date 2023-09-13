package core.basesyntax;

public interface MyMap<K, V> {
    void put(K key, V value);

    V getValue(K key);

    int getSize();

    //P.s: you can implement other methods of the Map interface.
    V remove(K key);
    void clear();
    boolean containsKey(Object key);
    boolean containsValue(Object value);
    boolean isEmpty();
}
