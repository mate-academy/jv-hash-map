package core.basesyntax;

import java.util.List;
import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final double LOAD_LEVEL = 0.75;
    private Node<K, V> [] table;
    private int size;

    public MyHashMap(){
        table = new Node[CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_LEVEL){
            resize();
        }
        int index = findIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = newNode;
            size++;
            return;
        }
        if (Objects.equals(key, node.key)) {
            node.value = value;
            return;
        }
        while (node.next != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;

        }
        node.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(key);
        Node<K, V> node = table[index];
        while (node != null){
            if (Objects.equals(node.key, key)){
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int findIndex (K key){
        if (key != null){
            return ((key.hashCode() & (table.length - 1)));
        }
        return 0;
    }

    private void resize (){
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> node: oldTable){
            while (node != null){
                put(node.key, node.value);
                node = node.next;
            }
        }
    }


    class Node<K, V>{
        private K key;
        private V value;
        private  Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
