package core.basesyntax;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float LOAD_FACTOR = 0.75f;
    static final int INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;

    private int size;
    private int threshold;
    private MyNode<K, V>[] table;

    private static class MyNode<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private MyNode<K, V> next;

        MyNode(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            resize();
        }

        int index = getIndex(key);
        MyNode<K, V> node = table[index];

        if (node == null) {
            table[index] = new MyNode<>(
                    hashCode(key),
                    key, value);
        } else {
            MyNode<K, V> pointer = node;
            while (pointer != null) {
                if (pointer.hash == hashCode(key)
                        && Objects.equals(pointer.key, key)) {
                    pointer.value = value;
                    return;
                }
                pointer = pointer.next;
            }

            while (node.next != null) {
                node = node.next;
            }
            node.next = new MyNode<>(
                    hashCode(key),
                    key, value);
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        MyNode<K,V> e = getNode(key);
        if (e == null) {
            return null;
        } else {
            return e.value;
        }
    }

    @Override
    public V remove(K key) {
        MyNode<K,V> e = removeNode(key);
        if (e == null) {
            return null;
        } else {
            return e.value;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private MyNode<K,V> removeNode(K key) {
        if (table != null && table.length > 0) {
            int hash = hashCode(key);
            MyNode<K, V> found = table[getIndex(key)];
            MyNode<K, V> prev = null;
            while (found != null) {
                if (found.hash == hash && Objects.equals(found.key, key)) {
                    if (prev == null) {
                        table[getIndex(key)] = found.next;
                    } else {
                        prev.next = found.next;
                    }
                    return found;
                } else {
                    prev = found;
                    found = found.next;
                }
            }
        }
        return null;
    }

    private MyNode<K, V> getNode(K key) {
        if (table != null && table.length > 0) {
            int hash = hashCode(key);
            MyNode<K, V> found = table[getIndex(key)];
            while (found != null) {
                if (found.hash == hash
                        && Objects.equals(found.key, key)
                ) {
                    return found;
                } else {
                    found = found.next;
                }
            }
        }
        return null;
    }

    private int getIndex(K key) {
        if (table == null || table.length == 0) {
            throw new IllegalStateException("Hash table is not initialized or has no capacity.");
        }
        int hash = key == null ? 0 : hashCode(key);
        return Math.abs(hash % table.length);
    }

    private int hashCode(Object key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(31 * key.hashCode());
    }

    private void resize() {
        MyNode<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity;
        int newThreshold;

        if (oldCapacity > 0) {
            newCapacity = oldCapacity << 1;
            newThreshold = oldThreshold << 1;
        } else {
            newCapacity = INITIAL_CAPACITY;
            newThreshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        }

        threshold = newThreshold;
        MyNode<K, V>[] newTable = new MyNode[newCapacity];
        table = newTable;

        if (oldTable != null) {
            MyNode<K,V> pointer;
            for (MyNode<K, V> kvMyNode : oldTable) {
                pointer = kvMyNode;
                while (pointer != null) {
                    MyNode<K, V> next = pointer.next;
                    int index = getIndex(pointer.key);
                    pointer.next = newTable[index];
                    newTable[index] = pointer;
                    pointer = next;
                }
            }
        }
    }
}
