package core.basesyntax;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMAL_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size = 0;
    //private List<Entry<K,V>>[]table = new ArrayList[DEFAULT_INITIAL_CAPACITY];
    //private float threshold = (table.length * DEFAULT_LOAD_FACTOR);

    private List<Node<K, V>> table = new ArrayList<Node<K, V>>(DEFAULT_INITIAL_CAPACITY);
    private float threshold;


    public MyHashMap() {
        table = new ArrayList<>(DEFAULT_INITIAL_CAPACITY);
        for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++) {
            table.add(new Node<K,V>());
        }
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
    }



    @Override
    public void put(K key, V value) {
        int index = hash(key);

        if (size >= threshold) {
            resize();
        }

        for (Node<K,V> node : table) {
            Node<K,V> current = node;
            if (current.next != null) {
                do {
                    if ((key == null && current.getKey() == null)
                            || (key != null && key.equals(current.getKey()))) {
                        current.setValue(value);
                        current.setKey(key);
                        if (current.next != null) {

                        }
                        return;
                    }
                    current = current.next;

                } while (current.next == null);


            }

            if ((key == null && node.getKey() == null)
                    || (key != null && key.equals(node.getKey()))) {
                node.setValue(value);
                node.setKey(key);
                return;
            }
        }
        table.add(index, new Node<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);

        for (Node<K, V> entry : table) {
            Node<K,V> current = entry;
            if (current.next != null) {
                do {
                    if ((key == null && current.getKey() == null)
                            || (key != null && key.equals(current.getKey()))) {
                        return current.getValue();
                    }
                    current = current.next;
                } while (current.next == null);
            }

            if ((key == null && entry.getKey() == null)
                    || (key != null && key.equals(entry.getKey()))) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode()) % table.size());
    }

    private void resize() {
        /*
        int oldCapacity = table.size();
        int newCapacity = oldCapacity * 2;
        if (newCapacity > MAXIMAL_CAPACITY) {
            newCapacity = MAXIMAL_CAPACITY;
        }

        List<Node<K, V>> newTable = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newTable.add(new Node());
        }

        threshold = newCapacity * DEFAULT_LOAD_FACTOR;

        for (Node<K, V> bucket : table) {
            Node current = bucket;
            if (current.next != null) {
                int newIndex = current.getKey() == null ? 0 :
                        Math.abs(current.getKey().hashCode()) % newCapacity;
                newTable.add(newIndex,current);
            }
        }
        table = newTable;
         */
    }

}
