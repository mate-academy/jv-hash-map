package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int threshold;
    private Node<K, V>[] elements;

    public MyHashMap() {
        elements = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (checkKeyByNull(key)){
            putForNullKey(value);
            return;
        }
        int hash = getHashCode(key);
        int index = getIndex(hash);
        if (elements[index] == null){
            elements[index] = new Node<>(key,value,null);
        } else{
            Node<K, V> temp = elements[index];
            while(temp.next != null){
                if (key == temp.key || Objects.equals(key,temp.key)){
                    temp.value = value;
                    return;
                }
                temp = temp.next;
            }
            temp.next = new Node<>(key,value,null);
        }
        size++;
        if (size > threshold){
            resize();
        }
    }

    // TODO:
    @Override
    public V getValue(K key) {
        return null;
    }

    // TODO:
    @Override
    public int getSize() {
        return size;
    }

    private boolean checkKeyByNull(K key){
        return key == null;
    }

    private int getHashCode(K key){
        int hashCode = key.hashCode();
        return hashCode ^ (hashCode >>> DEFAULT_CAPACITY);
    }

    private int getIndex(int hash){
        return hash & (elements.length - 1);
    }

    // TODO:
    private boolean putForNullKey(V value){
        return false;
    }
    // TODO:
    private boolean resize(){
        return true;
    }

    private static class Node<K,V>{
        final K key;
        V value;
        Node <K, V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
