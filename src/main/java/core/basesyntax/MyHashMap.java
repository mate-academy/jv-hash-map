package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    final int START_CAPACITY = 16;
    int size = 0;
    Node<K, V>[] table = (Node<K, V>[]) new Node[START_CAPACITY];

    @Override
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        int keyHash = key == null ? 0 : key.hashCode();
        Node<K, V> head = table[index];
        Node<K, V> current = head;

        while (current != null) {
            if (current.hash == keyHash && (current.key == key) || key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(keyHash, key, value, head);
        table[index] = newNode;
        size++;

    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        int keyHash = key == null ? 0 : key.hashCode();
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.hash == keyHash && Objects.equals(key, current.key)) {
                return current.value;
            } else {
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % START_CAPACITY;
    }

    private class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            return true;
        }

        private MyHashMap getEnclosingInstance() {
            return MyHashMap.this;
        }
    }
}
