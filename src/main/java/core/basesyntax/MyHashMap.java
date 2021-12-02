package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K,V>[] table;

    private int size;

    private final float loadFactor;

    private int threshold;

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        } //Bitwise exclusive OR(returns 0 if both bits are the same)

        public final boolean equals(Object o) {

            if (o == this) {
                return true;
            }

            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue())) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        Node<K,V>[] tab = table;
        int tabLen = 0;
        if (tab != null) {
            tabLen = tab.length;
        }
        Node<K,V> curr;
        int i;
        if (tab == null || tabLen == 0) {
            tab = resize();
            tabLen = tab.length;
        }
        //Bitwise AND (&) returns 1 if and only if both bits are 1, else returns 0
        i = (tabLen - 1) & hash;//index of bucket where to put value
        curr = tab[i];//посмотрим что там было в бакете до записи в него
        if (curr == null) { //если по данному индексу в table еще нет ноды
            tab[i] = newNode(hash, key, value, null);
        } else { //если нода уже есть
            Node<K,V> existing = null; // existing mapping for key
            if (curr.hash == hash && (curr.key == key || (key != null && key.equals(curr.key)))) {
                existing = curr;//если key и hash совпадают, то скопируем curr в existing
            } else { //проверим на совпадение по key и hash всю цепочку нод .next от curr до конца
                Node<K,V> next;
                while (true) { //вечный цикл (мы не знаем сколько всего нод в списке)
                    next = curr.next;
                    if (next == null) { //значит проверили все ноды и совпадения не выявлено
                        curr.next = newNode(hash, key, value, null);
                        //дописываем в конец списка новую ноду
                        break;//и прерываем цикл
                    }
                    //если на предыдущих итерациях не сработал break в выше расположенном if(),
                    //значит до конца списка еще не дошли и сечас мы проверим у next его key и hash
                    if (next.hash == hash && (next.key == key
                            || (key != null && key.equals(next.key)))) {
                        existing = next;//если key и hash совпадают, то скопируем next в existing
                        break;//и прерываем цикл
                    }
                    curr = next;//переходим к следующей ноде, но уже на следующей итерации
                }
            }
            if (existing != null) { //если с таким key и hash уже была запись в списке
                existing.value = value;//запишем в .value новое значение
                return;
            }
        }
        if (++size > threshold) { //увеличиваем на 1 size и после сравниваем с threshold
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> e = getNode(hash(key), key);
        return e == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    } //^ Bitwise exclusive OR (returns 0 if both bits are the same, else returns 1)
    //>>> Unsigned Right Shift Operator (11110000 >>> 2 = 00111100)

    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap;
        int newThr = 0;
        if (oldCap > 0) {
            newCap = oldCap << 1; // double capacity
            newThr = oldThr << 1; // double threshold
        } else { //oldCap=0,oldThr=0 (zero initial threshold signifies using defaults)
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * newCap);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        size = 0;
        for (int j = 0; j < oldCap; ++j) { //нужно перенести все элементы из oldTab в newTab
            Node<K,V> e = oldTab[j];
            if (e != null) {
                put(e.key, e.value);
                if (e.next != null) { //то нужно перенести в newTab всю цепочку
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if (next != null) {
                            put(next.key, next.value);
                        }
                        e = next;
                    } while (e != null);
                }
            }
        }
        return table;
    }

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab;
        int tabLen;
        if ((tab = table) != null && (tabLen = tab.length) > 0) {
            Node<K,V> first = tab[(tabLen - 1) & hash];
            K k = first.key;
            Node<K,V> next;
            if (first.hash == hash && (k == key || (key != null && key.equals(k)))) {
                return first;
            }
            if ((next = first.next) != null) {
                do {
                    if (next.hash == hash
                            && ((k = next.key) == key || (key != null && key.equals(k)))) {
                        return next;
                    }
                } while ((next = next.next) != null);
            }
        }
        return null;
    }

}
